package eu.asangarin.monhun.util.enums;

public enum MHMonsterStatuses {
	POISON, PARALYSIS, SLEEP, EXHAUST, KO, BLAST;

	public static MHMonsterStatuses fromString(String key) {
		for(MHMonsterStatuses status : MHMonsterStatuses.values())
			if(status.name().equalsIgnoreCase(key)) return status;
		return null;
	}
}
