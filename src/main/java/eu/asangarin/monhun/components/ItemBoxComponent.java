package eu.asangarin.monhun.components;

import dev.onyxstudios.cca.api.v3.component.Component;
import eu.asangarin.monhun.gui.ItemBoxInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ItemBoxComponent implements Component {
	private final TreeMap<Integer, ItemBoxInventory> inventories = new TreeMap<>();

	@Override
	public void readFromNbt(NbtCompound tag) {
		for (NbtElement element : tag.getList("inventories", NbtElement.COMPOUND_TYPE)) {
			if (!(element instanceof NbtCompound compound)) return;
			ItemBoxInventory inventory = new ItemBoxInventory();
			inventory.readFromNbt(compound);
			inventories.put(compound.getInt("page"), inventory);
		}
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		NbtList list = new NbtList();
		for(Map.Entry<Integer, ItemBoxInventory> entry : inventories.entrySet()) {
			NbtCompound compound = new NbtCompound();
			compound.putInt("page", entry.getKey());
			entry.getValue().writeToNbt(compound);
			list.add(compound);
		}
		tag.put("inventories", list);
	}

	public Collection<ItemBoxInventory> values() {
		return inventories.values();
	}

	public Inventory getOrDefault(int page) {
		return inventories.getOrDefault(page, new ItemBoxInventory());
	}

	public void put(int page, ItemBoxInventory inventory) {
		inventories.put(page, inventory);
		inventories.values().removeIf(Inventory::isEmpty);
		reset();
	}

	public void reset() {
		Map<Integer, ItemBoxInventory> map = new HashMap<>(inventories);
		inventories.clear();
		int i = 0;
		for (ItemBoxInventory inv : map.values()) {
			inventories.put(i, inv);
			i++;
		}
	}
}
