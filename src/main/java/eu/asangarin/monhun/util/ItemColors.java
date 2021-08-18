package eu.asangarin.monhun.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ItemColors {
	public static final Map<String, Integer> COLORS = new HashMap<>();

	// Common Colors
	public static final int GREEN = 0x6BBE84;
	public static final int LIGHT_BLUE = 0xA5E3F7;
	public static final int BLUE = 0xA5BEFF;
	public static final int YELLOW = 0xFFD36B;
	public static final int RED = 0xDF4A5F;
	public static final int ORANGE = 0xF49C61;
	public static final int WHITE = 0xF7F7F7;
	public static final int GRAY = 0x8C8A8C;
	public static final int PURPLE = 0xCEA6D6;
	public static final int PINK = 0xF8879D;
	public static final int BROWN = 0xFECF6A;

	// Unique Colors
	public static final int LIME = 0xB5D773;
	public static final int CYAN = 0x54EDCE;
	public static final int DARK_GREEN = 0x508B37;
	public static final int BLACK = 0x444444;

	// Monster Colors
	public static final int RATHALOS = 0xE75163;

	public static int getColor(String s) {
		return COLORS.getOrDefault(s.toLowerCase(), WHITE);
	}

	static {
		for (Field f : ItemColors.class.getDeclaredFields()) {
			try {
				if (!int.class.isAssignableFrom(f.getType())) continue;
				COLORS.put(f.getName().toLowerCase(), (int) f.get(null));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
}
