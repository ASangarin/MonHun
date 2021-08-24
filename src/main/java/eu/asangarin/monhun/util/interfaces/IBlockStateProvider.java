package eu.asangarin.monhun.util.interfaces;

import net.minecraft.block.BlockState;

@FunctionalInterface
public interface IBlockStateProvider {
	BlockState provide(String str);
}
