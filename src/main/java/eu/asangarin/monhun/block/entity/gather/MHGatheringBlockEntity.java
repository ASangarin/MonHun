package eu.asangarin.monhun.block.entity.gather;

import eu.asangarin.monhun.managers.MHBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class MHGatheringBlockEntity extends MHAbstractGatheringBlockEntity {
	public MHGatheringBlockEntity(BlockPos pos, BlockState state) {
		super(MHBlocks.GATHERING_BLOCK_ENTITY, pos, state);
	}
}
