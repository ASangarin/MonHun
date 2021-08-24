package eu.asangarin.monhun.block.gather;

import eu.asangarin.monhun.managers.MHBlocks;
import eu.asangarin.monhun.util.enums.MHGatheringType;
import eu.asangarin.monhun.util.interfaces.IDoubleBlock;
import eu.asangarin.monhun.util.interfaces.IGatheringSpot;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("deprecation")
public class MHBottomPlantBlock extends Block implements IDoubleBlock, IGatheringSpot {
	public MHBottomPlantBlock() {
		super(FabricBlockSettings.of(MHBlocks.RESOURCE).strength(1.0f).nonOpaque().noCollision().sounds(BlockSoundGroup.MOSS_BLOCK));
	}

	@Override
	@SuppressWarnings({"ConstantConditions", "deprecation"})
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		BlockState up = world.getBlockState(pos.up());
		if (up.getBlock() instanceof MHTopPlantBlock) return up.getBlock().onUse(up, world, pos.up(), player, hand, hit);
		return ActionResult.PASS;
	}

	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
		for (MHGatheringType type : MHGatheringType.values())
			stacks.add(withType(type));
	}

	private ItemStack withType(MHGatheringType type) {
		ItemStack stack = new ItemStack(this);
		NbtCompound nbt = new NbtCompound();
		nbt.putString("type", type.asString());
		stack.setNbt(nbt);
		return stack;
	}

	@Override
	public AbstractBlock.OffsetType getOffsetType() {
		return AbstractBlock.OffsetType.XZ;
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (direction.getAxis() == Direction.Axis.Y && (direction == Direction.UP) && (!(neighborState.getBlock() instanceof MHTopPlantBlock))) {
			return Blocks.AIR.getDefaultState();
		} else {
			return direction == Direction.DOWN && !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(
					state, direction, neighborState, world, pos, neighborPos);
		}
	}

	@Nullable
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockPos blockPos = ctx.getBlockPos();
		World world = ctx.getWorld();
		return blockPos.getY() < world.getTopY() - 1 && world.getBlockState(blockPos.up()).canReplace(ctx) ? super.getPlacementState(ctx) : null;
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		BlockPos blockPos = pos.up();
		world.setBlockState(blockPos,
				MHBlocks.TOP_PLANT_BLOCK.getDefaultState().with(MHColorGatheringBlock.GATHERING_TYPE, MHGatheringType.fromStack(itemStack)),
				Block.NOTIFY_ALL);
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		return this.canPlantOnTop(world.getBlockState(pos.down()));
	}

	protected boolean canPlantOnTop(BlockState floor) {
		return floor.isIn(BlockTags.DIRT) || floor.isOf(Blocks.FARMLAND);
	}

	@Override
	public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
		super.afterBreak(world, player, pos, Blocks.AIR.getDefaultState(), blockEntity, stack);
	}

	@Override
	public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
		return state.getFluidState().isEmpty();
	}

	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return type == NavigationType.AIR && !this.collidable || super.canPathfindThrough(state, world, pos, type);
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(MHGatheringType.fromStack(stack).getRarity().asText().formatted(Formatting.BOLD));
		final List<Text> resources = getAvailableResources(stack);
		if (resources == null || resources.isEmpty()) return;
		tooltip.add(new TranslatableText("block.monhun.possible_resources").formatted(Formatting.GRAY).formatted(Formatting.UNDERLINE));
		for (Text text : resources)
			tooltip.add(text.copy().formatted(Formatting.AQUA));
	}

	private List<Text> getAvailableResources(ItemStack stack) {
		if (!MHGatheringBlock.AVAILABLE_RESOURCES.containsKey(MHTopPlantBlock.class)) return Collections.emptyList();
		MHGatheringBlock.AvailableResources resources = MHGatheringBlock.AVAILABLE_RESOURCES.get(MHTopPlantBlock.class);
		return resources.get(MHGatheringType.fromStack(stack).asString());
	}

	@Override
	public String getType(ItemStack stack) {
		return MHGatheringType.fromStack(stack).getTranslationKey();
	}

	/*public static void placeAt(WorldAccess world, BlockState state, BlockPos pos, int flags) {
		BlockPos blockPos = pos.up();
		world.setBlockState(pos, state, flags);
		world.setBlockState(blockPos, MHBlocks.TOP_PLANT_BLOCK.getDefaultState(), flags);
	}*/
}
