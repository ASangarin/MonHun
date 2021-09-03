package eu.asangarin.monhun.util.enums;

import lombok.Getter;

@Getter
public enum MHNPCType {
	BLACKSMITH, PROVISIONER;

	public String getTranslationKey() {
		return "entity.monhun.npc." + name().toLowerCase();
	}
}
