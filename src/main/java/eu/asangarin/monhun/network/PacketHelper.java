package eu.asangarin.monhun.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class PacketHelper {
	public static PacketByteBuf blockPos(BlockPos pos) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeBlockPos(pos);
		return buf;
	}
}
