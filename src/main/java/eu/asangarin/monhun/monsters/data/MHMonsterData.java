package eu.asangarin.monhun.monsters.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import eu.asangarin.monhun.util.enums.MHMonsterStatuses;
import eu.asangarin.monhun.util.enums.MHPartType;
import eu.asangarin.monhun.util.enums.MHRank;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class MHMonsterData {
	private final Map<MHRank, MHRankedStats> stats = new HashMap<>();
	private final Map<MHPartType, MHDamageValues> hitzones = new HashMap<>();
	private final Map<MHMonsterStatuses, MHStatusTolerance> tolerance = new HashMap<>();
	private final int rageDuration;
	private final double rageAttack, rageDefense;

	public MHDamageValues getValues(MHPartType type) {
		return hitzones.getOrDefault(type, MHDamageValues.DEFAULT);
	}

	public MHMonsterData(JsonObject object) {
		JsonObject stats = object.getAsJsonObject("stats");
		JsonObject drops = object.getAsJsonObject("drops");
		this.stats.put(MHRank.LOW, new MHRankedStats(stats.getAsJsonObject("low"), drops.getAsJsonObject("low")));
		this.stats.put(MHRank.HIGH, new MHRankedStats(stats.getAsJsonObject("high"), drops.getAsJsonObject("high")));
		this.stats.put(MHRank.G, new MHRankedStats(stats.getAsJsonObject("g"), drops.getAsJsonObject("g")));
		JsonObject hitzones = object.getAsJsonObject("hitzones");
		for (Map.Entry<String, JsonElement> json : hitzones.entrySet())
			this.hitzones.put(MHPartType.fromString(json.getKey()), new MHDamageValues(json.getValue().getAsJsonObject()));
		JsonObject tolerance = object.getAsJsonObject("tolerance");
		for (Map.Entry<String, JsonElement> json : tolerance.entrySet())
			this.tolerance.put(MHMonsterStatuses.fromString(json.getKey()), new MHStatusTolerance(json.getValue().getAsJsonObject()));

		JsonObject enraged = stats.getAsJsonObject("enraged");
		rageDuration = enraged.get("duration").getAsInt();
		rageAttack = enraged.get("attack").getAsDouble();
		rageDefense = enraged.get("defense").getAsDouble();
	}
}
