package eu.asangarin.monhun.client.model;

import eu.asangarin.monhun.MonHun;
import eu.asangarin.monhun.item.MHBaseModelItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MHItemModel extends AnimatedGeoModel<MHBaseModelItem> {
	@Override
	public Identifier getModelLocation(MHBaseModelItem weapon) {
		return MonHun.i("geo/" + weapon.getResourcePath() + ".geo.json");
	}

	@Override
	public Identifier getTextureLocation(MHBaseModelItem weapon) {
		return MonHun.i("textures/model/" + weapon.getResourcePath() + ".png");
	}

	@Override
	public Identifier getAnimationFileLocation(MHBaseModelItem weapon) {
		return weapon.getAnimationFile();
	}
}