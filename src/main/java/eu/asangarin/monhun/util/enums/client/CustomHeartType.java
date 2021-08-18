package eu.asangarin.monhun.util.enums.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum CustomHeartType {
	CONTAINER(0, true),
	ABSORBING(8),
	FIRE(0);

	private final int textureIndex;
	private final boolean canBlink;

	CustomHeartType(int textureIndex) {
		this(textureIndex, false);
	}

	CustomHeartType(int textureIndex, boolean canBlink) {
		this.textureIndex = textureIndex;
		this.canBlink = canBlink;
	}

	/**
	 * {@return the left-most coordinate of the heart texture}
	 */
	public int getU(boolean halfHeart, boolean blinking) {
		int l;
		if (this == CONTAINER) {
			l = blinking ? 1 : 0;
		} else {
			int j = halfHeart ? 1 : 0;
			int k = this.canBlink && blinking ? 2 : 0;
			l = j + k;
		}

		return (this == FIRE ? 0 : 16) + (this.textureIndex * 2 + l) * 9;
	}
}