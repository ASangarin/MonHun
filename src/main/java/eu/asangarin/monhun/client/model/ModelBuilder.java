package eu.asangarin.monhun.client.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.client.model.Model;

import java.util.Map.Entry;
import java.util.Set;

public class ModelBuilder {
	private final JsonObject model = new JsonObject();
	private final JsonObject textures = new JsonObject();
	private final JsonArray overrides = new JsonArray();

	public ModelBuilder(String parent) {
		model.addProperty("parent", parent);
	}

	public ModelBuilder addTexture(String texPath, String value) {
		textures.addProperty(texPath, "monhun:" + value);
		return this;
	}

	public ModelBuilder addPredicate(String key, float value, String model) {
		return addVanillaPredicate("monhun:" + key, value, model);
	}

	public ModelBuilder addVanillaPredicate(String key, float value, String model) {
		final JsonObject json = new JsonObject();
		final JsonObject predicate = new JsonObject();
		predicate.addProperty(key, value);
		json.add("predicate", predicate);
		json.addProperty("model", "monhun:" + model);
		overrides.add(json);
		return this;
	}

	public ModelBuilder addPredicate(Set<Entry<String, Float>> predicates, String model) {
		final JsonObject json = new JsonObject();
		final JsonObject predicate = new JsonObject();
		for (Entry<String, Float> entry : predicates)
			predicate.addProperty(entry.getKey(), entry.getValue());
		json.add("predicate", predicate);
		json.addProperty("model", "monhun:" + model);
		overrides.add(json);
		return this;
	}

	public ModelBuilder addManualPredicate(JsonObject predicate, String model) {
		final JsonObject json = new JsonObject();
		json.add("predicate", predicate);
		json.addProperty("model", "monhun:" + model);
		overrides.add(json);
		return this;
	}

	public String build() {
		if (textures.size() != 0) model.add("textures", textures);
		if (overrides.size() != 0) model.add("overrides", overrides);
		return model.toString();
	}

	public static ModelBuilder quick(String parent, String texture) {
		return new ModelBuilder(parent).addTexture("layer0", texture);
	}
}
