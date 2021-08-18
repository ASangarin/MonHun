package eu.asangarin.monhun.network;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

@Getter
@RequiredArgsConstructor
public enum MHC2SPackets {
	;

	private final Identifier id;
	private final ServerPlayNetworking.PlayChannelHandler handler;
}
