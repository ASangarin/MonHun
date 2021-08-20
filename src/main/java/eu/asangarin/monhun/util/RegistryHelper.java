package eu.asangarin.monhun.util;

import eu.asangarin.monhun.MonHun;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.lang.reflect.Field;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class RegistryHelper {
	@SuppressWarnings("unchecked")
	public static <T> void registerAll(Registry<T> registry, Class<?> clazz, Class<?> type, Function<Identifier, Identifier> preLogic, BiConsumer<T, Identifier> postLogic) {
		for (Field f : clazz.getDeclaredFields()) {
			try {
				if (!type.isAssignableFrom(f.getType())) continue;
				Identifier ident = preLogic.apply(MonHun.i(f.getName().toLowerCase()));
				T object = (T) f.get(null);
				Registry.register(registry, ident, object);
				postLogic.accept(object, ident);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	public static <T> void registerAll(Registry<T> registry, Class<?> clazz, Class<?> type) {
		registerAll(registry, clazz, type, (ident) -> ident, (effect, id) -> {
		});
	}

	public static <T> void registerAll(Registry<T> registry, Class<?> clazz, Class<?> type, BiConsumer<T, Identifier> postLogic) {
		registerAll(registry, clazz, type, (ident) -> ident, postLogic);
	}

	public static <T> void registerAll(Registry<T> registry, Class<?> clazz, Class<?> type, Function<Identifier, Identifier> preLogic) {
		registerAll(registry, clazz, type, preLogic, (effect, id) -> {
		});
	}
}
