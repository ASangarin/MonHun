package eu.asangarin.monhun.dynamic;

import lombok.Getter;
import lombok.Setter;

@Setter
public class MHItemData {
	@Getter
	private boolean account = false;
	private int buy_value = 0;
	private int sell_value = 0;
	private int value = 100;
	private int stack_size = 64;

	public MHCachedItemData cached() {
		return new MHCachedItemData(this);
	}

	public int getBuyValue() {
		if (buy_value != 0) return buy_value;
		return value;
	}

	public int getSellValue() {
		if (sell_value != 0) return sell_value;
		return value;
	}

	public int getStackSize() {
		return stack_size;
	}
}
