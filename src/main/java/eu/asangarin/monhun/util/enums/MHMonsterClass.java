package eu.asangarin.monhun.util.enums;

public enum MHMonsterClass {
	AMPHIBIAN, BIRD_WYVERN, BRUTE_WYVERN, CARAPACEON, ELDER_DRAGON, FANGED_BEAST, FANGED_WYVERN, FLYING_WYVERN, HERBIVORE, LEVIATHAN,
	PISCINE_WYVERN, TEMNOCERAN, MYSTERY;

	public String toIdentifier() {
		return name().toLowerCase();
	}

	public static MHMonsterClass getFromString(String value) {
		for (MHMonsterClass clazz : MHMonsterClass.values())
			if (clazz.name().equalsIgnoreCase(value)) return clazz;
		return MHMonsterClass.MYSTERY;
	}
}
