package eu.asangarin.monhun.util.interfaces;

import eu.asangarin.monhun.util.enums.MHGatheringType;
import net.minecraft.block.BlockState;

@FunctionalInterface
public interface IBlockStateProvider {
	BlockState provide(MHGatheringType type);
}
