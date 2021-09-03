package eu.asangarin.monhun.entity.npc;

import eu.asangarin.monhun.entity.MHNPCEntity;
import eu.asangarin.monhun.managers.MHItems;
import eu.asangarin.monhun.util.enums.MHNPCType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class MHProvisionerEntity extends MHNPCEntity {
	public MHProvisionerEntity(EntityType<? extends PassiveEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public void initGoals() {
		//this.goalSelector.add(0, new MHStationGoal(this, BlockTags.SIGNS));
		//super.initGoals();
	}

	@Override
	public MHNPCType getNPCType() {
		return MHNPCType.PROVISIONER;
	}

	@Override
	public ItemStack getHeldItem() {
		return ItemStack.EMPTY;
	}

	@Override
	public ActionResult interactMob(PlayerEntity player, Hand hand) {
		swingHand(Hand.MAIN_HAND);
		dropItem();

		return ActionResult.SUCCESS;
	}

	private void dropItem() {
		double d = this.getEyeY() - 0.30000001192092896D;
		ItemEntity itemEntity = new ItemEntity(this.world, this.getX(), d, this.getZ(), new ItemStack(MHItems.POTION));
		itemEntity.setPickupDelay(40);

		float g = MathHelper.sin(this.getPitch() * 0.017453292F);
		float j = MathHelper.cos(this.getPitch() * 0.017453292F);
		float k = MathHelper.sin(this.getYaw() * 0.017453292F);
		float l = MathHelper.cos(this.getYaw() * 0.017453292F);
		float m = this.random.nextFloat() * 6.2831855F;
		float n = 0.02F * this.random.nextFloat();
		itemEntity.setVelocity((double) (-k * j * 0.3F) + Math.cos(m) * (double) n,
				-g * 0.3F + 0.1F + (this.random.nextFloat() - this.random.nextFloat()) * 0.1F, (double) (l * j * 0.3F) + Math.sin(m) * (double) n);
	}
}
