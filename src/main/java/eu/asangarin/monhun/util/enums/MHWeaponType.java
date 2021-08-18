package eu.asangarin.monhun.util.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MHWeaponType {
	GREAT_SWORD("gs", MHItemTexture.GREAT_SWORD, 0, 0),
	SWORD_AND_SHIELD("sns", MHItemTexture.SWORD_AND_SHIELD, 0, 0),
	DUAL_BLADES("db", MHItemTexture.DUAL_BLADES, 0, 0),
	HAMMER("hm", MHItemTexture.HAMMER, 0, 0),
	LANCE("la", MHItemTexture.LANCE, 0, 0),
	LIGHT_BOWGUN("lbg", MHItemTexture.LIGHT_BOWGUN, 0, 0),
	HEAVY_BOWGUN("hbg", MHItemTexture.HEAVY_BOWGUN, 0, 0);

	private final String id;
	private final MHItemTexture texture;
	private final double attackSpeed, moveSpeed;

	public String getName() {
		return name().toLowerCase();
	}
}
