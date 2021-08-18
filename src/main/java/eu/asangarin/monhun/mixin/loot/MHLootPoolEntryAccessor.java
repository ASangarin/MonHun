package eu.asangarin.monhun.mixin.loot;

import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.entry.LootPoolEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LootPoolEntry.class)
public interface MHLootPoolEntryAccessor {
	@Accessor
	LootCondition[] getConditions();
}
