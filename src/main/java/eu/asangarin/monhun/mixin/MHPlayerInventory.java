package eu.asangarin.monhun.mixin;

import eu.asangarin.monhun.item.MHCharmItem;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerInventory.class)
public class MHPlayerInventory {
	@Shadow
	@Final
	public PlayerEntity player;
	@Shadow
	@Final
	public DefaultedList<ItemStack> main;

	@Inject(method = "updateItems", at = @At("TAIL"))
	private void updateInventory(CallbackInfo ci) {
		boolean hasPowercharm = false;
		boolean hasArmorcharm = false;
		boolean hasPowertalon = false;
		boolean hasArmortalon = false;

		for (int i = 0; i < PlayerInventory.getHotbarSize(); ++i) {
			ItemStack stack = main.get(i);
			if (!stack.isEmpty() && stack.getItem() instanceof MHCharmItem charm) {
				if (!hasPowercharm) hasPowercharm = charm.isPowerCharm();
				if (!hasArmorcharm) hasArmorcharm = charm.isArmorCharm();
				if (!hasPowertalon) hasPowertalon = charm.isPowerTalon();
				if (!hasArmortalon) hasArmortalon = charm.isArmorTalon();
			}
		}

		if (hasPowercharm || hasPowertalon) player.addStatusEffect(
				new StatusEffectInstance(StatusEffects.STRENGTH, 1, hasPowercharm && hasPowertalon ? 1 : 0, false, false, false));
		if (hasArmorcharm || hasArmortalon) player.addStatusEffect(
				new StatusEffectInstance(StatusEffects.RESISTANCE, 1, hasArmorcharm && hasArmortalon ? 1 : 0, false, false, false));
	}
}
