package eu.asangarin.monhun.util.interfaces;

import eu.asangarin.monhun.util.enums.MHGatheringType;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public interface IGatheringSpot {
	MHGatheringType getType(ItemStack stack);
}
