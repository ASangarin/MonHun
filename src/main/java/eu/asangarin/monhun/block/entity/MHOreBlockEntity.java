package eu.asangarin.monhun.block.entity;

import eu.asangarin.monhun.managers.MHBlocks;
import eu.asangarin.monhun.util.enums.MHGatheringType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.Random;

public class MHOreBlockEntity extends MHGatheringBlockEntity {
	public MHOreBlockEntity(BlockPos pos, BlockState state) {
		super(MHBlocks.ORE_BLOCK_ENTITY, pos, state);
	}
}
