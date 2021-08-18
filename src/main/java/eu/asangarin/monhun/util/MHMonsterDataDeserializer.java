package eu.asangarin.monhun.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import eu.asangarin.monhun.monsters.data.MHMonsterData;

import java.lang.reflect.Type;

public class MHMonsterDataDeserializer implements JsonDeserializer<MHMonsterData> {
	@Override
	public MHMonsterData deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
		try {
			return new MHMonsterData(json.getAsJsonObject());
		} catch (Exception e) {
			return null;
		}
	}
}
