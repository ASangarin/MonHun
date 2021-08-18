package eu.asangarin.monhun.client.item;

import eu.asangarin.monhun.util.ItemColors;
import eu.asangarin.monhun.util.enums.MHItemTexture;
import eu.asangarin.monhun.util.enums.MHRarity;
import lombok.Setter;

@Setter
public class MHItemResource {
	private String translation_key = "item.monhun.resource.default", color = "0xFFFFFF", texture = "question";
	private int rarity = 1;

	public String getTranslationKey() {
		return translation_key;
	}

	public int getColor() {
		try {
			return Integer.parseInt(color.replace("0x", ""), 16);
		} catch (NumberFormatException e) {
			return ItemColors.getColor(color);
		}
	}

	public MHItemTexture getTexture() {
		return MHItemTexture.getFromString(texture);
	}

	public MHRarity getRarity() {
		return MHRarity.getFromValue(rarity);
	}

	public MHCachedResource cached() {
		return new MHCachedResource(this);
	}
}
