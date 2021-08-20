package eu.asangarin.monhun.managers;

import eu.asangarin.monhun.MonHun;
import eu.asangarin.monhun.entity.MHMonsterEntity;
import eu.asangarin.monhun.entity.MHThrownItemEntity;
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


	public static void registerAttributes() {
		FabricDefaultAttributeRegistry.register(RATHALOS, MHMonsterEntity.createMonsterAttributes());
	}
}
