package eu.asangarin.monhun.item.supply;

import eu.asangarin.monhun.item.MHBaseItem;
import eu.asangarin.monhun.util.enums.MHItemTexture;
import eu.asangarin.monhun.util.enums.MHRarity;
import net.minecraft.item.ItemGroup;

public class MHBaseSupplyItem extends MHBaseItem implements MHBaseItem.ISupplyItem {
	public MHBaseSupplyItem(MHItemTexture texture, int color, MHRarity rarity, int maxCount, ItemGroup group) {
		super(texture, color, rarity, maxCount, group);
	}
}
