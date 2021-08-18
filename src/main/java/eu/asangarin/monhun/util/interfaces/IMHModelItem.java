package eu.asangarin.monhun.util.interfaces;

import net.minecraft.client.render.model.json.ModelTransformation;

public interface IMHModelItem {
	String getModelName();
	boolean shouldRender(ModelTransformation.Mode mode);

	default boolean renderNormal() {
		return false;
	}
}
