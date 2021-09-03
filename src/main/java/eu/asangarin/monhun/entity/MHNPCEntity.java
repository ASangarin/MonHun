package eu.asangarin.monhun.entity;

import eu.asangarin.monhun.util.enums.MHNPCType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Npc;
import net.minecraft.entity.ai.goal.LongDoorInteractGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class MHNPCEntity extends PassiveEntity implements Npc {
	public MHNPCEntity(EntityType<? extends PassiveEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	protected void initGoals() {
		this.goalSelector.add(1, new LookAtEntityGoal(this, PlayerEntity.class, 5f, 1.1f));
		this.goalSelector.add(2, new LongDoorInteractGoal(this, true));
		this.goalSelector.add(3, new LookAroundGoal(this));
	}

	@Nullable
	@Override
	public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
		return null;
	}

	public static DefaultAttributeContainer.Builder createNPCAttributes() {
		return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.50D)
				.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48.00D);
	}

	@Override
	public Text getDefaultName() {
		return new TranslatableText(getNPCType().getTranslationKey());
	}

	public abstract MHNPCType getNPCType();

	public abstract ItemStack getHeldItem();
}
