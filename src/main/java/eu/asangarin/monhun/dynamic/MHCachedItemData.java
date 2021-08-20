package eu.asangarin.monhun.dynamic;

import lombok.Getter;

@Getter
public class MHCachedItemData {
	private final boolean account;
	private final int buyValue, sellValue;
	private final int stackSize;

	public MHCachedItemData() {
		account = false;
		buyValue = 100;
		sellValue = 10;
		stackSize = 64;
	}

	public MHCachedItemData(MHItemData data) {
		account = data.isAccount();
		buyValue = data.getBuyValue();
		sellValue = data.getSellValue();
		stackSize = data.getStackSize();
	}
}
