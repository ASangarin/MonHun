package eu.asangarin.monhun.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.ArrayList;

public abstract class MHMonsterEntity extends PathAwareEntity implements IAnimatable {
	protected final AnimationFactory factory = new AnimationFactory(this);

	public MHMonsterEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public Iterable<ItemStack> getArmorItems() {
		return new ArrayList<>();
	}

	@Override
	public ItemStack getEquippedStack(EquipmentSlot slot) {
		return ItemStack.EMPTY;
	}

	@Override
	public void equipStack(EquipmentSlot slot, ItemStack stack) {
	}

	@Override
	public Arm getMainArm() {
		return Arm.RIGHT;
	}

	@Override
	public AnimationFactory getFactory() {
		return factory;
	}

	public static DefaultAttributeContainer.Builder createMonsterAttributes() {
		return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, 35.0D)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23000000417232513D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0D)
				.add(EntityAttributes.GENERIC_ARMOR, 2.0D);
	}
}
