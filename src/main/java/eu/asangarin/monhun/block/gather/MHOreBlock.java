package eu.asangarin.monhun.block.gather;

import eu.asangarin.monhun.MonHun;
import eu.asangarin.monhun.block.entity.gather.MHAbstractGatheringBlockEntity;
import eu.asangarin.monhun.block.entity.gather.MHGatheringBlockEntity;
import eu.asangarin.monhun.item.MHPickaxeItem;
import eu.asangarin.monhun.item.MHToolItem;
import eu.asangarin.monhun.managers.MHBlocks;
import eu.asangarin.monhun.managers.MHSounds;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

@SuppressWarnings("deprecation")
public class MHOreBlock extends MHGatheringBlock {
	private static final VoxelShape SHAPE = Block.createCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D);
	public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
	public static final Identifier LOOT = MonHun.i("mining_outcrop");

	public MHOreBlock() {
		super(FabricBlockSettings.of(MHBlocks.RESOURCE).dynamicBounds().strength(12.0f).nonOpaque().sounds(BlockSoundGroup.DEEPSLATE_TILES));
	}

	protected BlockState getDefaultStateWith(BlockState state) {
		return state.with(FACING, Direction.EAST);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		super.appendProperties(stateManager);
		stateManager.add(FACING);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockState state = super.getPlacementState(ctx);
		if (state == null) state = getDefaultState();
		return state.with(FACING, ctx.getPlayerFacing().getOpposite());
	}

	@Override
	protected void tryBreak(ItemStack stack, World world, PlayerEntity player) {
		if (stack.getItem() instanceof MHToolItem tool) tool.tryBreak(stack, world, player);
	}

	@Override
	protected boolean canGather(ItemStack stack) {
		return stack.getItem() instanceof MHPickaxeItem;
	}

	@Override
	protected void playClientGatherEffects(World world, BlockPos pos) {
		for (int i = 0; i < 16; ++i) {
			double d = world.random.nextFloat() * 2.0F - 1.0F;
			double e = world.random.nextFloat() * 2.0F - 1.0F;
			double f = world.random.nextFloat() * 2.0F - 1.0F;
			if (!(d * d + e * e + f * f > 1.0D)) {
				world.addParticle(ParticleTypes.CRIT, false, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, d, e + 0.2D, f);
			}
		}
	}

	@Override
	protected void playServerGatherEffects(World world, BlockPos pos) {
		world.playSound(null, pos, MHSounds.MINE_ORE, SoundCategory.BLOCKS, 0.5F, world.random.nextFloat() * 0.1F + 1.0F);
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
		Vec3d vec3d = state.getModelOffset(world, pos);
		return SHAPE.offset(vec3d.x, vec3d.y, vec3d.z);
	}

	@Override
	public MHAbstractGatheringBlockEntity createGatheringBlockEntity(BlockPos pos, BlockState state) {
		return new MHGatheringBlockEntity(pos, state);
	}
}
