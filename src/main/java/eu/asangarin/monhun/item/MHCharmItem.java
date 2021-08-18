package eu.asangarin.monhun.item;

import eu.asangarin.monhun.MonHun;
import eu.asangarin.monhun.util.enums.MHItemTexture;
import eu.asangarin.monhun.util.enums.MHRarity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MHCharmItem extends MHBaseItem {
	private final boolean power, charm;

	public MHCharmItem(MHItemTexture texture, int color, boolean power, boolean charm) {
		super(texture, color, MHRarity.RARE_5, 1, MonHun.TOOL_GROUP);
		this.power = power;
		this.charm = charm;
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
	}

	public boolean isPowerCharm() {
		return power && charm;
	}

	public boolean isArmorCharm() {
		return !power && charm;
	}

	public boolean isPowerTalon() {
		return power && !charm;
	}

	public boolean isArmorTalon() {
		return !power && !charm;
	}
}
