package eu.asangarin.monhun.client;

import eu.asangarin.monhun.MonHun;
import eu.asangarin.monhun.block.MHBlockItem;
import eu.asangarin.monhun.client.model.ModelBuilder;
import eu.asangarin.monhun.item.MHResourceItem;
import eu.asangarin.monhun.managers.MHItems;
import eu.asangarin.monhun.util.enums.MHGatheringAmount;
import eu.asangarin.monhun.util.enums.MHGatheringType;
import eu.asangarin.monhun.util.enums.MHItemTexture;
import eu.asangarin.monhun.util.enums.MHMonsterClass;
import eu.asangarin.monhun.util.enums.MHWeaponType;
import net.fabricmc.fabric.mixin.object.builder.ModelPredicateProviderRegistryAccessor;
import net.fabricmc.fabric.mixin.object.builder.ModelPredicateProviderRegistrySpecificAccessor;

import java.util.HashMap;
import java.util.Map;

public class MHModelManager {
	private static final Map<String, String> modelTypes = new HashMap<>();

	public static void register() {
		ModelBuilder resItemModel = new ModelBuilder("item/generated").addTexture("layer0", "item/question");
		for (MHItemTexture texture : MHItemTexture.values()) {
			String key = "item/tex/" + texture.toIdentifier();
			registerModel(key, new ModelBuilder("item/generated").addTexture("layer0", "item/" + texture.toIdentifier()));
			resItemModel.addPredicate("texture_value", texture.getValue(), key);
		}
		registerModel("item/resource_item", resItemModel);

		for (MHMonsterClass clazz : MHMonsterClass.values())
			registerModel("item/egg/" + clazz.toIdentifier(), new ModelBuilder("item/generated").addTexture("layer0", "item/egg/monster_egg")
					.addTexture("layer1", "item/egg/" + clazz.toIdentifier() + "_overlay"));

		for (MHWeaponType type : MHWeaponType.values()) {
			registerInternal("item/" + type.getName());
			String key = "item/" + type.getName() + "_gui";
			registerModel(key, new ModelBuilder("item/generated").addTexture("layer0", "item/" + type.getTexture().toIdentifier()));
		}

		ModelBuilder oreItemModel = new ModelBuilder("monhun:block/ore_white");
		for (MHGatheringType type : MHGatheringType.values()) {
			registerOre(type);
			oreItemModel.addPredicate("gathering_type", type.getValue(), "block/ore_" + type.asString());
		}
		registerModel("item/ore_block", oreItemModel);
		registerInternal("block/ore_model");

		ModelBuilder mushroomItemModel = new ModelBuilder("monhun:block/white_mushroom_one");
		for (MHGatheringType type : MHGatheringType.values()) {
			for (MHGatheringAmount amount : MHGatheringAmount.values())
				registerMushroom(type, amount);
			mushroomItemModel.addPredicate("gathering_type", type.getValue(), "block/" + type.asString() + "_mushroom_one");
		}
		registerModel("item/mushroom_block", mushroomItemModel);
		registerInternal("block/mushroom/one");
		registerInternal("block/mushroom/two");
		registerInternal("block/mushroom/three");
		registerInternal("block/mushroom/four");
		registerInternal("block/mushroom/five");
		registerInternal("block/mushroom/six");

		registerModel("item/bug_block", new ModelBuilder("item/generated").addTexture("layer0", "item/butterfly"));
		registerInternal("block/bug_block");

		registerModel("block/delivery_box", new ModelBuilder("monhun:block/box").addTexture("box", "block/delivery_box"));
		registerModel("block/supply_box", new ModelBuilder("monhun:block/box").addTexture("box", "block/supply_box"));
		registerModel("block/item_box", new ModelBuilder("monhun:block/box").addTexture("box", "block/item_box"));
		registerModel("item/delivery_box", new ModelBuilder("monhun:block/closed_box").addTexture("box", "block/delivery_box"));
		registerModel("item/supply_box", new ModelBuilder("monhun:block/closed_box").addTexture("box", "block/supply_box"));
		registerModel("item/item_box", new ModelBuilder("monhun:block/closed_box").addTexture("box", "block/item_box"));
		registerInternal("block/box");
		registerInternal("block/closed_box");

		registerInternal("item/binoculars_in_hand");

		ModelPredicateProviderRegistrySpecificAccessor.callRegister(MHItems.RESOURCE_ITEM, MonHun.i("texture_value"),
				(itemStack, clientWorld, livingEntity, ticks) -> MHResourceItem.getResource(itemStack).getTexture().getValue());
		ModelPredicateProviderRegistryAccessor.callRegister(MonHun.i("gathering_type"), (itemStack, clientWorld, livingEntity, ticks) -> {
			if (!(itemStack.getItem() instanceof MHBlockItem)) return 0f;
			return MHGatheringType.fromStack(itemStack).getValue();
		});
	}

	public static String getItemJson(String item) {
		return modelTypes.getOrDefault(item, "");
	}

	public static void registerTexture(String key, MHItemTexture texture) {
		registerModel(key, new ModelBuilder("item/generated").addTexture("layer0", "item/" + texture.toIdentifier()));
	}

	public static void registerEgg(String key, MHMonsterClass monsterClass) {
		registerParental(key, "monhun:item/egg/" + monsterClass.toIdentifier());
	}

	private static void registerModel(String key, ModelBuilder model) {
		modelTypes.put(key, model.build());
	}

	public static void registerInternal(String key) {
		modelTypes.put(key, "internal");
	}

	public static void registerParental(String key, String parent) {
		modelTypes.put(key, new ModelBuilder(parent).build());
	}

	private static void registerOre(MHGatheringType type) {
		registerModel("block/ore_" + type.asString(),
				new ModelBuilder("monhun:block/ore_model").addTexture("crystal", "block/ore_" + type.asString())
						.addTexture("particle", "block/ore_" + type.asString()));
	}

	private static void registerMushroom(MHGatheringType type, MHGatheringAmount amount) {
		registerModel("block/" + type.asString() + "_mushroom_" + amount.asString(),
				new ModelBuilder("monhun:block/mushroom/" + amount.asString()).addTexture("mushroom", "block/mushroom_" + type.asString())
						.addTexture("particle", "block/mushroom_" + type.asString()));
	}
}
