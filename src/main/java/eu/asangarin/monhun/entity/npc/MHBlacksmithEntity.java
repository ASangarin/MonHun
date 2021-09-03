package eu.asangarin.monhun.entity.npc;

import eu.asangarin.monhun.entity.MHNPCEntity;
import eu.asangarin.monhun.gui.MHButtonListScreenHandler;
import eu.asangarin.monhun.managers.MHItems;
import eu.asangarin.monhun.util.enums.MHNPCType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class MHBlacksmithEntity extends MHNPCEntity implements NamedScreenHandlerFactory {
	private static final ItemStack ITEM = new ItemStack(MHItems.LIL_HAMMER);
	private State state = State.NOT_YET;

	public MHBlacksmithEntity(EntityType<? extends PassiveEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public void initGoals() {
		//this.goalSelector.add(0, new MHStationGoal(this, BlockTags.ANVIL));
		//super.initGoals();
	}

	@Override
	public MHNPCType getNPCType() {
		return MHNPCType.BLACKSMITH;
	}

	@Override
	public ItemStack getHeldItem() {
		return ITEM;
	}

	@Override
	public void tick() {
		super.tick();
		if (state == State.NOT_YET) return;
		if (state == State.START && getNavigation().isIdle()) {
			getNavigation().startMovingAlong(getNavigation().findPathTo(new BlockPos(370, 71, -30), 0), 0.5D);
			state = State.PART_II;
		}
		if (state == State.PART_II && getNavigation().isIdle()) {
			getLookControl().lookAt(367.5, 72.5, -29.5, (float) (getBodyYawSpeed() + 20), (float) getLookPitchSpeed());
		}
	}

	@Override
	public ActionResult interactMob(PlayerEntity player, Hand hand) {
		/*player.openHandledScreen(this);*/
		getNavigation().startMovingAlong(getNavigation().findPathTo(new BlockPos(369, 71, -32), 0), 0.5D);
		state = State.START;

		return ActionResult.SUCCESS;
		//return player.world.isClient ? ActionResult.SUCCESS : ActionResult.CONSUME;
	}

	@Nullable
	@Override
	public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
		return new MHButtonListScreenHandler(syncId, MHButtonListScreenHandler.TitleBoxType.SMITHY);
	}

	private enum State {
		NOT_YET, START, PART_II;
	}
}
