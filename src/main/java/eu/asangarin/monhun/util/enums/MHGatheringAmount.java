package eu.asangarin.monhun.util.enums;

import net.minecraft.util.StringIdentifiable;

public enum MHGatheringAmount implements StringIdentifiable {
	ONE, TWO, THREE, FOUR, FIVE, SIX;

	/*public static MHGatheringAmount fromString(String value) {
		for (MHGatheringAmount amount : MHGatheringAmount.values())
			if (amount.asString().equalsIgnoreCase(value)) return amount;
		return MHGatheringAmount.ONE;
	}*/

	public static MHGatheringAmount fromInteger(int remaining) {
		return switch (remaining) {
			case 0, 1 -> ONE;
			case 2 -> TWO;
			case 3 -> THREE;
			case 4 -> FOUR;
			case 5 -> FIVE;
			default -> SIX;
		};
	}

	@Override
	public String asString() {
		return name().toLowerCase();
	}
}
