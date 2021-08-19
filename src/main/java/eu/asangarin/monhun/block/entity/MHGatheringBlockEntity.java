package eu.asangarin.monhun.block.entity;

import eu.asangarin.monhun.util.enums.MHGatheringAmount;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public abstract class MHGatheringBlockEntity extends BlockEntity {
	private static final Random random = new Random();
	private int remaining;
	private boolean shiny;

	public MHGatheringBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		remaining = 2 + random.nextInt(4);
		shiny = random.nextDouble() < 0.5d;
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		remaining = nbt.getInt("remaining");
		shiny = nbt.getBoolean("shiny");
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.putInt("remaining", remaining);
		nbt.putBoolean("shiny", shiny);
		return nbt;
	}

	public boolean gather() {
		remaining--;
		markDirty();
		return remaining <= 0;
	}

	public MHGatheringAmount getFromHits() {
		return MHGatheringAmount.fromInteger(remaining);
	}
}
