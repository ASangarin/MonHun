package eu.asangarin.monhun.client.dynamic;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MHItemDisplayManager {
	private static final Map<String, MHCachedItemDisplay> registered_items = new HashMap<>();
	private static final MHCachedItemDisplay default_display = new MHCachedItemDisplay();

	public static MHCachedItemDisplay getDisplay(String key) {
		return registered_items.getOrDefault(key, default_display);
	}

	public static void clear() {
		registered_items.clear();
	}

	public static void add(String key, MHCachedItemDisplay display) {
		registered_items.put(key, display);
	}

	public static Set<Map.Entry<String, MHCachedItemDisplay>> getAll() {
		return registered_items.entrySet();
	}
}
