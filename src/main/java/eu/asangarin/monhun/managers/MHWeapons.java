package eu.asangarin.monhun.managers;

import eu.asangarin.monhun.MonHun;
import eu.asangarin.monhun.item.MHBaseItem;
import eu.asangarin.monhun.item.MHBlademasterItem;
import eu.asangarin.monhun.util.enums.MHRarity;
import eu.asangarin.monhun.util.enums.MHWeaponType;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

import java.lang.reflect.Field;

@SuppressWarnings("unused")
public class MHWeapons {
	// Great Sword
	public static final MHBaseItem GS_GUILD = new MHBlademasterItem("guild", MHWeaponType.GREAT_SWORD, MHRarity.RARE_2, 10);

	// Hammer
	public static final MHBaseItem HM_GUILD = new MHBlademasterItem("guild", MHWeaponType.HAMMER, MHRarity.RARE_6, 21);

	public static void onInitialize() {
		for (Field f : MHWeapons.class.getDeclaredFields()) {
			try {
				if (!Item.class.isAssignableFrom(f.getType())) continue;
				Registry.register(Registry.ITEM, MonHun.i(f.getName().toLowerCase()), (Item) f.get(null));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
}
