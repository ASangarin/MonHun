package eu.asangarin.monhun.managers;

import eu.asangarin.monhun.MonHun;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.registry.Registry;

import java.lang.reflect.Field;

@SuppressWarnings("unused")
public class MHSounds {
	public static final SoundEvent SILENCE = new SoundEvent(MonHun.i("silence"));

	public static final SoundEvent CONSUME_ITEM = new SoundEvent(MonHun.i("consume_item"));
	public static final SoundEvent GAIN_EFFECT = new SoundEvent(MonHun.i("gain_effect"));
	public static final SoundEvent HORN = new SoundEvent(MonHun.i("horn"));
	public static final SoundEvent MINE_ORE = new SoundEvent(MonHun.i("mine_ore"));
	public static final SoundEvent SONIC_BOMB = new SoundEvent(MonHun.i("sonic_bomb"));
	public static final SoundEvent FLASH_BOMB = new SoundEvent(MonHun.i("flash_bomb"));
	public static final SoundEvent TRAP_SETUP = new SoundEvent(MonHun.i("trap_setup"));
	public static final SoundEvent WHETSTONE = new SoundEvent(MonHun.i("whetstone"));

	public static final SoundEvent BOMB_EXPLODE = new SoundEvent(MonHun.i("bomb_explode"));
	public static final SoundEvent BOMB_FUSE = new SoundEvent(MonHun.i("bomb_fuse"));

	public static final SoundEvent THUNDERBLIGHT = new SoundEvent(MonHun.i("thunderblight"));
	public static final SoundEvent ICEBLIGHT = new SoundEvent(MonHun.i("iceblight"));
	public static final SoundEvent FIREBLIGHT = new SoundEvent(MonHun.i("fireblight"));
	public static final SoundEvent DRAGONBLIGHT = new SoundEvent(MonHun.i("dragonblight"));
	public static final SoundEvent WATERBLIGHT = new SoundEvent(MonHun.i("waterblight"));

	public static final SoundEvent PARALYSIS_LOOP = new SoundEvent(MonHun.i("paralysis_loop"));
	public static final SoundEvent PARALYSIS_END = new SoundEvent(MonHun.i("paralysis_end"));

	public static final SoundEvent BUTTERFLY_PLACE = new SoundEvent(MonHun.i("butterfly_place"));
	public static final SoundEvent BUTTERFLY_HURT = new SoundEvent(MonHun.i("butterfly_hurt"));
	public static final SoundEvent BUTTERFLY_DEATH = new SoundEvent(MonHun.i("butterfly_death"));

	public static void onInitialize() {
		for (Field f : MHSounds.class.getDeclaredFields()) {
			try {
				if (!SoundEvent.class.isAssignableFrom(f.getType())) continue;
				Registry.register(Registry.SOUND_EVENT, MonHun.i(f.getName().toLowerCase()), (SoundEvent) f.get(null));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
}
