package eu.asangarin.monhun.client;

import eu.asangarin.monhun.MonHun;
import eu.asangarin.monhun.client.dynamic.MHItemDisplay;
import eu.asangarin.monhun.client.dynamic.MHItemDisplayManager;
import eu.asangarin.monhun.client.render.MHBugBlockEntityRenderer;
import eu.asangarin.monhun.client.render.MHItemModelRenderer;
import eu.asangarin.monhun.gui.BoxScreen;
import eu.asangarin.monhun.item.MHBaseItem;
import eu.asangarin.monhun.item.MHBaseModelItem;
import eu.asangarin.monhun.item.MHSpawnEggItem;
import eu.asangarin.monhun.managers.MHBlocks;
import eu.asangarin.monhun.managers.MHEntities;
import eu.asangarin.monhun.managers.MHItems;
import eu.asangarin.monhun.managers.MHScreens;
import eu.asangarin.monhun.managers.MHWeapons;
import eu.asangarin.monhun.network.MHNetwork;
import eu.asangarin.monhun.temp.TempRathalosRenderer;
import io.github.ennuil.libzoomer.api.ZoomInstance;
import io.github.ennuil.libzoomer.api.ZoomRegistry;
import io.github.ennuil.libzoomer.api.modifiers.SpyglassMouseModifier;
import io.github.ennuil.libzoomer.api.overlays.SpyglassZoomOverlay;
import io.github.ennuil.libzoomer.api.transitions.SmoothTransitionMode;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

@Environment(EnvType.CLIENT)
public class MonHunClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		MHModelManager.register();
		MHNetwork.onClientInitialize();

		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
			@Override
			public Identifier getFabricId() {
				return MonHun.i("mh_items");
			}

			@Override
			public void reload(ResourceManager manager) {
				MHItemDisplayManager.clear();
				for (Identifier id : manager.findResources("mh_items", path -> path.endsWith(".json"))) {
					try (InputStream stream = manager.getResource(id).getInputStream()) {
						Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
						MHItemDisplayManager.add(id.toString().replace("mh_items/", "").replace(".json", ""),
								MonHun.GSON.fromJson(reader, MHItemDisplay.class).cached());
					} catch (Exception e) {
						MonHun.log("Error occurred while loading item display json " + id.toString(), e);
					}
				}
			}
		});

		for (Field f : MHItems.class.getDeclaredFields()) {
			try {
				if (!MHBaseItem.class.isAssignableFrom(f.getType())) {
					if (MHSpawnEggItem.class.isAssignableFrom(f.getType()))
						MHModelManager.registerEgg("item/" + f.getName().toLowerCase(), ((MHSpawnEggItem) f.get(null)).getMonsterClass());
					continue;
				}
				registerClientItem((MHBaseItem) f.get(null), f.getName().toLowerCase());
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		for (Field f : MHWeapons.class.getDeclaredFields()) {
			try {
				if (!MHBaseItem.class.isAssignableFrom(f.getType())) continue;
				registerClientItem((MHBaseItem) f.get(null), f.getName().toLowerCase());
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		ZoomInstance binocularInstance = ZoomRegistry.registerInstance(
				new ZoomInstance(MonHun.i("binocular_zoom"), 10.0F, new SmoothTransitionMode(), new SpyglassMouseModifier(),
						new SpyglassZoomOverlay(MonHun.i("textures/misc/binoculars.png"))));
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player == null) return;
			binocularInstance.setZoom(
					client.options.getPerspective().isFirstPerson() && (client.player.isUsingItem() && client.player.getActiveItem()
							.isOf(MHItems.BINOCULARS)));
		});

		BlockRenderLayerMap.INSTANCE.putBlock(MHBlocks.TOP_PLANT_BLOCK, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(MHBlocks.BOTTOM_PLANT_BLOCK, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(MHBlocks.SPIDER_WEB_BLOCK, RenderLayer.getCutout());
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> MHBlocks.BUG_BLOCK_ITEM.getColor(stack), MHBlocks.BUG_BLOCK_ITEM);
		registerRenderers();
		registerScreens();
	}

	private void registerScreens() {
		ScreenRegistry.register(MHScreens.SUPPLY_BOX_SCREEN_HANDLER, BoxScreen::new);
		ScreenRegistry.register(MHScreens.ITEM_BOX_SCREEN_HANDLER, BoxScreen::new);
	}

	private void registerClientItem(MHBaseItem item, String id) {
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> item.getColor(stack), item);
		if (item.shouldRegisterModel()) {
			if (item instanceof MHBaseModelItem modelItem) {
				MHModelManager.registerParental("item/" + id, "monhun:item/" + modelItem.getType());
				GeoItemRenderer.registerItemRenderer(item, new MHItemModelRenderer());
				return;
			}
			MHModelManager.registerTexture("item/" + id, item.getTexture());
		}
	}

	@SuppressWarnings({"unchecked"})
	private void registerRenderers() {
		EntityRendererRegistry.INSTANCE.register(MHEntities.THROWN_ITEM, FlyingItemEntityRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(MHBlocks.BUG_BLOCK_ENTITY,
				(BlockEntityRendererFactory.Context rendererDispatcherIn) -> new MHBugBlockEntityRenderer());

		EntityRendererRegistry.INSTANCE.register(MHEntities.RATHALOS, (context) -> new TempRathalosRenderer(context, "rathalos"));
		/*for (Field f : MHEntities.class.getDeclaredFields()) {
			try {
				Class<?> entityClass = ((ParameterizedType)f.getGenericType()).getActualTypeArguments()[0].getClass();
				if (!MHMonsterEntity.class.isAssignableFrom(entityClass)) continue;
				EntityRendererRegistry.INSTANCE.register((EntityType<? extends MHMonsterEntity>) f.get(null),
						(context) -> new TempRathalosRenderer(context, f.getName().toLowerCase()));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}*/
	}
}
