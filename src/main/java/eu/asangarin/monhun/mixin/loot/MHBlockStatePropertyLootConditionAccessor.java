package eu.asangarin.monhun.mixin.loot;

import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.predicate.StatePredicate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockStatePropertyLootCondition.class)
public interface MHBlockStatePropertyLootConditionAccessor {
	@Accessor
	StatePredicate getProperties();
}
