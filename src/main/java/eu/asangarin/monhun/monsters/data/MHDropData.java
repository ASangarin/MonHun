package eu.asangarin.monhun.monsters.data;

import eu.asangarin.monhun.util.compare.IIntegerComparable;
import lombok.Getter;
import net.minecraft.item.ItemStack;

public class MHDropData implements IIntegerComparable {
	private final ItemStack drop;
	@Getter
	private final double chance;

	private final int rawChance;

	public MHDropData(ItemStack stack, int chance) {
		this.drop = stack;
		this.rawChance = chance;
		this.chance = ((double) chance / (double) 100);
	}

	@Override
	public int getComparableInt() {
		return rawChance;
	}

	public ItemStack getItem() {
		return drop.copy();
	}
}
