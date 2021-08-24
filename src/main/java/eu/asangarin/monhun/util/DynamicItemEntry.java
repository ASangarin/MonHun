package eu.asangarin.monhun.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import eu.asangarin.monhun.item.MHDynamicItem;
import lombok.Getter;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.entry.LootPoolEntryType;
import net.minecraft.loot.entry.LootPoolEntryTypes;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.util.JsonHelper;

import java.util.function.Consumer;

public class DynamicItemEntry extends LeafEntry {
	@Getter
	final String item;

	DynamicItemEntry(String item, int i, int j, LootCondition[] lootConditions, LootFunction[] lootFunctions) {
		super(i, j, lootConditions, lootFunctions);
		this.item = item;
	}

	public LootPoolEntryType getType() {
		return LootPoolEntryTypes.ITEM;
	}

	public void generateLoot(Consumer<ItemStack> lootConsumer, LootContext context) {
		lootConsumer.accept(MHDynamicItem.withNBT(item));
	}

	public static class Serializer extends LeafEntry.Serializer<DynamicItemEntry> {
		public void addEntryFields(JsonObject jsonObject, DynamicItemEntry itemEntry, JsonSerializationContext jsonSerializationContext) {
			super.addEntryFields(jsonObject, itemEntry, jsonSerializationContext);
			jsonObject.addProperty("name", itemEntry.item);
		}

		protected DynamicItemEntry fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext, int i, int j, LootCondition[] lootConditions, LootFunction[] lootFunctions) {
			return new DynamicItemEntry(JsonHelper.getString(jsonObject, "name"), i, j, lootConditions, lootFunctions);
		}
	}
}
