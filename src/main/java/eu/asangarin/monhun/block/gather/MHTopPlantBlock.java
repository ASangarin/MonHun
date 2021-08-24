package eu.asangarin.monhun.block.gather;

import eu.asangarin.monhun.MonHun;
import eu.asangarin.monhun.block.entity.gather.MHAbstractGatheringBlockEntity;
import eu.asangarin.monhun.block.entity.gather.MHGatheringBlockEntity;
import eu.asangarin.monhun.managers.MHBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

@SuppressWarnings("deprecation")
public class MHTopPlantBlock extends MHColorGatheringBlock {
	public static final Identifier LOOT = MonHun.i("plant");

	public MHTopPlantBlock() {
		super(FabricBlockSettings.of(MHBlocks.RESOURCE).strength(1.0f).nonOpaque().noCollision().sounds(BlockSoundGroup.MOSS_BLOCK));
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (direction.getAxis() == Direction.Axis.Y && !(direction == Direction.UP) && (!(neighborState.getBlock() instanceof MHBottomPlantBlock))) {
			return Blocks.AIR.getDefaultState();
		} else {
			return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
		}
	}

	@Override
	protected BlockState getDefaultStateWith(BlockState state) {
		return state;
	}

	@Override
	protected boolean canGather(ItemStack stack) {
		return true;
	}

	@Override
	protected Identifier getLootTableIdent() {
		return LOOT;
	}

	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
	}

	@Override
	protected void playServerGatherEffects(World world, BlockPos pos) {
		world.playSound(null, pos, SoundEvents.BLOCK_SWEET_BERRY_BUSH_PICK_BERRIES, SoundCategory.BLOCKS, 0.5F,
				world.random.nextFloat() * 0.1F + 1.0F);
	}

	@Override
	public double getShinyY(Random random) {
		return random.nextDouble() - 0.4d;
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
	public AbstractBlock.OffsetType getOffsetType() {
		return AbstractBlock.OffsetType.XZ;
	}

	@Override
	public long getRenderingSeed(BlockState state, BlockPos pos) {
		return MathHelper.hashCode(pos.getX(), pos.down().getY(), pos.getZ());
	}

	@Override
	public MHAbstractGatheringBlockEntity createGatheringBlockEntity(BlockPos pos, BlockState state) {
		return new MHGatheringBlockEntity(pos, state);
	}
}
