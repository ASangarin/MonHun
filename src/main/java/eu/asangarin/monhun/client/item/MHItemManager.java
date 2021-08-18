package eu.asangarin.monhun.client.item;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MHItemManager {
	private static final Map<String, MHCachedResource> registered_items = new HashMap<>();
	private static final MHCachedResource default_resource = new MHCachedResource();

	public static MHCachedResource getResource(String key) {
		return registered_items.getOrDefault(key, default_resource);
	}

	public static void clear() {
		registered_items.clear();
	}

	public static void add(String key, MHCachedResource resource) {
		registered_items.put(key, resource);
	}

	public static Set<String> getResourceKeys() {
		return registered_items.keySet();
	}
}
