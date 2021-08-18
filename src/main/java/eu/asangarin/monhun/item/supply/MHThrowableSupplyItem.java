package eu.asangarin.monhun.item.supply;

import eu.asangarin.monhun.item.MHBaseItem;
import eu.asangarin.monhun.item.MHThrowableItem;
import eu.asangarin.monhun.util.enums.MHItemTexture;
import eu.asangarin.monhun.util.enums.MHRarity;
import eu.asangarin.monhun.util.enums.MHThrowables;
import net.minecraft.item.ItemGroup;

public class MHThrowableSupplyItem extends MHThrowableItem implements MHBaseItem.ISupplyItem {
	public MHThrowableSupplyItem(MHItemTexture texture, int color, MHRarity rarity, int maxCount, ItemGroup group, MHThrowables thrown) {
		super(texture, color, rarity, maxCount, group, thrown);
	}
}
