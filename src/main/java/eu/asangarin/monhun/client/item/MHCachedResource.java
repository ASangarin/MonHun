package eu.asangarin.monhun.client.item;

import eu.asangarin.monhun.util.ItemColors;
import eu.asangarin.monhun.util.enums.MHItemTexture;
import eu.asangarin.monhun.util.enums.MHRarity;
import lombok.Getter;

@Getter
public class MHCachedResource {
	private final String translationKey;
	private final int color;
	private final MHItemTexture texture;
	private final MHRarity rarity;

	public MHCachedResource() {
		translationKey = "item.monhun.resource.default";
		color = ItemColors.GRAY;
		texture = MHItemTexture.QUESTION;
		rarity = MHRarity.RARE_0;
	}

	public MHCachedResource(MHItemResource resource) {
		translationKey = resource.getTranslationKey();
		color = resource.getColor();
		texture = resource.getTexture();
		rarity = resource.getRarity();
	}
}
