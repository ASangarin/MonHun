package eu.asangarin.monhun.managers;

import eu.asangarin.monhun.MonHun;
import eu.asangarin.monhun.item.MHBaseItem;
import eu.asangarin.monhun.item.MHBinocularsItem;
import eu.asangarin.monhun.item.MHCharmItem;
import eu.asangarin.monhun.item.MHConsumableItem;
import eu.asangarin.monhun.item.MHDynamicItem;
import eu.asangarin.monhun.item.MHMushroomItem;
import eu.asangarin.monhun.item.MHPickaxeItem;
import eu.asangarin.monhun.item.MHSpawnEggItem;
import eu.asangarin.monhun.item.MHThrowableItem;
import eu.asangarin.monhun.item.MHThrownItem;
import eu.asangarin.monhun.item.MHToolItem;
import eu.asangarin.monhun.item.MHWIPItem;
import eu.asangarin.monhun.item.supply.MHBaseSupplyItem;
import eu.asangarin.monhun.item.supply.MHThrowableSupplyItem;
import eu.asangarin.monhun.monsters.MHMonsterManager;
import eu.asangarin.monhun.monsters.data.MHMonsterData;
import eu.asangarin.monhun.util.ItemColors;
import eu.asangarin.monhun.util.UtilityMethods;
import eu.asangarin.monhun.util.enums.MHItemTexture;
import eu.asangarin.monhun.util.enums.MHMonsterClass;
import eu.asangarin.monhun.util.enums.MHRank;
import eu.asangarin.monhun.util.enums.MHRarity;
import eu.asangarin.monhun.util.enums.MHThrowables;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.UUID;

@SuppressWarnings({"ConstantConditions", "unused"})
public class MHItems {
	private static final UUID hpAttUUID = UUID.fromString("8578e6a3-438e-43a4-9c00-93d6d0045814");

	// Dummy Items / WIP Stuff
	public static final MHBaseItem GARBAGE = new MHBaseItem(MHItemTexture.QUESTION, ItemColors.GRAY, MHRarity.RARE_1, 64, MonHun.EXTRA_GROUP);

	public static final MHBaseItem DUMMY_HELM = new MHBaseItem(MHItemTexture.HELM);
	public static final MHBaseItem DUMMY_PLATE = new MHBaseItem(MHItemTexture.PLATE);
	public static final MHBaseItem DUMMY_GAUNTLETS = new MHBaseItem(MHItemTexture.GAUNTLETS);
	public static final MHBaseItem DUMMY_WAIST = new MHBaseItem(MHItemTexture.WAIST);
	public static final MHBaseItem DUMMY_LEGGINGS = new MHBaseItem(MHItemTexture.LEGGINGS);
	public static final MHBaseItem DUMMY_TALISMAN = new MHBaseItem(MHItemTexture.TALISMAN);
	public static final MHBaseItem DUMMY_DECORATION = new MHBaseItem(MHItemTexture.JEWEL);

