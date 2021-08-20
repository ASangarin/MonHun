package eu.asangarin.monhun.block.gather;

import eu.asangarin.monhun.MonHun;
import eu.asangarin.monhun.block.entity.gather.MHAbstractGatheringBlockEntity;
import eu.asangarin.monhun.block.entity.gather.MHBugBlockEntity;
import eu.asangarin.monhun.item.MHToolItem;
import eu.asangarin.monhun.managers.MHBlocks;
import eu.asangarin.monhun.managers.MHSounds;
import eu.asangarin.monhun.util.enums.MHGatheringType;
import eu.asangarin.monhun.util.interfaces.IColorProvider;
import eu.asangarin.monhun.util.interfaces.INoBox;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class MHBugBlock extends MHGatheringBlock implements INoBox, IColorProvider {
	private static final VoxelShape SHAPE = Block.createCuboidShape(0.0D, 4.0D, 0.0D, 16.0D, 12.0D, 16.0D);
	public static final Identifier LOOT = MonHun.i("flying_bug");
	private static final BlockSoundGroup BUG_SOUNDS = new BlockSoundGroup(1.0F, 1.0F, MHSounds.BUTTERFLY_PLACE, MHSounds.SILENCE,
			MHSounds.BUTTERFLY_DEATH, MHSounds.SILENCE, MHSounds.SILENCE);

	public MHBugBlock() {
		super(FabricBlockSettings.of(MHBlocks.RESOURCE).strength(-1.0F, 0.2F).noCollision().sounds(BUG_SOUNDS));
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (world.getBlockEntity(pos) instanceof MHBugBlockEntity bugEntity) bugEntity.cleanAnimation();
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
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

	@Override
	protected BlockState getDefaultStateWith(BlockState state) {
		return state;
	}

	@Override
	protected void tryBreak(ItemStack stack, World world, PlayerEntity player) {
		if (stack.getItem() instanceof MHToolItem tool) tool.tryBreak(stack, world, player);
	}

	@Override
	protected boolean canGather(ItemStack stack) {
		return stack.getItem() instanceof MHToolItem tool && tool.isBugNet();
	}

	@Override
	protected void playServerGatherEffects(World world, BlockPos pos) {
		world.playSound(null, pos, SoundEvents.ENTITY_BAT_TAKEOFF, SoundCategory.PLAYERS, 0.8F, world.random.nextFloat() * 0.1F + 1.1F);
	}

	@Override
	protected Identifier getLootTableIdent() {
		return LOOT;
	}

	@Override
	public int provideColor(ItemStack stack) {
		return MHGatheringType.fromStack(stack).getColor();
	}

	@Override
	public MHAbstractGatheringBlockEntity createGatheringBlockEntity(BlockPos pos, BlockState state) {
		return new MHBugBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World abstractWorld, BlockState blockState, BlockEntityType<T> type) {
		return type.supports(blockState) ? (world, pos, state, be) -> {
			MHGatheringBlock.tick(be);
			MHBugBlockEntity.tick((MHBugBlockEntity) be);
		} : null;
	}
}
