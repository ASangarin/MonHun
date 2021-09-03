package eu.asangarin.monhun.entity.goals;

import eu.asangarin.monhun.entity.MHNPCEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.tag.Tag;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;
import java.util.EnumSet;

public class MHStationGoal extends Goal {
	protected final MHNPCEntity entity;
	private final Tag<Block> tag;
	protected BlockPos anvilPos;
	protected BlockPos pos;
	private int cooldown;

	public MHStationGoal(MHNPCEntity entity, Tag<Block> tag) {
		this.entity = entity;
		this.tag = tag;
		this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
	}

	public boolean shouldContinue() {
		return pos != null && entity.isNavigating();
	}

	public void start() {
		if (anvilPos != null && tag.contains(entity.world.getBlockState(anvilPos).getBlock())) return;
		anvilPos = null;
		pos = null;
		for (BlockPos position : BlockPos.iterateOutwards(entity.getBlockPos(), 30, 20, 30)) {
			if (tag.contains(entity.world.getBlockState(position).getBlock())) {
				for (BlockPos offset : Arrays.asList(position.east(), position.west(), position.north(), position.south())) {
					if (checkPos(offset)) {
						this.anvilPos = position;
						this.pos = offset;
						break;
					}
				}
				break;
			}
		}
	}

	private boolean checkPos(BlockPos position) {
		return entity.world.getBlockState(position).isAir() && entity.world.getBlockState(position.up()).isAir();
	}

	public void stop() {
		this.entity.getNavigation().stop();
		this.cooldown = 100;
	}

	public void tick() {
		if (pos == null || entity.squaredDistanceTo(pos.getX(), pos.getY(), pos.getZ()) <= 0.4D) return;
		if (anvilPos != null) this.entity.getLookControl()
				.lookAt(anvilPos.getX() + 0.5d, anvilPos.getY() + 1.5d, anvilPos.getZ() + 0.5d, (float) (this.entity.getBodyYawSpeed() + 20),
						(float) this.entity.getLookPitchSpeed());
		this.entity.getNavigation().startMovingAlong(this.entity.getNavigation().findPathTo(pos, 0), 0.5D);
	}

	@Override
	public boolean canStart() {
		if (this.cooldown > 0) {
			--this.cooldown;
			return false;
		}
		return true;
	}
}
