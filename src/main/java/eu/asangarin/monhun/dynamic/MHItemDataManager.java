package eu.asangarin.monhun.dynamic;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MHItemDataManager {
	private static final Map<String, MHCachedItemData> registered_items = new HashMap<>();
	private static final MHCachedItemData default_data = new MHCachedItemData();

	public static MHCachedItemData getData(String key) {
		return registered_items.getOrDefault(key, default_data);
	}

	public static void clear() {
		registered_items.clear();
	}

	public static void add(String key, MHCachedItemData data) {
		registered_items.put(key, data);
	}

	public static Set<String> getDataKeys() {
		return registered_items.keySet();
	}
}
