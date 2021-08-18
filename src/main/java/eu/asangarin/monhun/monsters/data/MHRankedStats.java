package eu.asangarin.monhun.monsters.data;

import com.google.gson.JsonObject;
import lombok.Getter;

@Getter
public class MHRankedStats {
	private final MHMonsterDropsData drops;
	private final int health;
	private final int limpPercent;
	private final int capturePercent;

	public MHRankedStats(JsonObject stats, JsonObject drops) {
		this.health = stats.get("health").getAsInt();
		this.limpPercent = stats.get("limp").getAsInt();
		this.capturePercent = stats.get("capture").getAsInt();
		this.drops = new MHMonsterDropsData(drops);
	}
}
