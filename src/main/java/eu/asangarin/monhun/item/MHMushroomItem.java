package eu.asangarin.monhun.item;

import eu.asangarin.monhun.MonHun;
import eu.asangarin.monhun.managers.MHItems;
import eu.asangarin.monhun.util.enums.MHItemTexture;
import eu.asangarin.monhun.util.enums.MHRarity;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class MHMushroomItem extends MHBaseItem {
	public static final MHConsumableItem.ConsumableAction BLUE_MUSHROOM = ((MHConsumableItem) MHItems.POTION).getAction();
	public static final MHConsumableItem.ConsumableAction NITROSHROOM = ((MHConsumableItem) MHItems.DEMONDRUG).getAction();
	public static final MHConsumableItem.ConsumableAction PARASHROOM = ((MHConsumableItem) MHItems.ARMORSKIN).getAction();
	public static final MHConsumableItem.ConsumableAction TOADSTOOL = ((MHConsumableItem) MHItems.NUTRIENTS).getAction();
	public static final MHConsumableItem.ConsumableAction MOPESHROOM = ((MHConsumableItem) MHItems.DASH_JUICE).getAction();
	public static final MHConsumableItem.ConsumableAction DRAGON_TOADSTOOL = ((MHConsumableItem) MHItems.MAX_POTION).getAction();
	public static final MHConsumableItem.ConsumableAction EXCITESHROOM = (stack, world, user) -> {
		switch(MHConsumableItem.random.nextInt(6)) {
			case 0 -> BLUE_MUSHROOM.consume(stack, world, user);
			case 1 -> NITROSHROOM.consume(stack, world, user);
			case 2 -> PARASHROOM.consume(stack, world, user);
			case 3 -> TOADSTOOL.consume(stack, world, user);
			case 4 -> MOPESHROOM.consume(stack, world, user);
			case 5 -> DRAGON_TOADSTOOL.consume(stack, world, user);
		}
	};

	private final MHConsumableItem.ConsumableAction action;

	public MHMushroomItem(int color, MHRarity rarity, int maxCount, MHConsumableItem.ConsumableAction action) {
		super(MHItemTexture.MUSHROOM, color, rarity, maxCount, MonHun.MATERIAL_GROUP);
		this.action = action;
	}

	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		PlayerEntity playerEntity = user instanceof PlayerEntity ? (PlayerEntity) user : null;
		if (playerEntity instanceof ServerPlayerEntity) Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity) playerEntity, stack);

		if (!world.isClient) action.consume(stack, world, user);
		if (playerEntity != null) {
			playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
			if (!playerEntity.getAbilities().creativeMode) stack.decrement(1);
		}

		return stack;
	}

	public int getMaxUseTime(ItemStack stack) {
		return 32;
	}

	public UseAction getUseAction(ItemStack stack) {
		return UseAction.EAT;
	}

	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if (!hasMushroomancer(user)) return TypedActionResult.fail(user.getStackInHand(hand));
		return ItemUsage.consumeHeldItem(world, user, hand);
	}

	private boolean hasMushroomancer(PlayerEntity user) {
		return user.isSneaking();
	}
}
