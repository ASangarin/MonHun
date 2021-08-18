package eu.asangarin.monhun.temp;

import eu.asangarin.monhun.entity.MHMonsterEntity;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class TempRathalosRenderer extends GeoEntityRenderer<MHMonsterEntity> {
	public TempRathalosRenderer(EntityRendererFactory.Context ctx, String name) {
		super(ctx, new TempRathalosModel(name));
		this.shadowRadius = 2f;
	}

	public void renderLate(MHMonsterEntity animatable, MatrixStack stackIn, float ticks, VertexConsumerProvider renderTypeBuffer, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
		super.renderLate(animatable, stackIn, ticks, renderTypeBuffer, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
		stackIn.scale(2.6f, 2.6f, 2.6f);
		stackIn.translate(0.0f, 0.0f, 0.0f);
	}
}
