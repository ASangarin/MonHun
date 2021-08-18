package eu.asangarin.monhun.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;

public class ItemBoxInventory implements Inventory {
	private final DefaultedList<ItemStack> items = DefaultedList.ofSize(54, ItemStack.EMPTY);

	@Override
	public int size() {
		return items.size();
	}

	@Override
	public boolean isEmpty() {
		for (int i = 0; i < size(); i++) {
			ItemStack stack = getStack(i);
			if (!stack.isEmpty()) return false;
		}
		return true;
	}

	@Override
	public ItemStack getStack(int slot) {
		return items.get(slot);
	}

	@Override
	public ItemStack removeStack(int slot, int count) {
		ItemStack result = Inventories.splitStack(items, slot, count);
		if (!result.isEmpty()) markDirty();
		return result;
	}

	@Override
	public ItemStack removeStack(int slot) {
		return Inventories.removeStack(items, slot);
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		items.set(slot, stack);
		if (stack.getCount() > getMaxCountPerStack()) stack.setCount(getMaxCountPerStack());
	}

	@Override
	public void markDirty() {
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return true;
	}

	@Override
	public void clear() {
		items.clear();
	}

	public void readFromNbt(NbtCompound tag) {
		Inventories.readNbt(tag, items);
	}

	public void writeToNbt(NbtCompound tag) {
		Inventories.writeNbt(tag, items);
	}
}
