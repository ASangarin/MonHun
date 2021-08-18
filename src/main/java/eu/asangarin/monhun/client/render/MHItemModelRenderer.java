package eu.asangarin.monhun.client.render;

import eu.asangarin.monhun.client.model.MHItemModel;
import eu.asangarin.monhun.item.MHBaseModelItem;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class MHItemModelRenderer extends GeoItemRenderer<MHBaseModelItem> {
	public MHItemModelRenderer() {
		super(new MHItemModel());
	}
}
