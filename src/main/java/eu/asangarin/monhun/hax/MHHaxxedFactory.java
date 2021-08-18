package eu.asangarin.monhun.hax;

import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class MHHaxxedFactory extends AnimationFactory {
	public static final MHHaxxedFactory HAXXED_FACTORY = new MHHaxxedFactory();

	public MHHaxxedFactory() {
		super(null);
	}

	public AnimationData getOrCreateAnimationData(Integer uniqueID) {
		return new AnimationData();
	}
}
