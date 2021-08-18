package eu.asangarin.monhun.item;

import eu.asangarin.monhun.MonHun;
import eu.asangarin.monhun.hax.MHHaxxedFactory;
import eu.asangarin.monhun.util.enums.MHRarity;
import eu.asangarin.monhun.util.enums.MHWeaponType;
import eu.asangarin.monhun.util.interfaces.IMHModelItem;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public abstract class MHBaseModelItem extends MHBaseItem implements IAnimatable, IMHModelItem {
	protected final MHWeaponType type;

	public MHBaseModelItem(MHWeaponType type, MHRarity rarity, int maxCount, ItemGroup group) {
		super(type.getTexture(), rarity.getColor(), rarity, maxCount, group);
		this.type = type;
	}

	public Identifier getAnimationFile() {
		return MonHun.i("animations/none.animation.json");
	}

	public abstract String getResourcePath();

	public void registerControllers(AnimationData animationData) {
	}

	public AnimationFactory getFactory() {
		return MHHaxxedFactory.HAXXED_FACTORY;
	}

	@Override
	public String getModelName() {
		return type.getName() + "_gui";
	}

	public String getType() {
		return type.getName();
	}

	public boolean shouldRender(ModelTransformation.Mode mode) {
		return mode == ModelTransformation.Mode.GUI;// || mode == ModelTransformation.Mode.FIXED;
	}
}