	public static final MHBaseItem THROWN_STONE = new MHThrownItem(MHItemTexture.THROWN, ItemColors.GRAY,
			(thrown, entity, owner) -> entity.damage(DamageSource.thrownProjectile(thrown, owner), 1));
	public static final MHBaseItem THROWN_PAINTBALL = new MHThrownItem(MHItemTexture.BALL, ItemColors.PINK, (thrown, entity, owner) -> {
		if (entity instanceof LivingEntity living) {
			UtilityMethods.paintballMark(living);
			living.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 2000));
		}
	});
	public static final MHBaseItem THROWN_FLASH_BOMB = new MHThrownItem(MHItemTexture.BALL, ItemColors.YELLOW, (thrown, entity, owner) -> {
	});
	public static final MHBaseItem THROWN_SONIC_BOMB = new MHThrownItem(MHItemTexture.BALL, ItemColors.GRAY, (thrown, entity, owner) -> {
	});
	public static final MHBaseItem THROWN_TRANQ_BOMB = new MHThrownItem(MHItemTexture.BALL, ItemColors.RED, (thrown, entity, owner) -> {
	});
	public static final MHBaseItem THROWN_DUNG_BOMB = new MHThrownItem(MHItemTexture.DUNG, ItemColors.BROWN, (thrown, entity, owner) -> {
	});
	public static final MHBaseItem THROWN_EZ_FLASH_BOMB = new MHThrownItem(MHItemTexture.BALL, ItemColors.BLUE, (thrown, entity, owner) -> {
	});
	public static final MHBaseItem THROWN_EZ_SONIC_BOMB = new MHThrownItem(MHItemTexture.BALL, ItemColors.DARK_GREEN, (thrown, entity, owner) -> {
	});

	// MonHun Dynamic Item
	public static final MHBaseItem DYNAMIC_ITEM = new MHDynamicItem();

	// Consumables
	public static final MHBaseItem POTION = new MHConsumableItem(ItemColors.GREEN, MHRarity.RARE_1, 10, (stack, world, user) -> user.heal(7f));
	public static final MHBaseItem MEGA_POTION = new MHConsumableItem(ItemColors.GREEN, MHRarity.RARE_2, 10,
			(stack, world, user) -> user.heal(14f));
	public static final MHBaseItem NUTRIENTS = new MHConsumableItem(ItemColors.LIGHT_BLUE, MHRarity.RARE_2, 5,
			(stack, world, user) -> modifyHealth(user, 4));
	public static final MHBaseItem MEGA_NUTRIENTS = new MHConsumableItem(ItemColors.LIGHT_BLUE, MHRarity.RARE_3, 5,
			(stack, world, user) -> modifyHealth(user, 8));

	public static final MHBaseItem ANTIDOTE = new MHConsumableItem(ItemColors.BLUE, MHRarity.RARE_1, 10,
			(stack, world, user) -> user.removeStatusEffect(StatusEffects.POISON));
	public static final MHBaseItem IMMUNIZER = new MHWIPItem(MHItemTexture.POTION, ItemColors.YELLOW, MHRarity.RARE_3, 10, MonHun.CONSUMABLE_GROUP);
	public static final MHBaseItem DASH_JUICE = new MHConsumableItem(ItemColors.YELLOW, MHRarity.RARE_2, 5, (stack, world, user) -> {
		// Dash Juice Potion Effect
	});
	public static final MHBaseItem MEGA_DASH_JUICE = new MHConsumableItem(ItemColors.YELLOW, MHRarity.RARE_3, 5, (stack, world, user) -> {
		// Dash Juice Potion Effect
	});
	public static final MHBaseItem DEMONDRUG = new MHConsumableItem(ItemColors.RED, MHRarity.RARE_4, 5,
			MHConsumableItem.ConsumableAction.potion(StatusEffects.STRENGTH, 1200, 0));
	public static final MHBaseItem MEGA_DEMONDRUG = new MHConsumableItem(ItemColors.RED, MHRarity.RARE_5, 5,
			MHConsumableItem.ConsumableAction.potion(StatusEffects.STRENGTH, 2400, 1));
	public static final MHBaseItem MIGHT_PILL = new MHConsumableItem(MHItemTexture.PILL, ItemColors.RED, MHRarity.RARE_1, 5,
			MHConsumableItem.ConsumableAction.potion(StatusEffects.STRENGTH, 120, 4));
	public static final MHBaseItem ARMORSKIN = new MHConsumableItem(ItemColors.ORANGE, MHRarity.RARE_4, 5,
			MHConsumableItem.ConsumableAction.potion(StatusEffects.RESISTANCE, 1200, 0));
	public static final MHBaseItem MEGA_ARMORSKIN = new MHConsumableItem(ItemColors.ORANGE, MHRarity.RARE_5, 5,
			MHConsumableItem.ConsumableAction.potion(StatusEffects.RESISTANCE, 2400, 1));
	public static final MHBaseItem ADAMANT_PILL = new MHConsumableItem(MHItemTexture.PILL, ItemColors.ORANGE, MHRarity.RARE_1, 5,
			MHConsumableItem.ConsumableAction.potion(StatusEffects.RESISTANCE, 120, 4));
	public static final MHBaseItem COOL_DRINK = new MHConsumableItem(MHItemTexture.POTION, ItemColors.WHITE, MHRarity.RARE_1, 5,
			MHConsumableItem.ConsumableAction.potion(MHStatusEffects.COOL_DRINK, 6000, 0));
	public static final MHBaseItem HOT_DRINK = new MHConsumableItem(MHItemTexture.POTION, ItemColors.RED, MHRarity.RARE_1, 5,
			MHConsumableItem.ConsumableAction.potion(MHStatusEffects.HOT_DRINK, 6000, 0));
	public static final MHBaseItem CLEANSER = new MHWIPItem(MHItemTexture.POTION, ItemColors.LIGHT_BLUE, MHRarity.RARE_2, 10,
			MonHun.CONSUMABLE_GROUP);
	public static final MHBaseItem PSYCHOSERUM = new MHWIPItem(MHItemTexture.POTION, ItemColors.ORANGE, MHRarity.RARE_3, 10,
			MonHun.CONSUMABLE_GROUP);
	public static final MHBaseItem HERBAL_MEDICINE = new MHConsumableItem(MHItemTexture.PILL, ItemColors.BLUE, MHRarity.RARE_2, 10,
			(stack, world, user) -> {
				user.heal(4f);
				user.removeStatusEffect(StatusEffects.POISON);
			});
	public static final MHBaseItem MAX_POTION = new MHConsumableItem(MHItemTexture.PILL, ItemColors.YELLOW, MHRarity.RARE_3, 2,
			(stack, world, user) -> {
				modifyHealth(user, 4);
				user.heal(user.getMaxHealth());
			});
	public static final MHBaseItem ANCIENT_POTION = new MHConsumableItem(MHItemTexture.PILL, ItemColors.RED, MHRarity.RARE_5, 1,
			(stack, world, user) -> {
				modifyHealth(user, 4);
				user.heal(user.getMaxHealth());
				if (user instanceof PlayerEntity) {
					HungerManager manager = ((PlayerEntity) user).getHungerManager();
					manager.setFoodLevel(20);
					if (manager.getSaturationLevel() < 5.0F) manager.setSaturationLevel(5.0F);
				}
			});
	public static final MHBaseItem ENERGY_DRINK = new MHConsumableItem(ItemColors.YELLOW, MHRarity.RARE_2, 10, (stack, world, user) -> {
		if (user instanceof PlayerEntity) ((PlayerEntity) user).getHungerManager().add(3, 1.0f);
	});
	public static final MHBaseItem RARE_STEAK = new MHConsumableItem(MHItemTexture.MEAT, ItemColors.ORANGE, MHRarity.RARE_1, 10,
			MHConsumableItem.ConsumableAction.food(12, 8.5f), true);
	public static final MHBaseItem WELLDONE_STEAK = new MHConsumableItem(MHItemTexture.MEAT, ItemColors.ORANGE, MHRarity.RARE_2, 10,
			MHConsumableItem.ConsumableAction.food(12, 12.5f), true);
	public static final MHBaseItem BURNT_MEAT = new MHConsumableItem(MHItemTexture.MEAT, ItemColors.GRAY, MHRarity.RARE_1, 10,
			MHConsumableItem.ConsumableAction.food(-4, -1.0f), true);
	public static final MHBaseItem CHILLED_MEAT = new MHConsumableItem(MHItemTexture.MEAT, ItemColors.LIGHT_BLUE, MHRarity.RARE_1, 10,
			(stack, world, user) -> {
				user.addStatusEffect(new StatusEffectInstance(MHStatusEffects.COOL_DRINK, 6000, 0));
				if (user instanceof PlayerEntity player) player.getHungerManager().add(12, 8.5f);
			});
	public static final MHBaseItem HOT_MEAT = new MHConsumableItem(MHItemTexture.MEAT, ItemColors.RED, MHRarity.RARE_1, 10,
			(stack, world, user) -> {
				user.addStatusEffect(new StatusEffectInstance(MHStatusEffects.HOT_DRINK, 6000, 0));
				if (user instanceof PlayerEntity player) player.getHungerManager().add(12, 8.5f);
			});
	public static final MHBaseItem MOSSWINE_JERKY = new MHConsumableItem(MHItemTexture.MEAT, ItemColors.PINK, MHRarity.RARE_2, 5,
			MHConsumableItem.ConsumableAction.JERKY_ACTION, true, true);
	public static final MHBaseItem YUKUMO_EGG = new MHConsumableItem(MHItemTexture.EGG, ItemColors.WHITE, MHRarity.RARE_1, 5,
			MHConsumableItem.ConsumableAction.JERKY_ACTION, true, true);
	public static final MHBaseItem TANZIA_CHIPS = new MHConsumableItem(MHItemTexture.SAC, ItemColors.WHITE, MHRarity.RARE_2, 5,
			MHConsumableItem.ConsumableAction.JERKY_ACTION, true, true);

	// Hunter's Tools
	public static final MHBaseItem BOOK_OF_COMBOS_1 = new MHWIPItem(MHItemTexture.BOOK, ItemColors.GRAY, MHRarity.RARE_4, 1, MonHun.TOOL_GROUP);
	public static final MHBaseItem BOOK_OF_COMBOS_2 = new MHWIPItem(MHItemTexture.BOOK, ItemColors.GRAY, MHRarity.RARE_4, 1, MonHun.TOOL_GROUP);
	public static final MHBaseItem BOOK_OF_COMBOS_3 = new MHWIPItem(MHItemTexture.BOOK, ItemColors.WHITE, MHRarity.RARE_4, 1, MonHun.TOOL_GROUP);
	public static final MHBaseItem BOOK_OF_COMBOS_4 = new MHWIPItem(MHItemTexture.BOOK, ItemColors.WHITE, MHRarity.RARE_5, 1, MonHun.TOOL_GROUP);
	public static final MHBaseItem BOOK_OF_COMBOS_5 = new MHWIPItem(MHItemTexture.BOOK, ItemColors.YELLOW, MHRarity.RARE_5, 1, MonHun.TOOL_GROUP);

	public static final MHBaseItem BINOCULARS = new MHBinocularsItem();

	public static final MHBaseItem BARREL_BOMB_S = new MHWIPItem(MHItemTexture.BOMB, ItemColors.YELLOW, MHRarity.RARE_2, 10, MonHun.TOOL_GROUP);
	public static final MHBaseItem BARREL_BOMB_L = new MHWIPItem(MHItemTexture.BOMB, ItemColors.RED, MHRarity.RARE_3, 3, MonHun.TOOL_GROUP);
	public static final MHBaseItem BARREL_BOMB_L_plus = new MHWIPItem(MHItemTexture.BOMB, ItemColors.RED, MHRarity.RARE_4, 2, MonHun.TOOL_GROUP);
	public static final MHBaseItem BOUNCE_BOMB = new MHWIPItem(MHItemTexture.BOMB, ItemColors.LIGHT_BLUE, MHRarity.RARE_2, 10, MonHun.TOOL_GROUP);
	public static final MHBaseItem BOUNCE_BOMB_plus = new MHWIPItem(MHItemTexture.BOMB, ItemColors.LIGHT_BLUE, MHRarity.RARE_4, 10,
			MonHun.TOOL_GROUP);

	public static final MHBaseItem PITFALL_TRAP = new MHWIPItem(MHItemTexture.TRAP, ItemColors.GREEN, MHRarity.RARE_3, 1, MonHun.TOOL_GROUP);
	public static final MHBaseItem SHOCK_TRAP = new MHWIPItem(MHItemTexture.TRAP, ItemColors.PURPLE, MHRarity.RARE_3, 1, MonHun.TOOL_GROUP);

	public static final MHBaseItem POWERCHARM = new MHCharmItem(MHItemTexture.SAC, ItemColors.RED, true, true);
	public static final MHBaseItem ARMORCHARM = new MHCharmItem(MHItemTexture.SAC, ItemColors.ORANGE, false, true);
	public static final MHBaseItem POWERTALON = new MHCharmItem(MHItemTexture.CLAW, ItemColors.RED, true, false);
	public static final MHBaseItem ARMORTALON = new MHCharmItem(MHItemTexture.CLAW, ItemColors.ORANGE, false, false);

	public static final MHBaseItem WHETSTONE = new MHWIPItem(MHItemTexture.WHETSTONE, ItemColors.YELLOW, MHRarity.RARE_1, 20, MonHun.TOOL_GROUP);
	public static final MHBaseItem BBQ_SPIT = new MHWIPItem(MHItemTexture.BBQ, ItemColors.RED, MHRarity.RARE_4, 1, MonHun.TOOL_GROUP);

	public static final MHBaseItem POISONED_MEAT = new MHWIPItem(MHItemTexture.MEAT, ItemColors.PURPLE, MHRarity.RARE_1, 10, MonHun.TOOL_GROUP);
	public static final MHBaseItem TINGED_MEAT = new MHWIPItem(MHItemTexture.MEAT, ItemColors.YELLOW, MHRarity.RARE_2, 10, MonHun.TOOL_GROUP);
	public static final MHBaseItem DRUGGED_MEAT = new MHWIPItem(MHItemTexture.MEAT, ItemColors.LIGHT_BLUE, MHRarity.RARE_1, 10, MonHun.TOOL_GROUP);

	public static final MHBaseItem OLD_PICKAXE = new MHPickaxeItem(ItemColors.GRAY, 0.3f);
	public static final MHBaseItem PICKAXE = new MHPickaxeItem(ItemColors.WHITE, 0.15f);
	public static final MHBaseItem MEGA_PICKAXE = new MHPickaxeItem(ItemColors.YELLOW, true, 0.05f);
	public static final MHBaseItem OLD_BUG_NET = new MHToolItem(MHItemTexture.NET, ItemColors.GRAY, false, 0.3f);
	public static final MHBaseItem BUG_NET = new MHToolItem(MHItemTexture.NET, ItemColors.WHITE, false, 0.15f);
	public static final MHBaseItem MEGA_BUG_NET = new MHToolItem(MHItemTexture.NET, ItemColors.YELLOW, true, 0.05f);

	public static final MHBaseItem WORM = new MHWIPItem(MHItemTexture.BAIT, ItemColors.GRAY, MHRarity.RARE_1, 10, MonHun.TOOL_GROUP);
	public static final MHBaseItem MEGA_FISHING_FLY = new MHWIPItem(MHItemTexture.BAIT, ItemColors.PINK, MHRarity.RARE_3, 10, MonHun.TOOL_GROUP);
	public static final MHBaseItem FROG = new MHWIPItem(MHItemTexture.BAIT, ItemColors.BLUE, MHRarity.RARE_2, 10, MonHun.TOOL_GROUP);
	public static final MHBaseItem SUSHIFISH_BAIT = new MHWIPItem(MHItemTexture.BAIT, ItemColors.ORANGE, MHRarity.RARE_2, 5, MonHun.TOOL_GROUP);
	public static final MHBaseItem BURST_BAIT = new MHWIPItem(MHItemTexture.BAIT, ItemColors.RED, MHRarity.RARE_2, 5, MonHun.TOOL_GROUP);
	public static final MHBaseItem GOLDENFISH_BAIT = new MHWIPItem(MHItemTexture.BAIT, ItemColors.YELLOW, MHRarity.RARE_4, 5, MonHun.TOOL_GROUP);

	public static final MHBaseItem BOOMERANG = new MHWIPItem(MHItemTexture.BOOMERANG, ItemColors.YELLOW, MHRarity.RARE_4, 5, MonHun.TOOL_GROUP);
	public static final MHBaseItem THROWING_KNIFE = new MHWIPItem(MHItemTexture.KNIFE, ItemColors.WHITE, MHRarity.RARE_1, 5, MonHun.TOOL_GROUP);
	public static final MHBaseItem POISON_KNIFE = new MHWIPItem(MHItemTexture.KNIFE, ItemColors.PURPLE, MHRarity.RARE_1, 5, MonHun.TOOL_GROUP);
	public static final MHBaseItem SLEEP_KNIFE = new MHWIPItem(MHItemTexture.KNIFE, ItemColors.LIGHT_BLUE, MHRarity.RARE_1, 5, MonHun.TOOL_GROUP);
	public static final MHBaseItem PARALYSIS_KNIFE = new MHWIPItem(MHItemTexture.KNIFE, ItemColors.YELLOW, MHRarity.RARE_1, 5, MonHun.TOOL_GROUP);
	public static final MHBaseItem TRANQ_KNIFE = new MHWIPItem(MHItemTexture.KNIFE, ItemColors.RED, MHRarity.RARE_4, 5, MonHun.TOOL_GROUP);

	public static final MHBaseItem FIELD_HORN = new MHWIPItem(MHItemTexture.HORN, ItemColors.YELLOW, MHRarity.RARE_2, 1, MonHun.TOOL_GROUP);
	public static final MHBaseItem HEALTH_FLUTE = new MHWIPItem(MHItemTexture.HORN, ItemColors.GREEN, MHRarity.RARE_4, 1, MonHun.TOOL_GROUP);
	public static final MHBaseItem ANTIDOTE_FLUTE = new MHWIPItem(MHItemTexture.HORN, ItemColors.BLUE, MHRarity.RARE_4, 1, MonHun.TOOL_GROUP);
	public static final MHBaseItem DEMON_FLUTE = new MHWIPItem(MHItemTexture.HORN, ItemColors.RED, MHRarity.RARE_5, 1, MonHun.TOOL_GROUP);
	public static final MHBaseItem ARMOR_FLUTE = new MHWIPItem(MHItemTexture.HORN, ItemColors.ORANGE, MHRarity.RARE_5, 1, MonHun.TOOL_GROUP);

	public static final MHBaseItem LIFEPOWDER = new MHWIPItem(MHItemTexture.SAC, ItemColors.WHITE, MHRarity.RARE_4, 3, MonHun.TOOL_GROUP);
	public static final MHBaseItem DUST_OF_LIFE = new MHWIPItem(MHItemTexture.SAC, ItemColors.GREEN, MHRarity.RARE_5, 2, MonHun.TOOL_GROUP);

	public static final MHBaseItem PAINTBALL = new MHThrowableItem(MHItemTexture.BALL, ItemColors.PINK, MHRarity.RARE_1, 64, MonHun.TOOL_GROUP,
			MHThrowables.PAINTBALL);
	public static final MHBaseItem FLASH_BOMB = new MHThrowableItem(MHItemTexture.BALL, ItemColors.YELLOW, MHRarity.RARE_2, 5, MonHun.TOOL_GROUP,
			MHThrowables.FLASH_BOMB);
	public static final MHBaseItem SONIC_BOMB = new MHThrowableItem(MHItemTexture.BALL, ItemColors.GRAY, MHRarity.RARE_2, 10, MonHun.TOOL_GROUP,
			MHThrowables.SONIC_BOMB);
	public static final MHBaseItem TRANQ_BOMB = new MHThrowableItem(MHItemTexture.BALL, ItemColors.RED, MHRarity.RARE_4, 8, MonHun.TOOL_GROUP,
			MHThrowables.TRANQ_BOMB);
	public static final MHBaseItem DUNG_BOMB = new MHThrowableItem(MHItemTexture.DUNG, ItemColors.BROWN, MHRarity.RARE_2, 10, MonHun.TOOL_GROUP,
			MHThrowables.DUNG_BOMB);

	public static final MHBaseItem DEODORANT = new MHWIPItem(MHItemTexture.SMOKE, ItemColors.LIGHT_BLUE, MHRarity.RARE_2, 10, MonHun.TOOL_GROUP);
	public static final MHBaseItem SMOKE_BOMB = new MHWIPItem(MHItemTexture.SMOKE, ItemColors.WHITE, MHRarity.RARE_2, 10, MonHun.TOOL_GROUP);
	public static final MHBaseItem POISON_SMOKE_BOMB = new MHWIPItem(MHItemTexture.SMOKE, ItemColors.PURPLE, MHRarity.RARE_2, 10,
			MonHun.TOOL_GROUP);

	public static final MHBaseItem FARCASTER = new MHWIPItem(MHItemTexture.SMOKE, ItemColors.GREEN, MHRarity.RARE_3, 1, MonHun.TOOL_GROUP);

	// Materials
	public static final MHBaseItem CATALYST = new MHBaseItem(MHItemTexture.SAC, ItemColors.GRAY, MHRarity.RARE_2, 5, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem GUNPOWDER = new MHBaseItem(MHItemTexture.SAC, ItemColors.RED, MHRarity.RARE_2, 20, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem LIFECRYSTALS = new MHBaseItem(MHItemTexture.SAC, ItemColors.WHITE, MHRarity.RARE_3, 3, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem TRANQUILIZER = new MHBaseItem(MHItemTexture.POTION, ItemColors.RED, MHRarity.RARE_4, 10, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem BOMB_CASING = new MHBaseItem(MHItemTexture.BALL, ItemColors.WHITE, MHRarity.RARE_1, 30, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem DUNG = new MHBaseItem(MHItemTexture.DUNG, ItemColors.BROWN, MHRarity.RARE_1, 10, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem TRAP_TOOL = new MHBaseItem(MHItemTexture.TRAPTOOL, ItemColors.GREEN, MHRarity.RARE_3, 2, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem SPIDER_WEB = new MHBaseItem(MHItemTexture.WEB, ItemColors.WHITE, MHRarity.RARE_1, 10, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem NET = new MHBaseItem(MHItemTexture.WEB, ItemColors.WHITE, MHRarity.RARE_2, 10, MonHun.MATERIAL_GROUP);

	public static final MHBaseItem BARREL_LID = new MHBaseItem(MHItemTexture.BARREL, ItemColors.GRAY, MHRarity.RARE_5, 64, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem SMALL_BARREL = new MHBaseItem(MHItemTexture.BARREL, ItemColors.YELLOW, MHRarity.RARE_1, 10,
			MonHun.MATERIAL_GROUP);
	public static final MHBaseItem LARGE_BARREL = new MHBaseItem(MHItemTexture.BARREL, ItemColors.RED, MHRarity.RARE_2, 10, MonHun.MATERIAL_GROUP);

	public static final MHBaseItem SLICKAXE = new MHWIPItem(MHItemTexture.PICKAXE, ItemColors.PURPLE, MHRarity.RARE_4, 64, MonHun.MATERIAL_GROUP);

	public static final MHBaseItem RAW_MEAT = new MHWIPItem(MHItemTexture.MEAT, ItemColors.RED, MHRarity.RARE_1, 10, MonHun.MATERIAL_GROUP);

	public static final MHBaseItem STEEL_EGG = new MHBaseItem(MHItemTexture.EGG, ItemColors.GRAY, MHRarity.RARE_4, 64, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem SILVER_EGG = new MHBaseItem(MHItemTexture.EGG, ItemColors.WHITE, MHRarity.RARE_4, 64, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem GOLDEN_EGG = new MHBaseItem(MHItemTexture.EGG, ItemColors.YELLOW, MHRarity.RARE_4, 64, MonHun.MATERIAL_GROUP);
	//public static final MHBaseItem PLATINUM_EGG = new MHBaseItem(MHItemTexture.EGG, ItemColors.WHITE, MHRarity.RARE_9, 64, MonHun.MATERIAL_GROUP);

	public static final MHBaseItem HONEY = new MHBaseItem(MHItemTexture.WEBBING, ItemColors.ORANGE, MHRarity.RARE_2, 10, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem HERB = new MHWIPItem(MHItemTexture.HERB, ItemColors.GREEN, MHRarity.RARE_1, 10, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem ANTIDOTE_HERB = new MHWIPItem(MHItemTexture.HERB, ItemColors.BLUE, MHRarity.RARE_1, 10, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem FIRE_HERB = new MHBaseItem(MHItemTexture.HERB, ItemColors.RED, MHRarity.RARE_2, 20, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem IVY = new MHBaseItem(MHItemTexture.HERB, ItemColors.GREEN, MHRarity.RARE_1, 10, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem SLEEP_HERB = new MHBaseItem(MHItemTexture.HERB, ItemColors.LIGHT_BLUE, MHRarity.RARE_1, 10,
			MonHun.MATERIAL_GROUP);
	public static final MHBaseItem SAP_PLANT = new MHBaseItem(MHItemTexture.HERB, ItemColors.WHITE, MHRarity.RARE_1, 30, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem FELVINE = new MHBaseItem(MHItemTexture.HERB, ItemColors.YELLOW, MHRarity.RARE_1, 10, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem GLOAMGRASS_ROOT = new MHBaseItem(MHItemTexture.HERB, ItemColors.PINK, MHRarity.RARE_2, 10,
			MonHun.MATERIAL_GROUP);
	public static final MHBaseItem HOT_PEPPER = new MHBaseItem(MHItemTexture.HERB, ItemColors.RED, MHRarity.RARE_2, 10, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem SNOW_HERB = new MHBaseItem(MHItemTexture.HERB, ItemColors.BLUE, MHRarity.RARE_2, 1, MonHun.MATERIAL_GROUP);

	public static final MHBaseItem BLUE_MUSHROOM = new MHMushroomItem(ItemColors.BLUE, MHRarity.RARE_2, 10, MHMushroomItem.BLUE_MUSHROOM);
	public static final MHBaseItem NITROSHROOM = new MHMushroomItem(ItemColors.RED, MHRarity.RARE_2, 10, MHMushroomItem.NITROSHROOM);
	public static final MHBaseItem PARASHROOM = new MHMushroomItem(ItemColors.YELLOW, MHRarity.RARE_3, 10, MHMushroomItem.PARASHROOM);
	public static final MHBaseItem TOADSTOOL = new MHMushroomItem(ItemColors.PURPLE, MHRarity.RARE_2, 10, MHMushroomItem.TOADSTOOL);
	public static final MHBaseItem MOPESHROOM = new MHMushroomItem(ItemColors.LIGHT_BLUE, MHRarity.RARE_2, 10, MHMushroomItem.MOPESHROOM);
	public static final MHBaseItem EXCITESHROOM = new MHMushroomItem(ItemColors.PURPLE, MHRarity.RARE_3, 10, MHMushroomItem.EXCITESHROOM);
	public static final MHBaseItem DRAGON_TOADSTOOL = new MHMushroomItem(ItemColors.RED, MHRarity.RARE_3, 10, MHMushroomItem.DRAGON_TOADSTOOL);

	public static final MHBaseItem PAINTBERRY = new MHBaseItem(MHItemTexture.SEED, ItemColors.PINK, MHRarity.RARE_1, 64, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem MIGHT_SEED = new MHWIPItem(MHItemTexture.SEED, ItemColors.RED, MHRarity.RARE_2, 10, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem ADAMANT_SEED = new MHWIPItem(MHItemTexture.SEED, ItemColors.PURPLE, MHRarity.RARE_2, 10, MonHun.MATERIAL_GROUP);
	//public static final MHBaseItem FROZEN_BERRY = new MHWIPItem(MHItemTexture.SEED, ItemColors.PINK, MHRarity.RARE_3, 10, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem NULBERRY = new MHWIPItem(MHItemTexture.SEED, ItemColors.LIGHT_BLUE, MHRarity.RARE_2, 10, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem DRAGONFELL_BERRY = new MHBaseItem(MHItemTexture.SEED, ItemColors.RED, MHRarity.RARE_4, 10,
			MonHun.MATERIAL_GROUP);

	public static final MHBaseItem STONE = new MHThrowableItem(MHItemTexture.ORE, ItemColors.GRAY, MHRarity.RARE_1, 64, MonHun.MATERIAL_GROUP,
			MHThrowables.STONE);
	public static final MHBaseItem IRON_ORE = new MHWIPItem(MHItemTexture.ORE, ItemColors.GRAY, MHRarity.RARE_4, 64, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem EARTH_CRYSTAL = new MHWIPItem(MHItemTexture.ORE, ItemColors.WHITE, MHRarity.RARE_4, 64, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem MACHALITE_ORE = new MHWIPItem(MHItemTexture.ORE, ItemColors.BLUE, MHRarity.RARE_4, 64, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem DRAGONITE_ORE = new MHWIPItem(MHItemTexture.ORE, ItemColors.GREEN, MHRarity.RARE_4, 64, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem LIGHTCRYSTAL = new MHWIPItem(MHItemTexture.ORE, ItemColors.GRAY, MHRarity.RARE_4, 64, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem ICE_CRYSTAL = new MHWIPItem(MHItemTexture.ORE, ItemColors.LIGHT_BLUE, MHRarity.RARE_4, 20,
			MonHun.MATERIAL_GROUP);
	public static final MHBaseItem BEALITE_ORE = new MHWIPItem(MHItemTexture.ORE, ItemColors.LIGHT_BLUE, MHRarity.RARE_4, 64,
			MonHun.MATERIAL_GROUP);
	public static final MHBaseItem FIRESTONE = new MHWIPItem(MHItemTexture.ORE, ItemColors.RED, MHRarity.RARE_4, 64, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem CARBALITE_ORE = new MHWIPItem(MHItemTexture.ORE, ItemColors.PURPLE, MHRarity.RARE_6, 64, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem NOVACRYSTAL = new MHWIPItem(MHItemTexture.ORE, ItemColors.WHITE, MHRarity.RARE_6, 64, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem FUCIUM_ORE = new MHWIPItem(MHItemTexture.ORE, ItemColors.PINK, MHRarity.RARE_6, 64, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem FIRECELL_STONE = new MHWIPItem(MHItemTexture.ORE, ItemColors.ORANGE, MHRarity.RARE_6, 64, MonHun.MATERIAL_GROUP);
	//public static final MHBaseItem PURECRYSTAL = new MHWIPItem(MHItemTexture.ORE, 0xF7F7F7, MHRarity.RARE_8, 1, MonHun.MATERIAL_GROUP);

	public static final MHBaseItem MONSTER_BONE_S = new MHWIPItem(MHItemTexture.BONE, ItemColors.YELLOW, MHRarity.RARE_3, 64,
			MonHun.MATERIAL_GROUP);
	public static final MHBaseItem MONSTER_BONE_M = new MHWIPItem(MHItemTexture.BONE, ItemColors.YELLOW, MHRarity.RARE_4, 64,
			MonHun.MATERIAL_GROUP);
	public static final MHBaseItem MONSTER_BONE_L = new MHWIPItem(MHItemTexture.BONE, ItemColors.YELLOW, MHRarity.RARE_4, 64,
			MonHun.MATERIAL_GROUP);
	public static final MHBaseItem MYSTERY_BONE = new MHWIPItem(MHItemTexture.BONE, ItemColors.YELLOW, MHRarity.RARE_1, 64, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem UNKNOWN_SKULL = new MHWIPItem(MHItemTexture.BONE, ItemColors.YELLOW, MHRarity.RARE_4, 64, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem MONSTER_BONE_plus = new MHWIPItem(MHItemTexture.BONE, ItemColors.ORANGE, MHRarity.RARE_4, 64,
			MonHun.MATERIAL_GROUP);
	public static final MHBaseItem MONSTER_KEENBONE = new MHWIPItem(MHItemTexture.BONE, ItemColors.PINK, MHRarity.RARE_6, 64,
			MonHun.MATERIAL_GROUP);
	public static final MHBaseItem MONSTER_HARDBONE = new MHWIPItem(MHItemTexture.BONE, ItemColors.RED, MHRarity.RARE_6, 64, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem ELDER_DRAGON_BONE = new MHWIPItem(MHItemTexture.BONE, ItemColors.PURPLE, MHRarity.RARE_6, 64,
			MonHun.MATERIAL_GROUP);
	//public static final MHBaseItem LARGE_ELDER_DRAGON_BONE = new MHWIPItem(MHItemTexture.BONE, ItemColors.PURPLE, MHRarity.RARE_8, 64, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem AVIAN_FINEBONE = new MHWIPItem(MHItemTexture.BONE, ItemColors.BLUE, MHRarity.RARE_6, 64, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem BRUTE_BONE = new MHWIPItem(MHItemTexture.BONE, ItemColors.WHITE, MHRarity.RARE_4, 64, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem JUMBO_BONE = new MHWIPItem(MHItemTexture.BONE, ItemColors.WHITE, MHRarity.RARE_4, 64, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem STOUTBONE = new MHWIPItem(MHItemTexture.BONE, ItemColors.WHITE, MHRarity.RARE_6, 64, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem DRAGONBONE_RELIC = new MHWIPItem(MHItemTexture.BONE, ItemColors.WHITE, MHRarity.RARE_6, 64,
			MonHun.MATERIAL_GROUP);
	public static final MHBaseItem WYVERN_FANG = new MHWIPItem(MHItemTexture.CLAW, ItemColors.WHITE, MHRarity.RARE_3, 50, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem WYVERN_CLAW = new MHWIPItem(MHItemTexture.CLAW, ItemColors.WHITE, MHRarity.RARE_3, 50, MonHun.MATERIAL_GROUP);

	public static final MHBaseItem INSECT_HUSK = new MHWIPItem(MHItemTexture.BUG, ItemColors.GRAY, MHRarity.RARE_1, 10, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem GODBUG = new MHWIPItem(MHItemTexture.BUG, ItemColors.WHITE, MHRarity.RARE_4, 10, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem BITTERBUG = new MHWIPItem(MHItemTexture.BUG, ItemColors.BLUE, MHRarity.RARE_1, 10, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem FLASHBUG = new MHWIPItem(MHItemTexture.BUG, ItemColors.YELLOW, MHRarity.RARE_2, 10, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem THUNDERBUG = new MHWIPItem(MHItemTexture.BUG, ItemColors.YELLOW, MHRarity.RARE_4, 64, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem STINKHOPPER = new MHWIPItem(MHItemTexture.BUG, ItemColors.RED, MHRarity.RARE_1, 64, MonHun.MATERIAL_GROUP);
	//public static final MHBaseItem GLUEGLOPPER = new MHWIPItem(MHItemTexture.BUG, 0xF7F7F7, MHRarity.RARE_1, 64, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem SNAKEBEE_LARVA = new MHWIPItem(MHItemTexture.BUG, ItemColors.ORANGE, MHRarity.RARE_1, 64, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem CARPENTERBUG = new MHWIPItem(MHItemTexture.BUG, ItemColors.GRAY, MHRarity.RARE_1, 64, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem SHINY_BEETLE = new MHWIPItem(MHItemTexture.BUG, ItemColors.GREEN, MHRarity.RARE_4, 64, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem KILLER_BEETLE = new MHWIPItem(MHItemTexture.BUG, ItemColors.GREEN, MHRarity.RARE_4, 64, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem HERCUDROME = new MHWIPItem(MHItemTexture.BUG, ItemColors.RED, MHRarity.RARE_4, 64, MonHun.MATERIAL_GROUP);
	//public static final MHBaseItem FULGURBUG = new MHWIPItem(MHItemTexture.BUG, ItemColors.BLUE, MHRarity.RARE_4, 64, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem KING_SCARAB = new MHWIPItem(MHItemTexture.BUG, ItemColors.PURPLE, MHRarity.RARE_6, 64, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem RARE_SCARAB = new MHWIPItem(MHItemTexture.BUG, ItemColors.YELLOW, MHRarity.RARE_6, 64, MonHun.MATERIAL_GROUP);

	public static final MHBaseItem WHETFISH = new MHWIPItem(MHItemTexture.FISH, ItemColors.YELLOW, MHRarity.RARE_1, 10, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem SUSHIFISH = new MHWIPItem(MHItemTexture.FISH, ItemColors.ORANGE, MHRarity.RARE_1, 10, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem SLEEPYFISH = new MHWIPItem(MHItemTexture.FISH, ItemColors.LIGHT_BLUE, MHRarity.RARE_2, 10,
			MonHun.MATERIAL_GROUP);
	public static final MHBaseItem PIN_TUNA = new MHWIPItem(MHItemTexture.FISH, ItemColors.GRAY, MHRarity.RARE_1, 30, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem SPEARTUNA = new MHWIPItem(MHItemTexture.FISH, ItemColors.BLUE, MHRarity.RARE_5, 10, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem POPFISH = new MHWIPItem(MHItemTexture.FISH, ItemColors.GRAY, MHRarity.RARE_2, 30, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem SCATTERFISH = new MHWIPItem(MHItemTexture.FISH, ItemColors.GRAY, MHRarity.RARE_4, 10, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem BURST_AROWANA = new MHWIPItem(MHItemTexture.FISH, ItemColors.GREEN, MHRarity.RARE_4, 10, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem BOMB_AROWANA = new MHWIPItem(MHItemTexture.FISH, ItemColors.PURPLE, MHRarity.RARE_4, 10, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem GLUTTON_TUNA = new MHWIPItem(MHItemTexture.FISH, ItemColors.ORANGE, MHRarity.RARE_4, 10, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem GASTRONOME_TUNA = new MHWIPItem(MHItemTexture.FISH, ItemColors.ORANGE, MHRarity.RARE_5, 5,
			MonHun.MATERIAL_GROUP);
	public static final MHBaseItem SMALL_GOLDENFISH = new MHWIPItem(MHItemTexture.FISH, ItemColors.YELLOW, MHRarity.RARE_4, 64,
			MonHun.MATERIAL_GROUP);
	public static final MHBaseItem WANCHOVY = new MHWIPItem(MHItemTexture.FISH, ItemColors.GREEN, MHRarity.RARE_2, 20, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem ANCIENT_FISH = new MHWIPItem(MHItemTexture.FISH, ItemColors.WHITE, MHRarity.RARE_5, 5, MonHun.MATERIAL_GROUP);

	public static final MHBaseItem SCREAMER_SAC = new MHWIPItem(MHItemTexture.SAC, ItemColors.GRAY, MHRarity.RARE_4, 64, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem POISON_SAC = new MHWIPItem(MHItemTexture.SAC, ItemColors.PURPLE, MHRarity.RARE_4, 64, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem TOXIN_SAC = new MHWIPItem(MHItemTexture.SAC, ItemColors.PURPLE, MHRarity.RARE_6, 64, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem PARALYSIS_SAC = new MHWIPItem(MHItemTexture.SAC, ItemColors.YELLOW, MHRarity.RARE_4, 64, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem OMNIPLEGIA_SAC = new MHWIPItem(MHItemTexture.SAC, ItemColors.YELLOW, MHRarity.RARE_6, 64, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem SLEEP_SAC = new MHWIPItem(MHItemTexture.SAC, ItemColors.BLUE, MHRarity.RARE_4, 64, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem COMA_SAC = new MHWIPItem(MHItemTexture.SAC, ItemColors.BLUE, MHRarity.RARE_6, 64, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem FLAME_SAC = new MHWIPItem(MHItemTexture.SAC, ItemColors.RED, MHRarity.RARE_4, 64, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem INFERNO_SAC = new MHWIPItem(MHItemTexture.SAC, ItemColors.RED, MHRarity.RARE_6, 64, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem ELECTRO_SAC = new MHWIPItem(MHItemTexture.SAC, ItemColors.YELLOW, MHRarity.RARE_4, 64, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem THUNDER_SAC = new MHWIPItem(MHItemTexture.SAC, ItemColors.YELLOW, MHRarity.RARE_6, 64, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem FROST_SAC = new MHWIPItem(MHItemTexture.SAC, ItemColors.WHITE, MHRarity.RARE_4, 64, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem FREEZER_SAC = new MHWIPItem(MHItemTexture.SAC, ItemColors.WHITE, MHRarity.RARE_6, 64, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem AQUA_SAC = new MHWIPItem(MHItemTexture.SAC, ItemColors.LIGHT_BLUE, MHRarity.RARE_4, 64, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem TORRENT_SAC = new MHWIPItem(MHItemTexture.SAC, ItemColors.LIGHT_BLUE, MHRarity.RARE_6, 64,
			MonHun.MATERIAL_GROUP);
	public static final MHBaseItem DASH_EXTRACT = new MHWIPItem(MHItemTexture.POTION, ItemColors.YELLOW, MHRarity.RARE_4, 20,
			MonHun.MATERIAL_GROUP);
	public static final MHBaseItem PALE_EXTRACT = new MHWIPItem(MHItemTexture.POTION, ItemColors.WHITE, MHRarity.RARE_4, 20, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem MONSTER_FLUID = new MHWIPItem(MHItemTexture.POTION, ItemColors.LIGHT_BLUE, MHRarity.RARE_4, 64,
			MonHun.MATERIAL_GROUP);
	public static final MHBaseItem MONSTER_BROTH = new MHWIPItem(MHItemTexture.POTION, ItemColors.BLUE, MHRarity.RARE_6, 64, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem ELDER_DRAGON_BLOOD = new MHWIPItem(MHItemTexture.POTION, ItemColors.RED, MHRarity.RARE_4, 64,
			MonHun.MATERIAL_GROUP);
	public static final MHBaseItem PURE_DRAGON_BLOOD = new MHWIPItem(MHItemTexture.POTION, ItemColors.RED, MHRarity.RARE_6, 64,
			MonHun.MATERIAL_GROUP);

	public static final MHBaseItem BIRD_WYVERN_GEM = new MHWIPItem(MHItemTexture.BALL, ItemColors.BLUE, MHRarity.RARE_7, 64, MonHun.MATERIAL_GROUP);
	public static final MHBaseItem WYVERN_GEM = new MHWIPItem(MHItemTexture.BALL, ItemColors.LIGHT_BLUE, MHRarity.RARE_7, 64,
			MonHun.MATERIAL_GROUP);

	// Field Supplies
	public static final MHBaseItem MAP = new MHBaseSupplyItem(MHItemTexture.MAP, ItemColors.WHITE, MHRarity.RARE_1, 1, MonHun.SUPPLY_GROUP);
	public static final MHBaseItem FIRST_AID_MED = new MHBaseSupplyItem(MHItemTexture.POTION, ItemColors.GREEN, MHRarity.RARE_1, 10,
			MonHun.SUPPLY_GROUP);
	public static final MHBaseItem FIRST_AID_MED_plus = new MHBaseSupplyItem(MHItemTexture.POTION, ItemColors.GREEN, MHRarity.RARE_2, 10,
			MonHun.SUPPLY_GROUP);
	public static final MHBaseItem RATION = new MHBaseSupplyItem(MHItemTexture.MEAT, ItemColors.ORANGE, MHRarity.RARE_1, 10, MonHun.SUPPLY_GROUP);
	public static final MHBaseItem MINI_WHETSTONE = new MHBaseSupplyItem(MHItemTexture.WHETSTONE, ItemColors.YELLOW, MHRarity.RARE_1, 10,
			MonHun.SUPPLY_GROUP);
	public static final MHBaseItem EZ_PITFALL_TRAP = new MHBaseSupplyItem(MHItemTexture.TRAP, ItemColors.BLUE, MHRarity.RARE_3, 1,
			MonHun.SUPPLY_GROUP);
	public static final MHBaseItem EZ_SHOCK_TRAP = new MHBaseSupplyItem(MHItemTexture.TRAP, ItemColors.PURPLE, MHRarity.RARE_3, 1,
			MonHun.SUPPLY_GROUP);
	public static final MHBaseItem EZ_FLASH_BOMB = new MHThrowableSupplyItem(MHItemTexture.BALL, ItemColors.BLUE, MHRarity.RARE_2, 1,
			MonHun.SUPPLY_GROUP, MHThrowables.EZ_FLASH_BOMB);
	public static final MHBaseItem EZ_SONIC_BOMB = new MHThrowableSupplyItem(MHItemTexture.BALL, ItemColors.DARK_GREEN, MHRarity.RARE_2, 1,
			MonHun.SUPPLY_GROUP, MHThrowables.EZ_SONIC_BOMB);
	public static final MHBaseItem EZ_FARCASTER = new MHBaseSupplyItem(MHItemTexture.SMOKE, ItemColors.GREEN, MHRarity.RARE_2, 1,
			MonHun.SUPPLY_GROUP);
	public static final MHBaseItem EZ_MAX_POTION = new MHBaseSupplyItem(MHItemTexture.PILL, ItemColors.BLUE, MHRarity.RARE_2, 3,
			MonHun.SUPPLY_GROUP);
	public static final MHBaseItem EZ_LIFEPOWDER = new MHBaseSupplyItem(MHItemTexture.SAC, ItemColors.BLUE, MHRarity.RARE_2, 2,
			MonHun.SUPPLY_GROUP);
	public static final MHBaseItem EZ_DUST_OF_LIFE = new MHBaseSupplyItem(MHItemTexture.SAC, ItemColors.CYAN, MHRarity.RARE_3, 2,
			MonHun.SUPPLY_GROUP);
	public static final MHBaseItem EZ_BARREL_BOMB_L = new MHBaseSupplyItem(MHItemTexture.BOMB, ItemColors.BLUE, MHRarity.RARE_3, 1,
			MonHun.SUPPLY_GROUP);
	public static final MHBaseItem PAW_PASS_TICKET = new MHBaseSupplyItem(MHItemTexture.TICKET, ItemColors.WHITE, MHRarity.RARE_5, 1,
			MonHun.SUPPLY_GROUP);

	// Ammo
	public static final MHBaseItem HUSKBERRY = new MHWIPItem(MHItemTexture.HUSK, ItemColors.GRAY, MHRarity.RARE_1, 64, MonHun.AMMO_GROUP);
	public static final MHBaseItem SCATTERNUT = new MHWIPItem(MHItemTexture.SEED, ItemColors.GRAY, MHRarity.RARE_2, 30, MonHun.AMMO_GROUP);
	public static final MHBaseItem NEEDLEBERRY = new MHWIPItem(MHItemTexture.SEED, ItemColors.GRAY, MHRarity.RARE_1, 64, MonHun.AMMO_GROUP);
	public static final MHBaseItem LATCHBERRY = new MHWIPItem(MHItemTexture.SEED, ItemColors.LIGHT_BLUE, MHRarity.RARE_1, 60, MonHun.AMMO_GROUP);
	public static final MHBaseItem BOMBERRY = new MHWIPItem(MHItemTexture.SEED, ItemColors.GRAY, MHRarity.RARE_2, 10, MonHun.AMMO_GROUP);
	public static final MHBaseItem BONE_HUSK = new MHWIPItem(MHItemTexture.HUSK, ItemColors.WHITE, MHRarity.RARE_2, 64, MonHun.AMMO_GROUP);

	public static final MHBaseItem EMPTY_PHIAL = new MHWIPItem(MHItemTexture.VIAL, ItemColors.WHITE, MHRarity.RARE_1, 64, MonHun.AMMO_GROUP);

	// Extras
	public static final MHBaseItem AQUAGLOW_JEWEL = new MHWIPItem(MHItemTexture.JEWEL, ItemColors.LIGHT_BLUE, MHRarity.RARE_4, 64,
			MonHun.EXTRA_GROUP);
	public static final MHBaseItem SUNSPIRE_JEWEL = new MHWIPItem(MHItemTexture.JEWEL, ItemColors.WHITE, MHRarity.RARE_4, 64, MonHun.EXTRA_GROUP);
	public static final MHBaseItem BLOODRUN_JEWEL = new MHWIPItem(MHItemTexture.JEWEL, ItemColors.PURPLE, MHRarity.RARE_5, 64, MonHun.EXTRA_GROUP);
	public static final MHBaseItem LAZURITE_JEWEL = new MHWIPItem(MHItemTexture.JEWEL, ItemColors.YELLOW, MHRarity.RARE_5, 64, MonHun.EXTRA_GROUP);
	//public static final MHBaseItem ADAMANT_ORB = new MHWIPItem(MHItemTexture.JEWEL, ItemColors.ORANGE, MHRarity.RARE_8, 64, MonHun.EXTRA_GROUP);
	//public static final MHBaseItem SOVEREIGN_ORB = new MHWIPItem(MHItemTexture.JEWEL, ItemColors.RED, MHRarity.RARE_8, 64, MonHun.EXTRA_GROUP);

	public static final MHBaseItem MYSTERY_CHARM = new MHWIPItem(MHItemTexture.CHARM, ItemColors.WHITE, MHRarity.RARE_4, 10, MonHun.EXTRA_GROUP);
	public static final MHBaseItem SHINING_CHARM = new MHWIPItem(MHItemTexture.CHARM, ItemColors.YELLOW, MHRarity.RARE_5, 10, MonHun.EXTRA_GROUP);
	public static final MHBaseItem TIMEWORN_CHARM = new MHWIPItem(MHItemTexture.CHARM, ItemColors.RED, MHRarity.RARE_6, 10, MonHun.EXTRA_GROUP);
	//public static final MHBaseItem ENDURING_CHARM = new MHWIPItem(MHItemTexture.CHARM, ItemColors.LIGHT_BLUE, MHRarity.RARE_8, 10, MonHun.EXTRA_GROUP);

	public static final MHBaseItem ARMOR_SPHERE = new MHWIPItem(MHItemTexture.SPHERE, ItemColors.BLUE, MHRarity.RARE_4, 64, MonHun.EXTRA_GROUP);
	public static final MHBaseItem ARMOR_SPHERE_plus = new MHWIPItem(MHItemTexture.SPHERE, ItemColors.GREEN, MHRarity.RARE_4, 64,
			MonHun.EXTRA_GROUP);
	public static final MHBaseItem ADVANCED_ARMOR_SPHERE = new MHWIPItem(MHItemTexture.SPHERE, ItemColors.ORANGE, MHRarity.RARE_5, 64,
			MonHun.EXTRA_GROUP);
	public static final MHBaseItem HARD_ARMOR_SPHERE = new MHWIPItem(MHItemTexture.SPHERE, ItemColors.PURPLE, MHRarity.RARE_5, 64,
			MonHun.EXTRA_GROUP);
	public static final MHBaseItem HEAVY_ARMOR_SPHERE = new MHWIPItem(MHItemTexture.SPHERE, ItemColors.RED, MHRarity.RARE_6, 64,
			MonHun.EXTRA_GROUP);
	//public static final MHBaseItem KING_ARMOR_SPHERE = new MHWIPItem(MHItemTexture.SPHERE, ItemColors.YELLOW, MHRarity.RARE_8, 64, MonHun.EXTRA_GROUP);
	//public static final MHBaseItem TRUE_ARMOR_SPHERE = new MHWIPItem(MHItemTexture.SPHERE, ItemColors.LIGHT_BLUE, MHRarity.RARE_9, 64, MonHun.EXTRA_GROUP);
	//public static final MHBaseItem DIVINE_ARMOR_SPHERE = new MHWIPItem(MHItemTexture.SPHERE, ItemColors.CYAN, MHRarity.RARE_10, 64, MonHun.EXTRA_GROUP);

	public static final MHBaseItem VOUCHER = new MHWIPItem(MHItemTexture.TICKET, ItemColors.WHITE, MHRarity.RARE_4, 64, MonHun.EXTRA_GROUP);
	public static final MHBaseItem GOURMET_VOUCHER = new MHWIPItem(MHItemTexture.TICKET, ItemColors.YELLOW, MHRarity.RARE_5, 64,
			MonHun.EXTRA_GROUP);
	public static final MHBaseItem COMMENDATION = new MHWIPItem(MHItemTexture.TICKET, ItemColors.ORANGE, MHRarity.RARE_4, 64, MonHun.EXTRA_GROUP) {
		public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
			ItemStack itemStack = user.getStackInHand(hand);
			if (!world.isClient) {
				MHMonsterData data = MHMonsterManager.getData("rathalos");
				if (data != null) {
					for (MHRank rank : MHRank.values())
						for (ItemStack stack : data.getStats().get(rank).getDrops().allItems())
							if (stack != null && !stack.isEmpty()) user.dropItem(stack, false, false);
				}
			}

			return TypedActionResult.success(itemStack, world.isClient());
		}
	};
	public static final MHBaseItem COMMENDATION_G = new MHWIPItem(MHItemTexture.TICKET, ItemColors.ORANGE, MHRarity.RARE_5, 64, MonHun.EXTRA_GROUP);
	public static final MHBaseItem PAWPRINT_STAMP = new MHWIPItem(MHItemTexture.TICKET, ItemColors.WHITE, MHRarity.RARE_5, 64, MonHun.EXTRA_GROUP);
	//public static final MHBaseItem PAWPRINT_TICKET = new MHWIPItem(MHItemTexture.TICKET, ItemColors.WHITE, MHRarity.RARE_8, 64, MonHun.MATERIAL_GROUP);

	public static final MHSpawnEggItem RATHALOS_SPAWN_EGG = new MHSpawnEggItem(MHEntities.RATHALOS, 0xDF3916, 0x4A4A4A,
			MHMonsterClass.FLYING_WYVERN);

	private static void modifyHealth(LivingEntity user, double value) {
		EntityAttributeInstance instance = user.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
		EntityAttributeModifier modifier = instance.getModifier(MHItems.hpAttUUID);
		if (modifier != null) {
			if (modifier.getValue() != (double) 8) {
				instance.removeModifier(MHItems.hpAttUUID);
				instance.addPersistentModifier(
						new EntityAttributeModifier(MHItems.hpAttUUID, "monhun_maxhealth", 8, EntityAttributeModifier.Operation.ADDITION));
			}
		} else instance.addPersistentModifier(
				new EntityAttributeModifier(MHItems.hpAttUUID, "monhun_maxhealth", value, EntityAttributeModifier.Operation.ADDITION));
	}

	public static ItemStack getStackOf(String id, int amount) {
		Item item = Registry.ITEM.getOrEmpty(MonHun.i(id)).orElse(MHItems.DYNAMIC_ITEM);
		if (item == MHItems.DYNAMIC_ITEM) return MHDynamicItem.withNBT(id.contains(":") ? id : "monhun:" + id);
		return new ItemStack(item, amount);
	}
}
