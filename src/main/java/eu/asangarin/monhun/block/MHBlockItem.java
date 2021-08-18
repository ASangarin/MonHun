package eu.asangarin.monhun.block;

import eu.asangarin.monhun.MonHun;
import eu.asangarin.monhun.util.interfaces.IColorProvider;
import eu.asangarin.monhun.util.interfaces.IGatheringSpot;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class MHBlockItem extends BlockItem {
	public MHBlockItem(Block block) {
		super(block, new FabricItemSettings().group(MonHun.BLOCK_GROUP));
	}

	@Override
	public Text getName(ItemStack stack) {
		if (getBlock() instanceof IGatheringSpot gatheringSpot)
			return new TranslatableText(this.getTranslationKey(stack), new TranslatableText(gatheringSpot.getType(stack).getTranslationKey()));
		return super.getName(stack);
	}

	public int getColor(ItemStack stack) {
		if (getBlock() instanceof IColorProvider provider) return provider.provideColor(stack);
		return 0xFFFFFF;
	}
}
