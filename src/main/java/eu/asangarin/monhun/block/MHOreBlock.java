package eu.asangarin.monhun.block;

import eu.asangarin.monhun.MonHun;
import eu.asangarin.monhun.block.entity.MHOreBlockEntity;
import eu.asangarin.monhun.item.MHPickaxeItem;
import eu.asangarin.monhun.managers.MHBlocks;
import eu.asangarin.monhun.managers.MHSounds;
import eu.asangarin.monhun.network.MHNetwork;
import eu.asangarin.monhun.network.MHS2CPackets;
import eu.asangarin.monhun.network.PacketHelper;
import eu.asangarin.monhun.util.UtilityMethods;
import eu.asangarin.monhun.util.enums.MHGatheringType;
import eu.asangarin.monhun.util.interfaces.IGatheringSpot;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("deprecation")
public class MHOreBlock extends HorizontalFacingBlock implements BlockEntityProvider, IGatheringSpot {
	public static final Map<MHGatheringType, List<Text>> AVAILABLE_ORES = new HashMap<>();
	public static final Identifier ORE_LOOTTABLE = MonHun.i("mining_outcrop");

	public static final EnumProperty<MHGatheringType> ORE_TYPE = EnumProperty.of("type", MHGatheringType.class);

	public MHOreBlock() {
		super(FabricBlockSettings.of(MHBlocks.RESOURCE).dynamicBounds().luminance(11).strength(12.0f).nonOpaque()
				.sounds(BlockSoundGroup.DEEPSLATE_TILES));
		setDefaultState(getStateManager().getDefaultState().with(ORE_TYPE, MHGatheringType.WHITE).with(FACING, Direction.EAST));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(ORE_TYPE).add(FACING);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite()).with(ORE_TYPE, MHGatheringType.fromStack(ctx.getStack()));
	}

	@Override
	public OffsetType getOffsetType() {
		return OffsetType.XZ;
	}

	@Override
	public float getMaxModelOffset() {
		return 0.15F;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		VoxelShape voxelShape = Block.createCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D);
		Vec3d vec3d = state.getModelOffset(world, pos);
		return voxelShape.offset(vec3d.x, vec3d.y, vec3d.z);
	}

	@Override
	@SuppressWarnings("ConstantConditions")
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ItemStack stack = player.getStackInHand(hand);
		if (stack.getItem() instanceof MHPickaxeItem tool) {
			for (int i = 0; i < 16; ++i) {
				double d = world.random.nextFloat() * 2.0F - 1.0F;
				double e = world.random.nextFloat() * 2.0F - 1.0F;
				double f = world.random.nextFloat() * 2.0F - 1.0F;
				if (!(d * d + e * e + f * f > 1.0D)) {
					world.addParticle(ParticleTypes.CRIT, false, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, d, e + 0.2D, f);
				}
			}

			if (!world.isClient) {
				LootTable lootTable = world.getServer().getLootManager().getTable(ORE_LOOTTABLE);
				LootContext.Builder builder = new LootContext.Builder((ServerWorld) world).parameter(LootContextParameters.BLOCK_STATE, state);
				lootTable.generateLoot(builder.build(LootContextType.create().require(LootContextParameters.BLOCK_STATE).build()),
						(loot) -> player.getInventory().offerOrDrop(loot));
				if (!player.getAbilities().creativeMode) tool.tryBreak(stack, world, player);
				world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), MHSounds.MINE_ORE, SoundCategory.BLOCKS, 0.5F,
						world.random.nextFloat() * 0.1F + 1.0F);
				BlockEntity blockEntity = world.getBlockEntity(pos);
				if (blockEntity instanceof MHOreBlockEntity oreBlockEntity && oreBlockEntity.mineOre()) {
					for (ServerPlayerEntity serverPlayer : PlayerLookup.tracking(blockEntity))
						MHNetwork.sendToClient(MHS2CPackets.BLOCK_BREAK, serverPlayer, PacketHelper.blockPos(pos));
					world.removeBlock(pos, true);
				}
			}
			return ActionResult.SUCCESS;
		}

		if (player.getAbilities().creativeMode) {
			if (!world.isClient) world.setBlockState(pos, cycle(state));
			return ActionResult.SUCCESS;
		}

		return ActionResult.PASS;
	}

	private BlockState cycle(BlockState state) {
		return state.with(ORE_TYPE, UtilityMethods.cycleEnum(MHGatheringType.class, state.get(ORE_TYPE)));
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext context) {
		MHGatheringType type = MHGatheringType.fromStack(stack);
		tooltip.add(type.getRarity().asText().formatted(Formatting.BOLD));
		tooltip.add(new TranslatableText("block.monhun.possible_resources").formatted(Formatting.GRAY).formatted(Formatting.UNDERLINE));
		final List<Text> resources = AVAILABLE_ORES.get(type);
		if (resources == null) return;
		for (Text text : resources)
			tooltip.add(text.copy().formatted(Formatting.AQUA));
	}

	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
		for (MHGatheringType type : MHGatheringType.values())
			stacks.add(withType(type));
	}

	@Override
	public MutableText getName() {
		return super.getName();
	}

	@Override
	public MHGatheringType getType(ItemStack stack) {
		return MHGatheringType.fromStack(stack);
	}

	private ItemStack withType(MHGatheringType type) {
		ItemStack stack = new ItemStack(this);
		NbtCompound nbt = new NbtCompound();
		nbt.putString("ore_type", type.asString());
		stack.setNbt(nbt);
		return stack;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new MHOreBlockEntity(pos, state);
	}
}
