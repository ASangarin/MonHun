package eu.asangarin.monhun.gui;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class SupplySlot extends Slot {
	public SupplySlot(Inventory inventory, int index, int x, int y) {
		super(inventory, index, x, y);
	}

	public boolean canInsert(ItemStack stack) {
		return false;
	}
}
