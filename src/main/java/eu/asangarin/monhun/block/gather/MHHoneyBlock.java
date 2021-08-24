package eu.asangarin.monhun.block.gather;

import eu.asangarin.monhun.MonHun;
import eu.asangarin.monhun.block.entity.gather.MHAbstractGatheringBlockEntity;
import eu.asangarin.monhun.block.entity.gather.MHGatheringBlockEntity;
import eu.asangarin.monhun.managers.MHBlocks;
import eu.asangarin.monhun.util.enums.MHRarity;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.Random;

@SuppressWarnings("deprecation")
public class MHHoneyBlock extends MHGatheringBlock {
	private static final VoxelShape SHAPE = Block.createCuboidShape(5.5D, 0.0D, 5.5D, 10.5D, 28.0D, 10.5D);
	public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
	public static final BooleanProperty ROYAL = BooleanProperty.of("royal");
	public static final Identifier LOOT = MonHun.i("honey");

	public MHHoneyBlock() {
		super(FabricBlockSettings.of(MHBlocks.RESOURCE).dynamicBounds().strength(2.0f).nonOpaque().sounds(BlockSoundGroup.WOOD));
	}

	protected BlockState getDefaultStateWith(BlockState state) {
		return state.with(FACING, Direction.EAST).with(ROYAL, false);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		super.appendProperties(stateManager);
		stateManager.add(FACING);
		stateManager.add(ROYAL);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockState state = super.getPlacementState(ctx);
		if (state == null) state = getDefaultState();
		return state.with(FACING, ctx.getPlayerFacing().getOpposite().rotateYClockwise()).with(ROYAL, isRoyal(ctx.getStack()));
	}


	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		return !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, neighborState,
				world, pos, neighborPos);
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		return world.getBlockState(pos.down()).isFullCube(world, pos);
	}

	@Override
	public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
		return state.getFluidState().isEmpty();
	}

	@Override
	protected boolean canGather(ItemStack stack) {
		return true;
	}

	protected BlockState cycle(BlockState state) {
		return state.with(ROYAL, !state.get(ROYAL));
	}

	@Override
	protected void playServerGatherEffects(World world, BlockPos pos) {
		world.playSound(null, pos, SoundEvents.BLOCK_HONEY_BLOCK_HIT, SoundCategory.BLOCKS, 0.5F, world.random.nextFloat() * 0.1F + 1.0F);
	}

	@Override
	protected MHRarity getRarity(ItemStack stack) {
		return isRoyal(stack) ? MHRarity.RARE_4 : MHRarity.RARE_2;
	}

	@Override
	protected String getResourceKey(ItemStack stack) {
		return isRoyal(stack) ? "royal" : "normal";
	}

	@Override
	public String getType(ItemStack stack) {
		return isRoyal(stack) ? "block.monhun.honey.royal" : null;
	}

	public static boolean isRoyal(ItemStack stack) {
		NbtCompound nbt = stack.getNbt();
		if (nbt != null && nbt.contains("royal")) return nbt.getBoolean("royal");
		return false;
	}

	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
		stacks.add(new ItemStack(this));
		ItemStack stack = new ItemStack(this);
		NbtCompound nbt = new NbtCompound();
		nbt.putBoolean("royal", true);
		stack.setNbt(nbt);
		stacks.add(stack);
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		if (world.random.nextInt(16) == 0) {
			Vec3d vec3d = state.getModelOffset(world, pos).subtract((dirOffset(state.get(FACING)).multiply(0.7)));
			double x = vec3d.x + pos.getX();
			double z = vec3d.z + pos.getZ();
			world.addParticle(ParticleTypes.DRIPPING_HONEY, MathHelper.lerp(world.random.nextDouble(), x + 0.3d, x + 0.7d), pos.getY() + 1.1d,
					MathHelper.lerp(world.random.nextDouble(), z + 0.3d, z + 0.7d), 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	protected Identifier getLootTableIdent() {
		return LOOT;
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
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(FACING)));
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		Vec3d vec3d = state.getModelOffset(world, pos).add(dirOffset(state.get(FACING)));
		return SHAPE.offset(vec3d.x, vec3d.y, vec3d.z);
	}

	private Vec3d dirOffset(Direction dir) {
		return switch (dir) {
			case NORTH -> new Vec3d(0.28, 0, 0);
			case SOUTH -> new Vec3d(-0.28, 0, 0);
			case EAST -> new Vec3d(0, 0, 0.28);
			case WEST -> new Vec3d(0, 0, -0.28);
			default -> throw new IllegalStateException("Unexpected value: " + dir);
		};
	}

	@Override
	public double getShinyY(Random random) {
		return 1.2f + (random.nextDouble() - 0.4d);
	}

	@Override
	public MHAbstractGatheringBlockEntity createGatheringBlockEntity(BlockPos pos, BlockState state) {
		return new MHGatheringBlockEntity(pos, state);
	}
}
