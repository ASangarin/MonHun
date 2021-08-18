package eu.asangarin.monhun.block.entity;

import eu.asangarin.monhun.managers.MHBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class MHOreBlockEntity extends BlockEntity {
	private static final Random random = new Random();
	private int remainingHits;

	public MHOreBlockEntity(BlockPos pos, BlockState state) {
		super(MHBlocks.ORE_BLOCK_ENTITY, pos, state);
		remainingHits = 2 + random.nextInt(4);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		remainingHits = nbt.getInt("remaining_hits");
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.putInt("remaining_hits", remainingHits);
		return nbt;
	}

	public boolean mineOre() {
		remainingHits--;
		markDirty();
		return remainingHits <= 0;
	}
}
