package eu.asangarin.monhun.client.render.entity;

import eu.asangarin.monhun.MonHun;
import eu.asangarin.monhun.entity.MHNPCEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class MHNPCEntityRenderer extends LivingEntityRenderer<MHNPCEntity, PlayerEntityModel<MHNPCEntity>> {
	private static final Identifier BLACKSMITH_TEXTURE = MonHun.i("textures/entity/blacksmith.png");
	private static final Identifier PROVISIONER_TEXTURE = MonHun.i("textures/entity/provisioner.png");

	public MHNPCEntityRenderer(EntityRendererFactory.Context ctx, boolean slim) {
		super(ctx, new PlayerEntityModel<>(ctx.getPart(slim ? EntityModelLayers.PLAYER_SLIM : EntityModelLayers.PLAYER), slim), 0.5F);
		this.addFeature(new NPCHeldItemFeatureRenderer<>(this));
	}

	public void render(MHNPCEntity entity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
		this.setModelPose(entity);
		super.render(entity, f, g, matrixStack, vertexConsumerProvider, i);
	}

	private void setModelPose(MHNPCEntity entity) {
		PlayerEntityModel<MHNPCEntity> playerEntityModel = getModel();
		playerEntityModel.setVisible(true);
		playerEntityModel.rightArmPose = entity.getHeldItem().isEmpty() ? BipedEntityModel.ArmPose.EMPTY : BipedEntityModel.ArmPose.ITEM;
		playerEntityModel.leftArmPose = BipedEntityModel.ArmPose.EMPTY;

	}

	public Identifier getTexture(MHNPCEntity npc) {
		return switch (npc.getNPCType()) {
			case BLACKSMITH -> BLACKSMITH_TEXTURE;
			case PROVISIONER -> PROVISIONER_TEXTURE;
		};
	}

	protected void scale(MHNPCEntity entity, MatrixStack matrixStack, float f) {
		matrixStack.scale(0.9375F, 0.9375F, 0.9375F);
	}

	protected void renderLabelIfPresent(MHNPCEntity entity, Text text, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
		double d = this.dispatcher.getSquaredDistanceToCamera(entity);
		matrixStack.push();
		if (d < 100.0D) {
			Scoreboard scoreboard = entity.world.getScoreboard();
			ScoreboardObjective scoreboardObjective = scoreboard.getObjectiveForSlot(2);
			if (scoreboardObjective != null) {
				ScoreboardPlayerScore scoreboardPlayerScore = scoreboard.getPlayerScore(entity.getEntityName(), scoreboardObjective);
				super.renderLabelIfPresent(entity, (new LiteralText(Integer.toString(scoreboardPlayerScore.getScore()))).append(" ")
						.append(scoreboardObjective.getDisplayName()), matrixStack, vertexConsumerProvider, i);
				Objects.requireNonNull(this.getTextRenderer());
				matrixStack.translate(0.0D, 9.0F * 1.15F * 0.025F, 0.0D);
			}
		}

		super.renderLabelIfPresent(entity, text, matrixStack, vertexConsumerProvider, i);
		matrixStack.pop();
	}

	protected void setupTransforms(MHNPCEntity entity, MatrixStack matrixStack, float f, float g, float h) {
		float i = entity.getLeaningPitch(h);
		super.setupTransforms(entity, matrixStack, f, g, h);
		if (i > 0.0F) {
			float n = entity.isTouchingWater() ? -90.0F - entity.getPitch() : -90.0F;
			matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(MathHelper.lerp(i, 0.0F, n)));
		}
	}

	@Environment(EnvType.CLIENT)
	public static class NPCHeldItemFeatureRenderer<T extends MHNPCEntity, M extends EntityModel<T> & ModelWithArms> extends HeldItemFeatureRenderer<T, M> {
		public NPCHeldItemFeatureRenderer(FeatureRendererContext<T, M> featureRendererContext) {
			super(featureRendererContext);
		}

		public void render(MatrixStack matrixStack, VertexConsumerProvider vertex, int i, T entity, float f, float g, float h, float j, float k, float l) {
			ItemStack stack = entity.getHeldItem();
			if (!stack.isEmpty()) {
				matrixStack.push();
				this.renderItem(entity, stack, ModelTransformation.Mode.THIRD_PERSON_RIGHT_HAND, Arm.RIGHT, matrixStack, vertex, i);
				matrixStack.pop();
			}
		}
	}
}
