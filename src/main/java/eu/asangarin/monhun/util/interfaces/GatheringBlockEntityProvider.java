package eu.asangarin.monhun.util.interfaces;

import eu.asangarin.monhun.block.entity.MHGatheringBlockEntity;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public interface GatheringBlockEntityProvider extends BlockEntityProvider {
	@Nullable
	@Override
	default BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return createGatheringBlockEntity(pos, state);
	}

	MHGatheringBlockEntity createGatheringBlockEntity(BlockPos pos, BlockState state);
}
