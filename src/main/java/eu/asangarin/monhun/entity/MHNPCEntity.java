package eu.asangarin.monhun.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.Npc;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class MHNPCEntity extends PassiveEntity implements Npc {
	protected MHNPCEntity(EntityType<? extends PassiveEntity> entityType, World world) {
		super(entityType, world);
	}

	@Nullable
	@Override
	public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
		return null;
	}
}
