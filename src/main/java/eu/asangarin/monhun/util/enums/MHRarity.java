package eu.asangarin.monhun.util.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.TranslatableText;

@Getter
@RequiredArgsConstructor
public enum MHRarity {
	RARE_0(0x808080), RARE_1(0xE1DADB), RARE_2(0xB592F7), RARE_3(0xE7D763), RARE_4(0xE7D763), RARE_5(0x73C773), RARE_6(0x738EFF), RARE_7(0xDE595A);

	private final int color;

	public MutableText asText() {
		return new TranslatableText("item.monhun.rarity.desc", ordinal()).setStyle(Style.EMPTY.withColor(color));
	}

	public static MHRarity getFromValue(int value) {
		for (MHRarity texture : MHRarity.values())
			if (texture.name().equalsIgnoreCase("RARE_" + value)) return texture;
		return MHRarity.RARE_1;
	}
}
