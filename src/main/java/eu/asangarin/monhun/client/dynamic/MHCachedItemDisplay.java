package eu.asangarin.monhun.client.dynamic;

import eu.asangarin.monhun.util.ItemColors;
import eu.asangarin.monhun.util.enums.MHItemTexture;
import eu.asangarin.monhun.util.enums.MHRarity;
import lombok.Getter;

@Getter
public class MHCachedItemDisplay {
	private final String translationKey;
	private final int color;
	private final MHDisplayGroup group;
	private final MHItemTexture texture;
	private final MHRarity rarity;

	public MHCachedItemDisplay() {
		translationKey = "item.monhun.dynamic.default";
		color = ItemColors.GRAY;
		group = MHDisplayGroup.RESOURCE;
		texture = MHItemTexture.QUESTION;
		rarity = MHRarity.RARE_0;
	}

	public MHCachedItemDisplay(MHItemDisplay display) {
		translationKey = display.getTranslationKey();
		color = display.getColor();
		group = display.getGroup();
		texture = display.getTexture();
		rarity = display.getRarity();
	}
}
