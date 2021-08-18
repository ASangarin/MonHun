package eu.asangarin.monhun.item;

import eu.asangarin.monhun.MonHun;
import eu.asangarin.monhun.util.ItemColors;
import eu.asangarin.monhun.util.enums.MHItemTexture;
import eu.asangarin.monhun.util.enums.MHRarity;
import eu.asangarin.monhun.util.interfaces.IMHModelItem;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class MHBinocularsItem extends MHBaseItem implements IMHModelItem {
	public MHBinocularsItem() {
		super(MHItemTexture.BINOCULARS, ItemColors.WHITE, MHRarity.RARE_1, 1, MonHun.TOOL_GROUP);
	}

	public int getMaxUseTime(ItemStack stack) {
		return 1200;
	}

	public UseAction getUseAction(ItemStack stack) {
		return UseAction.SPYGLASS;
	}

	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		user.playSound(SoundEvents.ITEM_SPYGLASS_USE, 1.0F, 1.0F);
		user.incrementStat(Stats.USED.getOrCreateStat(this));
		return ItemUsage.consumeHeldItem(world, user, hand);
	}

	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		this.playStopUsingSound(user);
		return stack;
	}

	public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		this.playStopUsingSound(user);
	}

	private void playStopUsingSound(LivingEntity user) {
		user.playSound(SoundEvents.ITEM_SPYGLASS_STOP_USING, 1.0F, 1.0F);
	}

	@Override
	public String getModelName() {
		return "binoculars_in_hand";
	}

	@Override
	public boolean shouldRender(ModelTransformation.Mode mode) {
		return mode != ModelTransformation.Mode.GUI && mode != ModelTransformation.Mode.FIXED && mode != ModelTransformation.Mode.GROUND;
	}

	@Override
	public boolean renderNormal() {
		return true;
	}
}
