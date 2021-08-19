package eu.asangarin.monhun.block;

import eu.asangarin.monhun.block.entity.MHGatheringBlockEntity;
import eu.asangarin.monhun.network.MHNetwork;
import eu.asangarin.monhun.network.MHS2CPackets;
import eu.asangarin.monhun.network.PacketHelper;
import eu.asangarin.monhun.util.UtilityMethods;
import eu.asangarin.monhun.util.enums.MHGatheringType;
import eu.asangarin.monhun.util.interfaces.GatheringBlockEntityProvider;
import eu.asangarin.monhun.util.interfaces.IGatheringSpot;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
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
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class MHGatheringBlock extends Block implements GatheringBlockEntityProvider, IGatheringSpot {
	public static final EnumProperty<MHGatheringType> GATHERING_TYPE = EnumProperty.of("type", MHGatheringType.class);

	public MHGatheringBlock(Settings settings) {
		super(settings);
		setDefaultState(getDefaultStateWith(getStateManager().getDefaultState().with(GATHERING_TYPE, MHGatheringType.WHITE)));
	}

	protected abstract BlockState getDefaultStateWith(BlockState state);

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(GATHERING_TYPE);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return getDefaultState().with(GATHERING_TYPE, MHGatheringType.fromStack(ctx.getStack()));
	}

	@Override
	@SuppressWarnings({"ConstantConditions", "deprecation"})
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ItemStack stack = player.getStackInHand(hand);
		if (canGather(stack)) {
			playClientGatherEffects(world, pos);

			if (!world.isClient) {
				LootTable lootTable = world.getServer().getLootManager().getTable(getLootTableIdent());
				LootContext.Builder builder = new LootContext.Builder((ServerWorld) world).parameter(LootContextParameters.BLOCK_STATE, state);
				lootTable.generateLoot(builder.build(LootContextType.create().require(LootContextParameters.BLOCK_STATE).build()),
						(loot) -> player.getInventory().offerOrDrop(loot));
				if (!player.getAbilities().creativeMode) tryBreak(stack, world, player);
				playServerGatherEffects(world, pos);
				if (world.getBlockEntity(pos) instanceof MHGatheringBlockEntity gatherEntity && gatherEntity.gather()) {
					for (ServerPlayerEntity serverPlayer : PlayerLookup.tracking(gatherEntity))
						MHNetwork.sendToClient(MHS2CPackets.BLOCK_BREAK, serverPlayer, PacketHelper.blockPos(pos));
					world.removeBlock(pos, true);
				}
			}
			return ActionResult.SUCCESS;
		}

		if (player.getAbilities().creativeMode) {
			if (!world.isClient) {
				world.setBlockState(pos, cycle(state));
			}
			return ActionResult.SUCCESS;
		}

		return ActionResult.PASS;
	}

	protected void tryBreak(ItemStack stack, World world, PlayerEntity player) {}

	protected abstract boolean canGather(ItemStack stack);

	protected void playClientGatherEffects(World world, BlockPos pos) {}

	protected void playServerGatherEffects(World world, BlockPos pos) {}

	protected abstract Identifier getLootTableIdent();

	private BlockState cycle(BlockState state) {
		return state.with(GATHERING_TYPE, UtilityMethods.cycleEnum(MHGatheringType.class, state.get(GATHERING_TYPE)));
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext context) {
		MHGatheringType type = MHGatheringType.fromStack(stack);
		tooltip.add(type.getRarity().asText().formatted(Formatting.BOLD));
		final List<Text> resources = getAvailableTypes(type);
		if (resources == null) return;
		tooltip.add(new TranslatableText("block.monhun.possible_resources").formatted(Formatting.GRAY).formatted(Formatting.UNDERLINE));
		for (Text text : resources)
			tooltip.add(text.copy().formatted(Formatting.AQUA));
	}

	protected abstract List<Text> getAvailableTypes(MHGatheringType type);

	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
		for (MHGatheringType type : MHGatheringType.values())
			stacks.add(withType(type));
	}

	@Override
	public MHGatheringType getType(ItemStack stack) {
		return MHGatheringType.fromStack(stack);
	}

	private ItemStack withType(MHGatheringType type) {
		ItemStack stack = new ItemStack(this);
		NbtCompound nbt = new NbtCompound();
		nbt.putString("type", type.asString());
		stack.setNbt(nbt);
		return stack;
	}
}
