package eu.asangarin.monhun.components;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import eu.asangarin.monhun.MonHun;
import net.minecraft.entity.Entity;

public class MHComponents implements EntityComponentInitializer {
	public static final ComponentKey<ItemBoxComponent> ITEM_BOX = ComponentRegistry
			.getOrCreate(MonHun.i("player_item_box"), ItemBoxComponent.class);
	public static final ComponentKey<MoneyComponent> MONEY = ComponentRegistry
			.getOrCreate(MonHun.i("player_money"), MoneyComponent.class);
	public static final ComponentKey<EntityTickableComponent> TICKABLE = ComponentRegistry
			.getOrCreate(MonHun.i("entity_tickable"), EntityTickableComponent.class);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerForPlayers(ITEM_BOX, player -> new ItemBoxComponent(), RespawnCopyStrategy.ALWAYS_COPY);
		registry.registerForPlayers(MONEY, player -> new MoneyComponent(), RespawnCopyStrategy.ALWAYS_COPY);
		registry.registerFor(Entity.class, TICKABLE, e -> new EntityTickableComponent());
	}
}
