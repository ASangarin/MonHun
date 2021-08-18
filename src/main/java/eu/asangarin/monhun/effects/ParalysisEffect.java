package eu.asangarin.monhun.effects;

import eu.asangarin.monhun.managers.MHSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffectType;

public class ParalysisEffect extends MHStatusEffect implements IPotionable {
	private int perTick = 1;

	public ParalysisEffect() {
		super(StatusEffectType.HARMFUL, 16704002);
	}

	@Override
	public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		/*if (entity instanceof ServerPlayerEntity)
			ToANetworking.sendSoundHolderPacket((ServerPlayerEntity) entity, ToASoundHolders.PARALYSIS_START);
		else ToAComponents.TICKABLE.get(entity).setTickable(false);*/
	}

	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		perTick++;
		if (perTick % 10 == 0) {
			entity.playSound(MHSounds.PARALYSIS_LOOP, 0.5f, 1.0f);
			perTick = 1;
		}
	}

	@Override
	public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		/*if (entity instanceof ServerPlayerEntity) ToANetworking.sendSoundHolderPacket((ServerPlayerEntity) entity, ToASoundHolders.PARALYSIS_END);
		else ToAComponents.TICKABLE.get(entity).setTickable(true);*/
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}

	@Override
	public int defaultTime() {
		return 200;
	}
}
