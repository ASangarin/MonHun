package eu.asangarin.monhun.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class MHNetwork {
	public static void sendToClient(MHS2CPackets packet, ServerPlayerEntity player, PacketByteBuf buf) {
		ServerPlayNetworking.send(player, packet.getId(), buf);
	}

	public static void sendToServer(MHC2SPackets packet, PacketByteBuf buf) {
		ClientPlayNetworking.send(packet.getId(), buf);
	}

	public static void onServerInitialize() {
		for (MHC2SPackets packet : MHC2SPackets.values())
			ServerPlayNetworking.registerGlobalReceiver(packet.getId(), packet.getHandler());
	}

	public static void onClientInitialize() {
		for (MHS2CPackets packet : MHS2CPackets.values())
			ClientPlayNetworking.registerGlobalReceiver(packet.getId(), packet.getHandler());
	}
}
