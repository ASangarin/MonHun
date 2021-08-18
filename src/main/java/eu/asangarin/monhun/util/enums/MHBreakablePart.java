package eu.asangarin.monhun.util.enums;

public enum MHBreakablePart {
	HEAD,
	WING,
	BACK,
	UNSPECIFIED;

	public static MHBreakablePart fromString(String key) {
		for(MHBreakablePart part : MHBreakablePart.values())
			if(part.name().equalsIgnoreCase(key)) return part;
		return MHBreakablePart.UNSPECIFIED;
	}
}
