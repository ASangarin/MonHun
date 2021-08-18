package eu.asangarin.monhun.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffectType;

public class SleepEffect extends MHStatusEffect implements IPotionable {
	public SleepEffect() {
		super(StatusEffectType.HARMFUL, 6711039);
	}

	@Override
	public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		//if (!(entity instanceof PlayerEntity)) ToAComponents.TICKABLE.get(entity).setTickable(false);
		entity.sleep(entity.getBlockPos().add(0, 0, 0));
	}

	@Override
	public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		//if (!(entity instanceof PlayerEntity)) ToAComponents.TICKABLE.get(entity).setTickable(true);
		entity.wakeUp();
	}

	@Override
	public int defaultTime() {
		return 1200;
	}
}
