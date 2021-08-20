package eu.asangarin.monhun.managers;

import eu.asangarin.monhun.effects.MHStatusEffect;
import eu.asangarin.monhun.effects.ParalysisEffect;
import eu.asangarin.monhun.effects.SleepEffect;
import eu.asangarin.monhun.util.ItemColors;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;

public class MHStatusEffects {
	public static final StatusEffect PARALYSIS = new ParalysisEffect();
	public static final StatusEffect SLEEP = new SleepEffect();

	public static final StatusEffect HOT_DRINK = new MHStatusEffect(StatusEffectType.BENEFICIAL, ItemColors.RED);
	public static final StatusEffect COOL_DRINK = new MHStatusEffect(StatusEffectType.BENEFICIAL, ItemColors.LIGHT_BLUE);

	public static final StatusEffect DASH_JUICE = new MHStatusEffect(StatusEffectType.BENEFICIAL, ItemColors.YELLOW);
	public static final StatusEffect FROZEN_HUNGER = new MHStatusEffect(StatusEffectType.HARMFUL, ItemColors.LIGHT_BLUE);
}
