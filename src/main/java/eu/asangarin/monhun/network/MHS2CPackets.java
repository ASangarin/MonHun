package eu.asangarin.monhun.network;

import eu.asangarin.monhun.MonHun;
import lombok.Getter;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

@Getter
public enum MHS2CPackets {
	BLOCK_BREAK("blockbreakparticles", (client, handler, buf, sender) -> {
		BlockPos pos = buf.readBlockPos();
		if (client.world == null) return;
		BlockState state = client.world.getBlockState(pos);
		BlockSoundGroup blockSoundGroup = state.getSoundGroup();
		client.execute(() -> {
			if (!state.isAir()) {
				client.world.playSound(pos, blockSoundGroup.getBreakSound(), SoundCategory.BLOCKS, (blockSoundGroup.getVolume() + 1.0F) / 2.0F,
						blockSoundGroup.getPitch() * 0.8F, false);
			}
			client.world.addBlockBreakParticles(pos, state);
		});
	}), SOUND("sound", (client, handler, buf, sender) -> {
	});

	private final Identifier id;
	private final ClientPlayNetworking.PlayChannelHandler handler;

	MHS2CPackets(String blockbreak, ClientPlayNetworking.PlayChannelHandler handler) {
		this.id = MonHun.i("packet_" + blockbreak);
		this.handler = handler;
	}
}
