package eu.asangarin.monhun.monsters;

import eu.asangarin.monhun.MonHun;
import eu.asangarin.monhun.monsters.data.MHMonsterData;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MHMonsterManager {
	private static final Map<String, MHMonsterData> monsterData = new HashMap<>();

	public static MHMonsterData getData(Identifier key) {
		return monsterData.get(key.toString());
	}

	public static MHMonsterData getData(String key) {
		return monsterData.get("monhun:" + key);
	}

	public static void clear() {
		monsterData.clear();
	}

	public static Set<String> getDataKeys() {
		return monsterData.keySet();
	}

	public static void add(String key, MHMonsterData data) {
		if (data == null) {
			MonHun.log("Invalid data for '%s'", key);
			return;
		}
		monsterData.put(key, data);
	}
}
