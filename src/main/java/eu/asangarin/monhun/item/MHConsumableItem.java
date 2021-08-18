package eu.asangarin.monhun.item;

import eu.asangarin.monhun.MonHun;
import eu.asangarin.monhun.util.enums.MHItemTexture;
import eu.asangarin.monhun.util.enums.MHRarity;
import lombok.Getter;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.Random;

public class MHConsumableItem extends MHBaseItem {
	public final static Random random = new Random();
	@Getter
	private final ConsumableAction action;
	private final boolean food, alwaysEat;

	public MHConsumableItem(MHItemTexture texture, int color, MHRarity rarity, int maxCount, ConsumableAction action, boolean food, boolean alwaysEat) {
		super(texture, color, rarity, maxCount, MonHun.CONSUMABLE_GROUP);
		this.action = action;
		this.food = food;
		this.alwaysEat = alwaysEat;
	}

	public MHConsumableItem(MHItemTexture texture, int color, MHRarity rarity, int maxCount, ConsumableAction action, boolean food) {
		this(texture, color, rarity, maxCount, action, food, false);
	}

	public MHConsumableItem(MHItemTexture texture, int color, MHRarity rarity, int maxCount, ConsumableAction action) {
		this(texture, color, rarity, maxCount, action, false);
	}

	public MHConsumableItem(int color, MHRarity rarity, int maxCount, ConsumableAction action) {
		this(MHItemTexture.POTION, color, rarity, maxCount, action, false);
	}

	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		PlayerEntity playerEntity = user instanceof PlayerEntity ? (PlayerEntity) user : null;
		if (playerEntity instanceof ServerPlayerEntity) Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity) playerEntity, stack);

		if (!world.isClient) action.consume(stack, world, user);
		if (playerEntity != null) {
			playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
			if (!playerEntity.getAbilities().creativeMode) stack.decrement(1);
		}

		if(!food) world.emitGameEvent(user, GameEvent.DRINKING_FINISH, user.getCameraBlockPos());
		return stack;
	}

	public int getMaxUseTime(ItemStack stack) {
		return 32;
	}

	public UseAction getUseAction(ItemStack stack) {
		return food ? UseAction.EAT : UseAction.DRINK;
	}

	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if (food && !alwaysEat && !user.canConsume(false)) return TypedActionResult.fail(user.getStackInHand(hand));
		return ItemUsage.consumeHeldItem(world, user, hand);
	}

	@FunctionalInterface
	public interface ConsumableAction {
		void consume(ItemStack stack, World world, LivingEntity user);

		static ConsumableAction potion(StatusEffect effect, int duration, int amplifier) {
			return potion(effect, duration, amplifier, true);
		}

		static ConsumableAction potion(StatusEffect effect, int duration, int amplifier, boolean particles) {
			return (stack, world, user) -> user.addStatusEffect(new StatusEffectInstance(effect, duration, amplifier, true, particles, true));
		}

		static ConsumableAction food(int food, float saturation) {
			return (stack, world, user) -> {
				if(user instanceof PlayerEntity player)
					player.getHungerManager().add(food, saturation);
			};
		}

		ConsumableAction JERKY_ACTION = (stack, world, user) -> {
			user.heal(4.0f);
			user.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 120, 0));
			if (user instanceof PlayerEntity) ((PlayerEntity) user).getHungerManager().add(2, 0.0f);
		};
	}
}
