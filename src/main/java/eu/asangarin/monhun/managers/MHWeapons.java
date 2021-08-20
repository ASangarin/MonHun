package eu.asangarin.monhun.managers;

import eu.asangarin.monhun.item.MHBaseItem;
import eu.asangarin.monhun.item.MHBlademasterItem;
import eu.asangarin.monhun.util.enums.MHRarity;
import eu.asangarin.monhun.util.enums.MHWeaponType;

@SuppressWarnings("unused")
public class MHWeapons {
	// Great Sword
	public static final MHBaseItem GS_GUILD = new MHBlademasterItem("guild", MHWeaponType.GREAT_SWORD, MHRarity.RARE_2, 10);

	// Hammer
	public static final MHBaseItem HM_GUILD = new MHBlademasterItem("guild", MHWeaponType.HAMMER, MHRarity.RARE_6, 21);
}
