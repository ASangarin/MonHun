package eu.asangarin.monhun.gui;

import eu.asangarin.monhun.item.MHBaseItem;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class MHSlot extends Slot {
	public MHSlot(Inventory inventory, int index, int x, int y) {
		super(inventory, index, x, y);
	}

	public boolean canInsert(ItemStack stack) {
		return stack.getItem() instanceof MHBaseItem;
	}
}
