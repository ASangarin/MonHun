package eu.asangarin.monhun.monsters.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import eu.asangarin.monhun.util.enums.MHBreakablePart;
import lombok.AccessLevel;
import lombok.Getter;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class MHMonsterDropsData {
	private final MHDropDataList carve = new MHDropDataList();
	private final MHDropDataList tail = new MHDropDataList();
	private final MHDropDataList shiny = new MHDropDataList();
	private final MHDropDataList capture = new MHDropDataList();
	@Getter(value = AccessLevel.PRIVATE)
	private final Map<MHBreakablePart, MHDropDataList> breakables = new HashMap<>();

	public MHMonsterDropsData(JsonObject drops) {
		carve.addAll(drops.getAsJsonArray("carve"));
		tail.addAll(drops.getAsJsonArray("tail"));
		shiny.addAll(drops.getAsJsonArray("shiny"));
		capture.addAll(drops.getAsJsonArray("capture"));
		for (Map.Entry<String, JsonElement> entry : drops.getAsJsonObject("break").entrySet())
			breakables.put(MHBreakablePart.fromString(entry.getKey()), new MHDropDataList(entry.getValue().getAsJsonArray()));
	}

	public MHDropDataList getBreakableDrops(MHBreakablePart part) {
		return breakables.getOrDefault(part, new MHDropDataList());
	}

	public List<ItemStack> allItems() {
		List<ItemStack> list = new ArrayList<>();
		for (DropDataType type : DropDataType.values())
			list.addAll(allItems(type));
		return list;
	}

	public List<ItemStack> allItems(DropDataType type) {
		switch (type) {
			case CARVE -> {
				return carve.getAll();
			}
			case TAIL -> {
				return tail.getAll();
			}
			case SHINY -> {
				return shiny.getAll();
			}
			case CAPTURE -> {
				return capture.getAll();
			}
			case BREAKABLE -> {
				List<ItemStack> list = new ArrayList<>();
				for (MHDropDataList dropDataList : breakables.values())
					list.addAll(dropDataList.getAll());
				return list;
			}
		}

		return Collections.emptyList();
	}

	public enum DropDataType {
		CARVE, TAIL, SHINY, CAPTURE, BREAKABLE
	}
}
