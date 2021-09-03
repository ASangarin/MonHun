package eu.asangarin.monhun.util;

import eu.asangarin.monhun.managers.MHItems;
import eu.asangarin.monhun.managers.MHWeapons;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.item.ItemStack;

@Getter
@RequiredArgsConstructor
public enum ItemIcon {
	WEAPON(new ItemStack(MHWeapons.GS_GUILD)), ARMOR(new ItemStack(MHItems.DUMMY_PLATE)), DECORATION(new ItemStack(MHItems.DUMMY_DECORATION)),
	MYSTERY(new ItemStack(MHItems.GARBAGE));

	private final ItemStack icon;
}
