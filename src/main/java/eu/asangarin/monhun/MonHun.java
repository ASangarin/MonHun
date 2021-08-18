package eu.asangarin.monhun;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.asangarin.monhun.block.MHBugBlock;
import eu.asangarin.monhun.block.MHOreBlock;
import eu.asangarin.monhun.config.MHConfig;
import eu.asangarin.monhun.managers.MHBlocks;
import eu.asangarin.monhun.managers.MHEntities;
import eu.asangarin.monhun.managers.MHItems;
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
import eu.asangarin.monhun.util.MHMonsterDataDeserializer;
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
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
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

public class MonHun implements ModInitializer {
	private static MHConfig config;
	public static final Gson GSON = new GsonBuilder().registerTypeAdapter(MHMonsterData.class, new MHMonsterDataDeserializer()).setPrettyPrinting()
			.create();

	public MonHun() {
		GeckoLibMod.DISABLE_IN_DEV = true;
		GeckoLib.initialize();
	}

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
	public static final ItemGroup ACCOUNT_GROUP = FabricItemGroupBuilder.create(MonHun.i("account")).icon(() -> new ItemStack(MHItems.WYVERN_TEAR))
			.build();
	public static final ItemGroup SPAWN_EGG_GROUP = FabricItemGroupBuilder.create(MonHun.i("spawn_eggs"))
			.icon(() -> new ItemStack(MHItems.RATHALOS_SPAWN_EGG)).build();

	@Override
	public void onInitialize() {
		AutoConfig.register(MHConfig.class, PartitioningSerializer.wrap(GsonConfigSerializer::new));
		config = AutoConfig.getConfigHolder(MHConfig.class).getConfig();

		MHItems.onInitialize();
		MHBlocks.onInitialize();
		MHEntities.onInitialize();
		MHStatusEffects.onInitialize();
		MHNetwork.onServerInitialize();

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

	public void registerCallbacks() {
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
						MonHun.log("Error occurred while loading resource json " + id.toString(), e);
						e.printStackTrace();
					}
				}
			}
		});
		LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, table, setter) -> {
			if (id.equals(MHOreBlock.ORE_LOOTTABLE)) {
				MHOreBlock.AVAILABLE_ORES.clear();
				Map<MHGatheringType, List<Text>> available = getItemsForOreType(table,
						type -> MHBlocks.ORE_BLOCK.getDefaultState().with(MHOreBlock.ORE_TYPE, type));
				MHOreBlock.AVAILABLE_ORES.putAll(available);
				return;
			}
			if (id.equals(MHBugBlock.BUG_LOOTTABLE)) {
				MHBugBlock.AVAILABLE_BUGS.clear();
				Map<MHGatheringType, List<Text>> available = getItemsForOreType(table,
						type -> MHBlocks.BUG_BLOCK.getDefaultState().with(MHBugBlock.ORE_TYPE, type));
				MHBugBlock.AVAILABLE_BUGS.putAll(available);
			}
		});
	}

	private Map<MHGatheringType, List<Text>> getItemsForOreType(FabricLootSupplierBuilder table, IBlockStateProvider stateProvider) {
		Map<MHGatheringType, List<Text>> availables = new HashMap<>();
		LootTable lootTable = table.build();
		final List<MHItemEntryAccessor> entries = new ArrayList<>();
		for (LootPool pool : ((MHLootTableAccessor) lootTable).getPools()) {
			for (LootPoolEntry entry : ((MHLootPoolAccessor) pool).getEntries()) {
				if (entry instanceof ItemEntry) entries.add((MHItemEntryAccessor) entry);
			}
		}

		for (MHGatheringType type : MHGatheringType.values()) {
			final List<Text> itemNames = new ArrayList<>();

			for (MHItemEntryAccessor entry : entries) {
				for (LootCondition condition : ((MHLootPoolEntryAccessor) entry).getConditions()) {
					if (condition instanceof BlockStatePropertyLootCondition stateCondition && ((MHBlockStatePropertyLootConditionAccessor) stateCondition)
							.getProperties().test(stateProvider.provide(type))) {
						itemNames.add(entry.getItem().getName());
					}
				}
			}

			availables.put(type, itemNames);
		}

		return availables;
	}

	public static Identifier i(String key) {
		return new Identifier("monhun", key);
	}

	public static void log(String message, Object... args) {
		System.out.printf((message) + "%n", args);
	}

	public static MHConfig getConfig() {
		return config;
	}
}