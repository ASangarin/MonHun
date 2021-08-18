package eu.asangarin.monhun.block;

import eu.asangarin.monhun.MonHun;
import eu.asangarin.monhun.block.entity.MHBugBlockEntity;
import eu.asangarin.monhun.util.interfaces.IColorProvider;
import eu.asangarin.monhun.util.interfaces.IGatheringSpot;
import eu.asangarin.monhun.util.interfaces.INoBox;
import eu.asangarin.monhun.item.MHToolItem;
import eu.asangarin.monhun.managers.MHBlocks;
import eu.asangarin.monhun.managers.MHSounds;
import eu.asangarin.monhun.util.UtilityMethods;
import eu.asangarin.monhun.util.enums.MHGatheringType;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
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
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("deprecation")
public class MHBugBlock extends BlockWithEntity implements BlockEntityProvider, INoBox, IGatheringSpot, IColorProvider {
	public static final Map<MHGatheringType, List<Text>> AVAILABLE_BUGS = new HashMap<>();
	public static final Identifier BUG_LOOTTABLE = MonHun.i("flying_bug");
	private static final BlockSoundGroup BUG_SOUNDS = new BlockSoundGroup(1.0F, 1.0F, MHSounds.BUTTERFLY_PLACE, MHSounds.SILENCE,
			MHSounds.BUTTERFLY_DEATH, MHSounds.SILENCE, MHSounds.SILENCE);

	public static final EnumProperty<MHGatheringType> ORE_TYPE = EnumProperty.of("type", MHGatheringType.class);

	public MHBugBlock() {
		super(FabricBlockSettings.of(MHBlocks.RESOURCE).luminance(1).strength(-1.0F, 0.2F).dynamicBounds().noCollision().sounds(BUG_SOUNDS));
		setDefaultState(getStateManager().getDefaultState().with(ORE_TYPE, MHGatheringType.WHITE));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(ORE_TYPE);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return Block.createCuboidShape(0.0D, 4.0D, 0.0D, 16.0D, 12.0D, 16.0D);
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (world.getBlockEntity(pos) instanceof MHBugBlockEntity bugEntity) bugEntity.cleanAnimation();
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return getDefaultState().with(ORE_TYPE, MHGatheringType.fromStack(ctx.getStack()));
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	@SuppressWarnings("ConstantConditions")
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ItemStack stack = player.getStackInHand(hand);
		if (stack.getItem() instanceof MHToolItem tool && tool.isBugNet()) {
			if (!world.isClient) {
				LootTable lootTable = world.getServer().getLootManager().getTable(BUG_LOOTTABLE);
				LootContext.Builder builder = new LootContext.Builder((ServerWorld) world).parameter(LootContextParameters.BLOCK_STATE, state);
				lootTable.generateLoot(builder.build(LootContextType.create().require(LootContextParameters.BLOCK_STATE).build()),
						(loot) -> player.getInventory().offerOrDrop(loot));
				if (!player.getAbilities().creativeMode) tool.tryBreak(stack, world, player);
				world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_BAT_TAKEOFF, SoundCategory.PLAYERS, 0.8F,
						world.random.nextFloat() * 0.1F + 1.1F);
				BlockEntity blockEntity = world.getBlockEntity(pos);
				if (blockEntity instanceof MHBugBlockEntity bugBlockEntity && bugBlockEntity.catchBug()) world.removeBlock(pos, true);
			}
			return ActionResult.SUCCESS;
		}

		if (player.getAbilities().creativeMode) {
			if (!world.isClient) world.setBlockState(pos, cycle(state));
			return ActionResult.SUCCESS;
		}

		return ActionResult.PASS;
	}

	@Override
	public void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
		if (!player.getAbilities().creativeMode && world.getBlockEntity(pos) instanceof MHBugBlockEntity bugBlockEntity) {
			if (!world.isClient) {
				SoundEvent sound = MHSounds.BUTTERFLY_HURT;
				if (bugBlockEntity.damage()) {
					world.removeBlock(pos, true);
					sound = MHSounds.BUTTERFLY_DEATH;
				}

				world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), sound, SoundCategory.BLOCKS, 0.5F, 1.0F);
				return;
			}

			bugBlockEntity.setHurt();
		}
	}

	private BlockState cycle(BlockState state) {
		return state.with(ORE_TYPE, UtilityMethods.cycleEnum(MHGatheringType.class, state.get(ORE_TYPE)));
	}


	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext context) {
		MHGatheringType type = MHGatheringType.fromStack(stack);
		tooltip.add(type.getRarity().asText().formatted(Formatting.BOLD));
		tooltip.add(new TranslatableText("block.monhun.possible_resources").formatted(Formatting.GRAY).formatted(Formatting.UNDERLINE));
		final List<Text> resources = AVAILABLE_BUGS.get(type);
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

	public int provideColor(ItemStack stack) {
		return MHGatheringType.fromStack(stack).getColor();
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new MHBugBlockEntity(pos, state);
	}

	private ItemStack withType(MHGatheringType type) {
		ItemStack stack = new ItemStack(this);
		NbtCompound nbt = new NbtCompound();
		nbt.putString("ore_type", type.asString());
		stack.setNbt(nbt);
		return stack;
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World abstractWorld, BlockState blockState, BlockEntityType<T> type) {
		return checkType(type, MHBlocks.BUG_BLOCK_ENTITY, (world, pos, state, be) -> MHBugBlockEntity.tick(be));
	}
}
