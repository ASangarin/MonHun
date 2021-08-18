package eu.asangarin.monhun.monsters.data;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MHDamageValues {
	public static final MHDamageValues DEFAULT = new MHDamageValues(1, 1, 1, 1, 1, 1, 1, 1);

	private final int cut, impact, shot, fire, water, ice, thunder, dragon;

	public MHDamageValues(JsonObject object) {
		this.cut = object.get("cut").getAsInt();
		this.impact = object.get("impact").getAsInt();
		this.shot = object.get("shot").getAsInt();
		this.fire = object.get("fire").getAsInt();
		this.water = object.get("water").getAsInt();
		this.ice = object.get("ice").getAsInt();
		this.thunder = object.get("thunder").getAsInt();
		this.dragon = object.get("dragon").getAsInt();
	}
}
