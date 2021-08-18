package eu.asangarin.monhun.mixin.loot;

import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.entry.LootPoolEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LootPool.class)
public interface MHLootPoolAccessor {
	@Accessor
	LootPoolEntry[] getEntries();
}
