package eu.asangarin.monhun.util.enums;

public enum MHPartType {
	HEAD,
	NECK,
	BACK,
	STOMACH,
	TAIL,
	WINGS,
	LEGS,
	NO_TYPE;

	public static MHPartType fromString(String key) {
		for(MHPartType type : MHPartType.values())
			if(type.name().equalsIgnoreCase(key)) return type;
		return MHPartType.NO_TYPE;
	}
}
