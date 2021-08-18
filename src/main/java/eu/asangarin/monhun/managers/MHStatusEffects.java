package eu.asangarin.monhun.managers;

import eu.asangarin.monhun.MonHun;
import eu.asangarin.monhun.effects.IPotionable;
import eu.asangarin.monhun.effects.MHStatusEffect;
import eu.asangarin.monhun.effects.ParalysisEffect;
import eu.asangarin.monhun.effects.SleepEffect;
import eu.asangarin.monhun.util.ItemColors;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.potion.Potion;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.lang.reflect.Field;

public class MHStatusEffects {
	public static final StatusEffect PARALYSIS = new ParalysisEffect();
	public static final StatusEffect SLEEP = new SleepEffect();

	public static final StatusEffect HOT_DRINK = new MHStatusEffect(StatusEffectType.BENEFICIAL, ItemColors.RED);
	public static final StatusEffect COOL_DRINK = new MHStatusEffect(StatusEffectType.BENEFICIAL, ItemColors.LIGHT_BLUE);

	public static final StatusEffect DASH_JUICE = new MHStatusEffect(StatusEffectType.BENEFICIAL, ItemColors.YELLOW);
	public static final StatusEffect FROZEN_HUNGER = new MHStatusEffect(StatusEffectType.HARMFUL, ItemColors.LIGHT_BLUE);

	public static void onInitialize() {
		for (Field f : MHStatusEffects.class.getDeclaredFields()) {
			try {
				if (!StatusEffect.class.isAssignableFrom(f.getType())) continue;
				StatusEffect effect = (StatusEffect) f.get(null);
				Identifier ident = MonHun.i(f.getName().toLowerCase());
				Registry.register(Registry.STATUS_EFFECT, ident, effect);
				if (effect instanceof IPotionable potionable) Registry.register(Registry.POTION, MonHun.i(f.getName().toLowerCase() + "_potion"),
						new Potion(new StatusEffectInstance(effect, potionable.defaultTime())));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
}
