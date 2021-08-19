package eu.asangarin.monhun.client.render;

import eu.asangarin.monhun.MonHun;
import eu.asangarin.monhun.block.MHGatheringBlock;
import eu.asangarin.monhun.block.entity.MHBugBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class MHBugBlockEntityRenderer extends GeoBlockRenderer<MHBugBlockEntity> {
	private final static Identifier WHITE_TEXTURE = MonHun.i("textures/block/bug_white.png");
	private final static Identifier BLACK_TEXTURE = MonHun.i("textures/block/bug_black.png");
	private final static Identifier BLUE_TEXTURE = MonHun.i("textures/block/bug_blue.png");
	private final static Identifier RED_TEXTURE = MonHun.i("textures/block/bug_red.png");
	private final static Identifier GOLD_TEXTURE = MonHun.i("textures/block/bug_gold.png");

	public MHBugBlockEntityRenderer() {
		super(new AnimatedGeoModel<>() {
			@Override
			public Identifier getModelLocation(MHBugBlockEntity blockEntity) {
				return MonHun.i("geo/block/flying_bug.geo.json");
			}

			@Override
			public Identifier getTextureLocation(MHBugBlockEntity blockEntity) {
				return WHITE_TEXTURE;
			}

			@Override
			public Identifier getAnimationFileLocation(MHBugBlockEntity blockEntity) {
				return MonHun.i("animations/flying_bug.animation.json");
			}
		});
	}

	@Override
	public void render(MHBugBlockEntity tile, float partialTicks, MatrixStack stack, VertexConsumerProvider bufferIn, int packedLightIn) {
		GeoModel model = getGeoModelProvider().getModel(getGeoModelProvider().getModelLocation(tile));
		getGeoModelProvider().setLivingAnimations(tile, this.getUniqueID(tile));
		stack.push();
		stack.translate(0, 0.01f, 0);
		stack.translate(0.5f, 0.0f, 0.5f);

		Identifier texId = getBugTexture(tile.getCachedState());
		MinecraftClient.getInstance().getTextureManager().bindTexture(texId);
		RenderLayer renderType = getRenderType(tile, partialTicks, stack, bufferIn, null, packedLightIn, texId);
		render(model, tile, partialTicks, renderType, stack, bufferIn, null, packedLightIn, getOverlayTexture(tile.getHurtTime()), 1.0f, 1.0f, 1.0f,
				1.0f);
		stack.pop();
	}

	private int getOverlayTexture(int hurtTime) {
		return OverlayTexture.packUv(OverlayTexture.getU(0), OverlayTexture.getV(hurtTime > 0));
	}

	private Identifier getBugTexture(BlockState state) {
		return switch (state.get(MHGatheringBlock.GATHERING_TYPE)) {
			case WHITE -> WHITE_TEXTURE;
			case RED -> RED_TEXTURE;
			case BLUE -> BLUE_TEXTURE;
			case GOLD -> GOLD_TEXTURE;
			case BLACK -> BLACK_TEXTURE;
		};
	}
}