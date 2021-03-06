package eu.asangarin.monhun;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.asangarin.monhun.block.MHBlockItem;
import eu.asangarin.monhun.block.gather.MHBugBlock;
import eu.asangarin.monhun.block.gather.MHColorGatheringBlock;
import eu.asangarin.monhun.block.gather.MHGatheringBlock;
import eu.asangarin.monhun.block.gather.MHHoneyBlock;
import eu.asangarin.monhun.block.gather.MHMushroomBlock;
import eu.asangarin.monhun.block.gather.MHOreBlock;
import eu.asangarin.monhun.block.gather.MHTopPlantBlock;
import eu.asangarin.monhun.client.dynamic.MHItemDisplayManager;
import eu.asangarin.monhun.config.MHConfig;
import eu.asangarin.monhun.dynamic.MHItemData;
import eu.asangarin.monhun.dynamic.MHItemDataManager;
import eu.asangarin.monhun.effects.IPotionable;
import eu.asangarin.monhun.managers.MHBlocks;
import eu.asangarin.monhun.managers.MHEntities;
import eu.asangarin.monhun.managers.MHItems;
import eu.asangarin.monhun.managers.MHParticles;
import eu.asangarin.monhun.managers.MHStatusEffects;
import eu.asangarin.monhun.managers.MHWeapons;
import eu.asangarin.monhun.mixin.loot.MHBlockStatePropertyLootConditionAccessor;
import eu.asangarin.monhun.mixin.loot.MHItemEntryAccessor;
import eu.asangarin.monhun.mixin.loot.MHLootPoolAccessor;
import eu.asangarin.monhun.mixin.loot.MHLootPoolEntryAccessor;
import eu.asangarin.monhun.mixin.loot.MHLootTableAccessor;
import eu.asangarin.monhun.monsters.MHMonsterManager;
import eu.asangarin.monhun.monsters.data.MHMonsterData;
import eu.asangarin.monhun.network.MHNetwork;
import eu.asangarin.monhun.util.DynamicItemEntry;
import eu.asangarin.monhun.util.MHMonsterDataDeserializer;
import eu.asangarin.monhun.util.RegistryHelper;
import eu.asangarin.monhun.util.enums.MHGatheringType;
import eu.asangarin.monhun.util.interfaces.IBlockStateProvider;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.loot.v1.FabricLootSupplierBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.fabric.impl.loot.table.LootEntryTypeRegistryImpl;
import net.minecraft.block.Block;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.particle.ParticleType;
import net.minecraft.potion.Potion;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import software.bernie.example.GeckoLibMod;
import software.bernie.geckolib3.GeckoLib;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MonHun implements ModInitializer {
	public static final Gson GSON = new GsonBuilder().registerTypeAdapter(MHMonsterData.class, new MHMonsterDataDeserializer()).setPrettyPrinting()
			.create();
	public static final ItemGroup WEAPON_GROUP = FabricItemGroupBuilder.create(MonHun.i("weapons")).icon(() -> new ItemStack(MHWeapons.GS_GUILD))
			.build();
	/*public static final ItemGroup ARMOR_GROUP = FabricItemGroupBuilder.create(MonHun.i("armor")).icon(() -> new ItemStack(MHItems.DUMMY_PLATE))
			.build();*/
	public static final ItemGroup TOOL_GROUP = FabricItemGroupBuilder.create(MonHun.i("tools")).icon(() -> new ItemStack(MHItems.WHETSTONE))
			.build();
	public static final ItemGroup CONSUMABLE_GROUP = FabricItemGroupBuilder.create(MonHun.i("consumables"))
			.icon(() -> new ItemStack(MHItems.POTION)).build();
	public static final ItemGroup MATERIAL_GROUP = FabricItemGroupBuilder.create(MonHun.i("materials"))
			.icon(() -> new ItemStack(MHItems.MACHALITE_ORE)).build();
	public static final ItemGroup RESOURCE_GROUP = FabricItemGroupBuilder.create(MonHun.i("resources"))
			.icon(() -> new ItemStack(MHItems.WYVERN_CLAW)).build();
	public static final ItemGroup SUPPLY_GROUP = FabricItemGroupBuilder.create(MonHun.i("supplies")).icon(() -> new ItemStack(MHItems.RATION))
			.build();
	public static final ItemGroup AMMO_GROUP = FabricItemGroupBuilder.create(MonHun.i("ammo")).icon(() -> new ItemStack(MHItems.EMPTY_PHIAL))
			.build();
	public static final ItemGroup EXTRA_GROUP = FabricItemGroupBuilder.create(MonHun.i("extras")).icon(() -> new ItemStack(MHItems.COMMENDATION))
			.build();
	/*public static final ItemGroup TALISMAN_GROUP = FabricItemGroupBuilder.create(MonHun.i("talismans"))
			.icon(() -> new ItemStack(MHItems.MYSTERY_CHARM)).build();*/
	/*public static final ItemGroup DECORATION_GROUP = FabricItemGroupBuilder.create(MonHun.i("decorations"))
			.icon(() -> new ItemStack(MHItems.AQUAGLOW_JEWEL)).build();*/
	public static final ItemGroup BLOCK_GROUP = FabricItemGroupBuilder.create(MonHun.i("blocks"))
			.icon(() -> new ItemStack(MHBlocks.ORE_BLOCK.asItem())).build();
	public static final ItemGroup ACCOUNT_GROUP = FabricItemGroupBuilder.create(MonHun.i("account")).icon(() -> new ItemStack(MHItems.SAP_PLANT))
			.build();
	public static final ItemGroup SPAWN_EGG_GROUP = FabricItemGroupBuilder.create(MonHun.i("spawn_eggs"))
			.icon(() -> new ItemStack(MHItems.RATHALOS_SPAWN_EGG)).build();
	private static MHConfig config;

	public MonHun() {
		GeckoLibMod.DISABLE_IN_DEV = true;
		GeckoLib.initialize();
	}

	public static MHConfig getConfig() {
		return config;
	}

	@Override
	public void onInitialize() {
		AutoConfig.register(MHConfig.class, PartitioningSerializer.wrap(GsonConfigSerializer::new));
		config = AutoConfig.getConfigHolder(MHConfig.class).getConfig();

		RegistryHelper.registerAll(Registry.ITEM, MHItems.class, Item.class);
		RegistryHelper.registerAll(Registry.ITEM, MHWeapons.class, Item.class);
		RegistryHelper.registerAll(Registry.BLOCK, MHBlocks.class, Block.class);
		RegistryHelper.registerAll(Registry.ITEM, MHBlocks.class, MHBlockItem.class, (ident) -> MonHun.i(ident.getPath().replace("_item", "")));
		RegistryHelper.registerAll(Registry.STATUS_EFFECT, MHStatusEffects.class, StatusEffect.class, (effect, id) -> {
			if (effect instanceof IPotionable potionable) Registry.register(Registry.POTION, MonHun.i(id.getPath() + "_potion"),
					new Potion(new StatusEffectInstance(effect, potionable.defaultTime())));
		});
		RegistryHelper.registerAll(Registry.PARTICLE_TYPE, MHParticles.class, ParticleType.class);

		MHBlocks.registerBlockEntities();
		MHEntities.registerAttributes();
		MHParticles.registerFactories();

		MHNetwork.onServerInitialize();

		LootEntryTypeRegistryImpl.INSTANCE.register(MonHun.i("dynamic_item"), new DynamicItemEntry.Serializer());

		registerCallbacks();
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
				if (player.getAbilities().creativeMode) continue;
				ServerWorld world = player.getServerWorld();
				BlockPos pos = player.getBlockPos();
				if (world.getDimension().isUltrawarm() && !player.hasStatusEffect(MHStatusEffects.COOL_DRINK)) {
					int i = server.getTicks() % 60;
					if (i != 0) return;
					player.damage(DamageSource.IN_FIRE, 1);
				} else if (world.getBiome(pos).isCold(pos) && !player.hasStatusEffect(MHStatusEffects.HOT_DRINK) && world.isSkyVisible(pos)) {
					int min = (player.getMinFreezeDamageTicks() - 10);
					if (player.getFrozenTicks() < min) player.setFrozenTicks(player.getFrozenTicks() + 3);
					if (player.getFrozenTicks() > min)
						player.addStatusEffect(new StatusEffectInstance(MHStatusEffects.FROZEN_HUNGER, 90, 0, false, false));
				}

				int i = server.getTicks() % 60;
				if (i != 0) return;
				if (player.hasStatusEffect(MHStatusEffects.FROZEN_HUNGER) && !player.hasStatusEffect(MHStatusEffects.HOT_DRINK))
					player.getHungerManager().addExhaustion(3f);
			}
		});
	}

	private void registerCallbacks() {
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
			@Override
			public Identifier getFabricId() {
				return MonHun.i("monster_data");
			}

			@Override
			public void reload(ResourceManager manager) {
				MHMonsterManager.clear();
				for (Identifier id : manager.findResources("monster_data", path -> path.endsWith(".json"))) {
					try (InputStream stream = manager.getResource(id).getInputStream()) {
						Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
						MHMonsterManager.add(id.toString().replace("monster_data/", "").replace(".json", ""),
								MonHun.GSON.fromJson(reader, MHMonsterData.class));
					} catch (Exception e) {
						MonHun.log("Error occurred while loading monster data json " + id.toString(), e);
						e.printStackTrace();
					}
				}
			}
		});
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
			@Override
			public Identifier getFabricId() {
				return MonHun.i("mh_items");
			}

			@Override
			public void reload(ResourceManager manager) {
				MHItemDataManager.clear();
				for (Identifier id : manager.findResources("mh_items", path -> path.endsWith(".json"))) {
					try (InputStream stream = manager.getResource(id).getInputStream()) {
						Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
						MHItemDataManager.add(id.toString().replace("mh_items/", "").replace(".json", ""),
								MonHun.GSON.fromJson(reader, MHItemData.class).cached());
					} catch (Exception e) {
						MonHun.log("Error occurred while loading item data json " + id.toString(), e);
						e.printStackTrace();
					}
				}
			}
		});
		LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, table, setter) -> {
			if (id.equals(MHOreBlock.LOOT)) addType(MHOreBlock.class, table, MHBlocks.ORE_BLOCK);
			if (id.equals(MHBugBlock.LOOT)) addType(MHBugBlock.class, table, MHBlocks.BUG_BLOCK);
			if (id.equals(MHMushroomBlock.LOOT)) addType(MHMushroomBlock.class, table, MHBlocks.MUSHROOM_BLOCK);
			if (id.equals(MHHoneyBlock.LOOT))
				addType(MHHoneyBlock.class, table, str -> MHBlocks.HONEY_BLOCK.getDefaultState().with(MHHoneyBlock.ROYAL, str.equals("royal")),
						List.of("normal", "royal"));
			if (id.equals(MHTopPlantBlock.LOOT)) addType(MHTopPlantBlock.class, table, MHBlocks.TOP_PLANT_BLOCK);
		});
	}

	public static Identifier i(String key) {
		return new Identifier("monhun", key);
	}

	public static void log(String message, Object... args) {
		System.out.printf((message) + "%n", args);
	}

	private void addType(Class<? extends MHGatheringBlock> clazz, FabricLootSupplierBuilder table, Block block) {
		addType(clazz, table, string -> block.getDefaultState().with(MHColorGatheringBlock.GATHERING_TYPE, MHGatheringType.fromString(string)),
				Stream.of(MHGatheringType.values()).map(MHGatheringType::asString).collect(Collectors.toList()));
	}

	private void addType(Class<? extends MHGatheringBlock> clazz, FabricLootSupplierBuilder table, IBlockStateProvider stateProvider, List<String> possibilities) {
		if (!MHGatheringBlock.AVAILABLE_RESOURCES.containsKey(clazz))
			MHGatheringBlock.AVAILABLE_RESOURCES.put(clazz, new MHGatheringBlock.AvailableResources());
		MHGatheringBlock.AVAILABLE_RESOURCES.get(clazz).clear();
		Map<String, List<Text>> available = getItems(table, stateProvider, possibilities);
		MHGatheringBlock.AVAILABLE_RESOURCES.get(clazz).putAll(available);
	}

	private Map<String, List<Text>> getItems(FabricLootSupplierBuilder table, IBlockStateProvider stateProvider, List<String> possibilities) {
		Map<String, List<Text>> availables = new HashMap<>();
		LootTable lootTable = table.build();
		final List<MHItemEntryAccessor> entries = new ArrayList<>();
		final List<DynamicItemEntry> dynamicEntries = new ArrayList<>();
		for (LootPool pool : ((MHLootTableAccessor) lootTable).getPools()) {
			for (LootPoolEntry entry : ((MHLootPoolAccessor) pool).getEntries()) {
				if (entry instanceof ItemEntry) entries.add((MHItemEntryAccessor) entry);
				if (entry instanceof DynamicItemEntry) dynamicEntries.add((DynamicItemEntry) entry);
			}
		}

		for (String s : possibilities) {
			final List<Text> itemNames = new ArrayList<>();

			for (MHItemEntryAccessor entry : entries) {
				boolean test = true;
				for (LootCondition condition : ((MHLootPoolEntryAccessor) entry).getConditions()) {
					if (condition instanceof BlockStatePropertyLootCondition stateCondition && !((MHBlockStatePropertyLootConditionAccessor) stateCondition).getProperties()
							.test(stateProvider.provide(s))) {
						test = false;
						break;
					}
				}
				if (test) itemNames.add(entry.getItem().getName());
			}
			for (DynamicItemEntry entry : dynamicEntries) {
				boolean test = true;
				for (LootCondition condition : ((MHLootPoolEntryAccessor) entry).getConditions()) {
					if (condition instanceof BlockStatePropertyLootCondition stateCondition && !((MHBlockStatePropertyLootConditionAccessor) stateCondition).getProperties()
							.test(stateProvider.provide(s))) {
						test = false;
						break;
					}
				}
				if (test) itemNames.add(new TranslatableText(MHItemDisplayManager.getDisplay(entry.getItem()).getTranslationKey()));
			}

			availables.put(s, itemNames);
		}

		return availables;
	}
}