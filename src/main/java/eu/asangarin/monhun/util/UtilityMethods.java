package eu.asangarin.monhun.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class UtilityMethods {
	private static final Map<Class<? extends Enum<?>>, List<? extends Enum<?>>> CACHE = new HashMap<>();
	private static final Random RANDOM = new Random();

	@SuppressWarnings("unchecked")
	public static <T extends Enum<T>> T getRandomEnum(Class<T> enumClass) {
		checkCache(enumClass);
		List<? extends Enum<?>> values = CACHE.get(enumClass);
		return (T) values.get(RANDOM.nextInt(values.size()));
	}

	@SuppressWarnings("unchecked")
	public static <T extends Enum<T>> T cycleEnum(Class<T> enumClass, T current) {
		checkCache(enumClass);
		List<? extends Enum<?>> values = CACHE.get(enumClass);
		int next = current.ordinal() + 1;
		if (next >= values.size()) return (T) values.get(0);
		return (T) values.get(next);
	}

	private static <T extends Enum<T>> void checkCache(Class<T> enumClass) {
		if (!CACHE.containsKey(enumClass)) CACHE.put(enumClass, List.of(enumClass.getEnumConstants()));
	}

	public static void paintballMark(LivingEntity entity) {
		World world = entity.world;
		Team team = world.getScoreboard().getTeam("paintball_marked");
		if (team == null) team = world.getScoreboard().addTeam("paintball_marked");
		team.setColor(Formatting.LIGHT_PURPLE);
		world.getScoreboard().addPlayerToTeam(entity.getEntityName(), team);
	}
}
