package eu.asangarin.monhun.block;

import eu.asangarin.monhun.MonHun;
import eu.asangarin.monhun.util.interfaces.IColorProvider;
import eu.asangarin.monhun.util.interfaces.IDoubleBlock;
import eu.asangarin.monhun.util.interfaces.IGatheringSpot;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MHBlockItem extends BlockItem {
	public MHBlockItem(Block block) {
		super(block, new FabricItemSettings().group(MonHun.BLOCK_GROUP));
	}

	@Override
	public Text getName(ItemStack stack) {
		if (getBlock() instanceof IGatheringSpot gatheringSpot) {
			String type = gatheringSpot.getType(stack);
			if (type == null) return super.getName(stack);
			return new TranslatableText(this.getTranslationKey(stack), new TranslatableText(type));
		}
		return super.getName(stack);
	}

	public int getColor(ItemStack stack) {
		if (getBlock() instanceof IColorProvider provider) return provider.provideColor(stack);
		return 0xFFFFFF;
	}

	@Override
	protected boolean place(ItemPlacementContext context, BlockState state) {
		if (getBlock() instanceof IDoubleBlock) {
			World world = context.getWorld();
			BlockPos blockPos = context.getBlockPos().up();
			BlockState blockState = world.isWater(blockPos) ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState();
			world.setBlockState(blockPos, blockState, Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD | Block.FORCE_STATE);
		}
		return super.place(context, state);
	}
}
