package eu.asangarin.monhun.client.dynamic;

import eu.asangarin.monhun.util.ItemColors;
import eu.asangarin.monhun.util.enums.MHItemTexture;
import eu.asangarin.monhun.util.enums.MHRarity;
import lombok.Setter;

@Setter
public class MHItemDisplay {
	private String group = "resource", translation_key = "item.monhun.display.default", color = "0xFFFFFF", texture = "question";
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

	public MHDisplayGroup getGroup() {
		return MHDisplayGroup.getFromString(group);
	}

	public MHRarity getRarity() {
		return MHRarity.getFromValue(rarity);
	}

	public MHCachedItemDisplay cached() {
		return new MHCachedItemDisplay(this);
	}
}
