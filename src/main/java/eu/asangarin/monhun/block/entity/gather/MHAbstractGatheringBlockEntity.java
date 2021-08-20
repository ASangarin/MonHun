package eu.asangarin.monhun.block.entity.gather;

import eu.asangarin.monhun.block.gather.MHGatheringBlock;
import eu.asangarin.monhun.managers.MHParticles;
import lombok.Getter;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Random;

public abstract class MHAbstractGatheringBlockEntity extends BlockEntity implements BlockEntityClientSerializable {
	private static final Random random = new Random();
	@Getter
	private int remaining;
	@Getter
	private boolean shiny;

	public MHAbstractGatheringBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		remaining = 2 + random.nextInt(5);
		shiny = random.nextDouble() < 0.1d;
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

	@Override
	public void fromClientTag(NbtCompound nbt) {
		shiny = nbt.getBoolean("shiny");
	}

	@Override
	public NbtCompound toClientTag(NbtCompound nbt) {
		nbt.putBoolean("shiny", shiny);
		return nbt;
	}

	public boolean gather() {
		if (!isShiny() || random.nextDouble() > 0.33d) {
			remaining--;
			markDirty();
		}
		return remaining <= 0;
	}

	public void setShiny(boolean shiny) {
		this.shiny = shiny;
		markDirty();
		sync();
	}

	public static void shinyTick(MHAbstractGatheringBlockEntity blockEntity) {
		World world = blockEntity.world;
		if (world != null && world.isClient()) {
			Vec3d pos = blockEntity.getCachedState().getModelOffset(world, blockEntity.pos)
					.add(new Vec3d(blockEntity.pos.getX(), blockEntity.pos.getY(), blockEntity.pos.getZ()));
			if (world.random.nextInt(16) == 0) {
				double d = pos.getX() + Math.min(0.85d, world.random.nextDouble());
				double e = pos.getY() + blockEntity.getShinyY();
				double f = pos.getZ() + Math.min(0.85d, world.random.nextDouble());
				world.addParticle(MHParticles.SHINY, d, e, f, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	protected double getShinyY() {
		// World will literally never be null, but IntelliJ was complaining
		if (world == null) return 0;
		if (getCachedState().getBlock() instanceof MHGatheringBlock gatheringBlock) return gatheringBlock.getShinyY(world.random);
		return world.random.nextDouble();
	}
}
