package eu.asangarin.monhun.managers;

import eu.asangarin.monhun.MonHun;
import eu.asangarin.monhun.entity.MHMonsterEntity;
import eu.asangarin.monhun.entity.MHNPCEntity;
import eu.asangarin.monhun.entity.MHThrownItemEntity;
import eu.asangarin.monhun.entity.npc.MHBlacksmithEntity;
import eu.asangarin.monhun.entity.npc.MHProvisionerEntity;
import eu.asangarin.monhun.temp.TempEntityRathalos;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.Registry;

public class MHEntities {
	public static final EntityType<MHThrownItemEntity> THROWN_ITEM = Registry.register(Registry.ENTITY_TYPE, MonHun.i("thrown_item"),
			FabricEntityTypeBuilder.create(SpawnGroup.MISC, MHThrownItemEntity::new).dimensions(EntityDimensions.fixed(0.25f, 0.25f))
					.trackRangeChunks(4).trackedUpdateRate(10).build());

	public static final EntityType<TempEntityRathalos> RATHALOS = Registry.register(Registry.ENTITY_TYPE, MonHun.i("rathalos"),
			FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, TempEntityRathalos::new).trackRangeChunks(10)
					.dimensions(EntityDimensions.fixed(1f, 2f)).build());

	public static final EntityType<MHBlacksmithEntity> BLACKSMITH = Registry.register(Registry.ENTITY_TYPE, MonHun.i("npc_blacksmith"),
			FabricEntityTypeBuilder.create(SpawnGroup.MISC, MHBlacksmithEntity::new).trackRangeChunks(10)
					.dimensions(EntityDimensions.fixed(0.6F, 1.8F)).build());

	public static final EntityType<MHProvisionerEntity> PROVISIONER = Registry.register(Registry.ENTITY_TYPE, MonHun.i("npc_provisioner"),
			FabricEntityTypeBuilder.create(SpawnGroup.MISC, MHProvisionerEntity::new).trackRangeChunks(10)
					.dimensions(EntityDimensions.fixed(0.6F, 1.8F)).build());


	public static void registerAttributes() {
		FabricDefaultAttributeRegistry.register(RATHALOS, MHMonsterEntity.createMonsterAttributes());
		FabricDefaultAttributeRegistry.register(BLACKSMITH, MHNPCEntity.createNPCAttributes());
		FabricDefaultAttributeRegistry.register(PROVISIONER, MHNPCEntity.createNPCAttributes());
	}
}
