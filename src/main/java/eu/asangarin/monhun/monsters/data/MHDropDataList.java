package eu.asangarin.monhun.monsters.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import eu.asangarin.monhun.managers.MHItems;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MHDropDataList extends ArrayList<MHDropData> {
	private final Random random = new Random();
	int totalChance = 0;

	public MHDropDataList() {}
	public MHDropDataList(JsonArray array) {
		addAll(array);
	}

	public void addAll(JsonArray array) {
		for(JsonElement element : array) {
			if(!element.isJsonObject()) continue;
			JsonObject json = element.getAsJsonObject();
			int amount = json.has("amount") ? json.get("amount").getAsInt() : 1;
			int chance = json.get("chance").getAsInt();
			add(new MHDropData(MHItems.getStackOf(json.get("item").getAsString(), amount), chance));
			totalChance += chance;
			if(totalChance > 100)
				throw new RuntimeException("Monster drop percentage can't exceed 100!");
		}
		Collections.sort(this);
	}

	public ItemStack getRandom() {
		double rand = random.nextDouble();
		for(MHDropData data : this)
			if(rand <= data.getChance())
				return data.getItem();
		return get(size() - 1).getItem();
	}

	public List<ItemStack> getAll() {
		List<ItemStack> list = new ArrayList<>();
		for(MHDropData data : this)
			list.add(data.getItem());
		return list;
	}
}
