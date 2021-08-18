package eu.asangarin.monhun.mixin.loot;

import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LootTable.class)
public interface MHLootTableAccessor {
	@Accessor
	LootPool[] getPools();
}
