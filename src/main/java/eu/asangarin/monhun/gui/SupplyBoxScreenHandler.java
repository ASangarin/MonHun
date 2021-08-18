package eu.asangarin.monhun.gui;

import eu.asangarin.monhun.managers.MHScreens;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class SupplyBoxScreenHandler extends ScreenHandler {
	private final Inventory inventory;

	public SupplyBoxScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, new SimpleInventory(54));
	}

	public SupplyBoxScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
		super(MHScreens.SUPPLY_BOX_SCREEN_HANDLER, syncId);
		checkSize(inventory, 54);
		this.inventory = inventory;
		inventory.onOpen(playerInventory.player);
		int i = 36;

		int n;
		int m;
		for(n = 0; n < 6; ++n)
			for(m = 0; m < 9; ++m)
				this.addSlot(new SupplySlot(inventory, m + n * 9, 8 + m * 18, 18 + n * 18));

		for(n = 0; n < 3; ++n)
			for(m = 0; m < 9; ++m)
				this.addSlot(new Slot(playerInventory, m + n * 9 + 9, 8 + m * 18, 103 + n * 18 + i));

		for(n = 0; n < 9; ++n)
			this.addSlot(new Slot(playerInventory, n, 8 + n * 18, 161 + i));
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return this.inventory.canPlayerUse(player);
	}

	@Override
	public ItemStack transferSlot(PlayerEntity player, int invSlot) {
		ItemStack newStack = ItemStack.EMPTY;
		Slot slot = this.slots.get(invSlot);
		if (slot.hasStack()) {
			ItemStack originalStack = slot.getStack();
			newStack = originalStack.copy();
			if (invSlot < this.inventory.size()) {
				if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true))
					return ItemStack.EMPTY;
			} else if (!this.insertItem(originalStack, 0, this.inventory.size(), false))
				return ItemStack.EMPTY;

			if (originalStack.isEmpty())
				slot.setStack(ItemStack.EMPTY);
			else
				slot.markDirty();
		}

		return newStack;
	}
}
