package eu.asangarin.monhun.block;

import eu.asangarin.monhun.block.entity.MHItemBoxBlockEntity;
import eu.asangarin.monhun.block.entity.MHSupplyBoxBlockEntity;
import eu.asangarin.monhun.managers.MHItems;
import eu.asangarin.monhun.util.enums.MHRarity;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("deprecation")
public class MHInventoryBoxBlock extends BlockWithEntity implements BlockEntityProvider, Waterloggable {
	private static final VoxelShape SHAPE = Block.createCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);
	private static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	public static final DirectionProperty FACING = Properties.FACING;

	private final boolean isItemBox;

	public MHInventoryBoxBlock(boolean itemBox) {
		super(FabricBlockSettings.of(Material.WOOD).strength(2.5F).sounds(BlockSoundGroup.WOOD).nonOpaque());
		setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.EAST).with(WATERLOGGED, false));
		this.isItemBox = itemBox;
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (state.get(WATERLOGGED)) world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(FACING).add(WATERLOGGED);
	}

	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
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
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
		return getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite()).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient) {
			if(!isItemBox && player.isSneaking()) {
				BlockEntity blockEntity = world.getBlockEntity(pos);
				if(blockEntity instanceof MHSupplyBoxBlockEntity supplyBox) {
					supplyBox.setStack(0, new ItemStack(MHItems.MAP, 1));
					supplyBox.markDirty();
				}
			} else {
				NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);

				if (screenHandlerFactory != null) player.openHandledScreen(screenHandlerFactory);
			}
		}
		return ActionResult.SUCCESS;
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(MHRarity.RARE_4.asText().formatted(Formatting.BOLD));
		String description = new TranslatableText("block.monhun." + (isItemBox ? "item_box" : "supply_box") + ".desc").getString();
		for (String s : description.split("\\n"))
			tooltip.add(new LiteralText(s).formatted(Formatting.GRAY));
	}

	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return isItemBox ? new MHItemBoxBlockEntity(pos, state) : new MHSupplyBoxBlockEntity(pos, state);
	}
}
