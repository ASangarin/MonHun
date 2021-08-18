package eu.asangarin.monhun.temp;

import eu.asangarin.monhun.entity.MHMonsterEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class TempEntityRathalos extends MHMonsterEntity {
	private static final AnimationBuilder IDLE_ANIMATION = new AnimationBuilder().addAnimation("animation.rathalos.idle", true);
	private static final AnimationBuilder IDLE_FLYING_ANIMATION = new AnimationBuilder().addAnimation("animation.rathalos.idle_flying", true);
	private static final AnimationBuilder HEAD_LOOK_ANIMATION = new AnimationBuilder().addAnimation("animation.rathalos.head_look", true);
	private static final AnimationBuilder PREPARE_HEAD_ANIMATION = new AnimationBuilder().addAnimation("animation.rathalos.prepare_head", true);
	private static final AnimationBuilder WALKING_ANIMATION = new AnimationBuilder().addAnimation("animation.rathalos.walking", true);
	private static final AnimationBuilder WING_FLAP_ANIMATION = new AnimationBuilder().addAnimation("animation.rathalos.wing_flap", true);
	private static final AnimationBuilder HEADYAW_ANIMATION = new AnimationBuilder().addAnimation("animation.rathalos.headyaw", true);
	private static final AnimationBuilder BITE_ANIMATION = new AnimationBuilder().addAnimation("animation.rathalos.bite", false);

	public TempEntityRathalos(EntityType<? extends MHMonsterEntity> entityType, World world) {
		super(entityType, world);
	}

	protected void initGoals() {
		this.goalSelector.add(2, new MeleeAttackGoal(this, 2.0D, false));
		this.goalSelector.add(3, new WanderAroundFarGoal(this, 1.0D));
		this.goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.add(4, new LookAroundGoal(this));
		this.targetSelector.add(1, new RevengeGoal(this));
		this.targetSelector.add(2, new FollowTargetGoal<>(this, PlayerEntity.class, true));
		this.targetSelector.add(3, new FollowTargetGoal<>(this, PassiveEntity.class, true));
	}

	private <E extends IAnimatable> PlayState extraAnimationPredicate(AnimationEvent<E> event) {
		event.getController().setAnimation(event.isMoving() ? WALKING_ANIMATION : IDLE_ANIMATION);
		return PlayState.CONTINUE;
	}

	private <E extends IAnimatable> PlayState actionAnimationPredicate(AnimationEvent<E> event) {
		event.getController().setAnimation(IDLE_ANIMATION);
		return PlayState.CONTINUE;
	}

	private <E extends IAnimatable> PlayState idleAnimationPredicate(AnimationEvent<E> event) {
		event.getController().setAnimation(IDLE_ANIMATION);
		return PlayState.CONTINUE;
	}

	/*@Override
	public ActionResult interactMob(PlayerEntity player, Hand hand) {
		this.setBodyYaw(bodyYaw + 3f);
		return ActionResult.SUCCESS;
	}*/

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController<>(this, "idle-controller", 0, this::idleAnimationPredicate));
		data.addAnimationController(new AnimationController<>(this, "action-controller", 0, this::actionAnimationPredicate));
		data.addAnimationController(new AnimationController<>(this, "extra-controller", 0, this::extraAnimationPredicate));
	}
}
