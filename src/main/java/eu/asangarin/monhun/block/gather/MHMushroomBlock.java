package eu.asangarin.monhun.block.gather;

import eu.asangarin.monhun.MonHun;
import eu.asangarin.monhun.block.entity.gather.MHAbstractGatheringBlockEntity;
import eu.asangarin.monhun.block.entity.gather.MHGatheringBlockEntity;
import eu.asangarin.monhun.managers.MHBlocks;
import eu.asangarin.monhun.util.enums.MHGatheringAmount;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

@SuppressWarnings("deprecation")
public class MHMushroomBlock extends MHGatheringBlock {
	private static final VoxelShape SHAPE = Block.createCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 8.0D, 13.0D);
	public static final Identifier LOOT = MonHun.i("mushroom_patch");
	public static final EnumProperty<MHGatheringAmount> AMOUNT = EnumProperty.of("amount", MHGatheringAmount.class);

	public MHMushroomBlock() {
		super(FabricBlockSettings.of(MHBlocks.RESOURCE).strength(1.0f).nonOpaque().noCollision().sounds(BlockSoundGroup.MOSS_BLOCK));
	}

	@Override
	protected BlockState getDefaultStateWith(BlockState state) {
		return state.with(AMOUNT, MHGatheringAmount.ONE);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		super.appendProperties(stateManager);
		stateManager.add(AMOUNT);
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (world.getBlockEntity(pos) instanceof MHGatheringBlockEntity gatherEntity)
			world.setBlockState(pos, state.with(AMOUNT, MHGatheringAmount.fromInteger(gatherEntity.getRemaining())));
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		return !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, neighborState,
				world, pos, neighborPos);
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		BlockState floor = world.getBlockState(pos.down());
		return floor.isIn(BlockTags.DIRT) || floor.isOf(Blocks.FARMLAND);
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
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		Vec3d vec3d = state.getModelOffset(world, pos);
		return SHAPE.offset(vec3d.x, vec3d.y, vec3d.z);
	}

	@Override
	public AbstractBlock.OffsetType getOffsetType() {
		return OffsetType.XZ;
	}

	@Override
	protected void playServerGatherEffects(World world, BlockPos pos) {
		world.playSound(null, pos, SoundEvents.BLOCK_SWEET_BERRY_BUSH_PICK_BERRIES, SoundCategory.BLOCKS, 0.5F,
				world.random.nextFloat() * 0.1F + 1.0F);
		if (world.getBlockEntity(pos) instanceof MHGatheringBlockEntity gatherEntity)
			world.setBlockState(pos, world.getBlockState(pos).with(AMOUNT, MHGatheringAmount.fromInteger(gatherEntity.getRemaining() - 1)));
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
	public double getShinyY(Random random) {
		return random.nextDouble() - 0.4d;
	}

	@Override
	public MHAbstractGatheringBlockEntity createGatheringBlockEntity(BlockPos pos, BlockState state) {
		return new MHGatheringBlockEntity(pos, state);
	}
}
