package eu.asangarin.monhun.item;

import eu.asangarin.monhun.entity.MHThrownItemEntity;
import eu.asangarin.monhun.managers.MHEntities;
import eu.asangarin.monhun.managers.MHItems;
import eu.asangarin.monhun.util.enums.MHItemTexture;
import eu.asangarin.monhun.util.enums.MHRarity;
import eu.asangarin.monhun.util.enums.MHThrowables;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class MHThrowableItem extends MHBaseItem {
	private final MHThrowables thrown;

	public MHThrowableItem(MHItemTexture texture, int color, MHRarity rarity, int maxCount, ItemGroup group, MHThrowables thrown) {
		super(texture, color, rarity, maxCount, group);
		this.thrown = thrown;
	}

	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ENDER_PEARL_THROW, SoundCategory.NEUTRAL, 0.5F,
				0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
		if (!world.isClient) {
			MHThrownItemEntity thrownItemEntity = new MHThrownItemEntity(MHEntities.THROWN_ITEM, world);
			thrownItemEntity.setOwner(user);
			thrownItemEntity.setPos(user.getX(), user.getEyeY() - 0.10000000149011612D, user.getZ());
			thrownItemEntity.setItem(new ItemStack(getThrownItem()));
			thrownItemEntity.setProperties(user, user.getPitch(), user.getYaw(), 0.0F, 1.5F, 1.0F);
			world.spawnEntity(thrownItemEntity);
		}

		user.incrementStat(Stats.USED.getOrCreateStat(this));
		if (!user.getAbilities().creativeMode) {
			itemStack.decrement(1);
		}

		return TypedActionResult.success(itemStack, world.isClient());
	}

	private Item getThrownItem() {
		return switch (thrown) {
			case STONE -> MHItems.THROWN_STONE;
			case PAINTBALL -> MHItems.THROWN_PAINTBALL;
			case FLASH_BOMB -> MHItems.THROWN_FLASH_BOMB;
			case SONIC_BOMB -> MHItems.THROWN_SONIC_BOMB;
			case TRANQ_BOMB -> MHItems.THROWN_TRANQ_BOMB;
			case DUNG_BOMB -> MHItems.THROWN_DUNG_BOMB;
			case EZ_FLASH_BOMB -> MHItems.THROWN_EZ_FLASH_BOMB;
			case EZ_SONIC_BOMB -> MHItems.THROWN_EZ_SONIC_BOMB;
		};
	}
}
