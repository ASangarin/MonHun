package eu.asangarin.monhun.entity;

import eu.asangarin.monhun.item.MHThrownItem;
import eu.asangarin.monhun.managers.MHItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class MHThrownItemEntity extends ThrownItemEntity {
	public MHThrownItemEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
		super(entityType, world);
	}

	protected Item getDefaultItem() {
		return MHItems.STONE;
	}

	public void handleStatus(byte status) {
		if (status == 3) {
			ParticleEffect particleEffect = new ItemStackParticleEffect(ParticleTypes.ITEM, getItem());

			for (int i = 0; i < 8; ++i)
				this.world.addParticle(particleEffect, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
		}
	}

	protected void onEntityHit(EntityHitResult entityHitResult) {
		super.onEntityHit(entityHitResult);
		if (!getItem().isEmpty() && getItem().getItem() instanceof MHThrownItem throwable)
			throwable.action(this, entityHitResult.getEntity(), getOwner());
	}

	protected void onCollision(HitResult hitResult) {
		super.onCollision(hitResult);
		if (!this.world.isClient) {
			this.world.sendEntityStatus(this, (byte) 3);
			this.discard();
		}
	}
}
