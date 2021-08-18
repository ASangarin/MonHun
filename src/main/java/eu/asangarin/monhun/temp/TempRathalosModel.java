package eu.asangarin.monhun.temp;

import eu.asangarin.monhun.MonHun;
import eu.asangarin.monhun.entity.MHMonsterEntity;
import lombok.RequiredArgsConstructor;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@RequiredArgsConstructor
public class TempRathalosModel extends AnimatedGeoModel<MHMonsterEntity> {
	private final String name;

	@Override
	public Identifier getModelLocation(MHMonsterEntity object) {
		return MonHun.i("geo/monster/" + name + ".geo.json");
	}

	@Override
	public Identifier getTextureLocation(MHMonsterEntity object) {
		return MonHun.i("textures/monster/" + name + ".png");
	}

	@Override
	public Identifier getAnimationFileLocation(MHMonsterEntity object) {
		return MonHun.i("animations/monster/" + name + ".animation.json");
	}
}
