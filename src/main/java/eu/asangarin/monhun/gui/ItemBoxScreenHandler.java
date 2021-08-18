package eu.asangarin.monhun.gui;

import eu.asangarin.monhun.components.MHComponents;
import eu.asangarin.monhun.managers.MHScreens;
import lombok.Getter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class ItemBoxScreenHandler extends ScreenHandler {
	@Getter
	private int page = 0;
	private final Inventory cachedInventory = new ItemBoxInventory();

	public ItemBoxScreenHandler(int syncId, PlayerInventory playerInventory) {
		super(MHScreens.ITEM_BOX_SCREEN_HANDLER, syncId);

		for (Inventory inventory : MHComponents.ITEM_BOX.get(playerInventory.player).values())
			checkSize(inventory, 54);
		int n;
		int m;
		for (n = 0; n < 6; ++n)
			for (m = 0; m < 9; ++m)
				this.addSlot(new MHSlot(cachedInventory, m + n * 9, 8 + m * 18, 18 + n * 18));

		for (n = 0; n < 3; ++n)
			for (m = 0; m < 9; ++m)
				this.addSlot(new Slot(playerInventory, m + n * 9 + 9, 8 + m * 18, 103 + n * 18 + 36));

		for (n = 0; n < 9; ++n)
			this.addSlot(new Slot(playerInventory, n, 8 + n * 18, 161 + 36));
		refresh(0, playerInventory.player);
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return cachedInventory.canPlayerUse(player);
	}

	@Override
	public boolean onButtonClick(PlayerEntity player, int id) {
		if (id == 1) {
			refresh(page + 1, player);
			return true;
		} else if (page > 0) {
			refresh(page - 1, player);
			return true;
		}
		return false;
	}

	public void refresh(int newPage, PlayerEntity player) {
		if (!cachedInventory.isEmpty()) {
			MHComponents.ITEM_BOX.get(player).put(page, createCopy());
		}

		page = newPage;
		cachedInventory.clear();
		Inventory inventory = getInventory(player);
		for (int i = 0; i < 54; i++)
			cachedInventory.setStack(i, inventory.getStack(i));
		cachedInventory.markDirty();
	}

	private ItemBoxInventory createCopy() {
		ItemBoxInventory copy = new ItemBoxInventory();
		for (int i = 0; i < 54; i++)
			copy.setStack(i, cachedInventory.getStack(i).copy());
		return copy;
	}

	public Inventory getInventory(PlayerEntity player) {
		return MHComponents.ITEM_BOX.get(player).getOrDefault(page);
	}

	public boolean hasItems() {
		return !cachedInventory.isEmpty();
	}

	@Override
	public void close(PlayerEntity player) {
		MHComponents.ITEM_BOX.get(player).put(page, createCopy());
	}

	@Override
	public ItemStack transferSlot(PlayerEntity player, int invSlot) {
		ItemStack newStack = ItemStack.EMPTY;
		Slot slot = this.slots.get(invSlot);
		if (slot.hasStack()) {
			ItemStack originalStack = slot.getStack();
			newStack = originalStack.copy();
			if (invSlot < cachedInventory.size()) {
				if (!this.insertItem(originalStack, cachedInventory.size(), this.slots.size(), true)) return ItemStack.EMPTY;
			} else if (!this.insertItem(originalStack, 0, cachedInventory.size(), false)) return ItemStack.EMPTY;

			if (originalStack.isEmpty()) slot.setStack(ItemStack.EMPTY);
			else slot.markDirty();
		}

		return newStack;
	}
}
