package eu.asangarin.monhun.client.mixin;

import eu.asangarin.monhun.item.MHBaseItem;
import eu.asangarin.monhun.item.MHBaseModelItem;
import eu.asangarin.monhun.managers.MHItems;
import eu.asangarin.monhun.util.interfaces.IMHModelItem;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BasicBakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemRenderer.class)
public abstract class MHItemRenderer {
	@Shadow
	@Final
	private ItemModels models;
	@Shadow
	@Final
	private BuiltinModelItemRenderer builtinModelItemRenderer;

	@Shadow
	protected abstract void renderBakedItemModel(BakedModel model, ItemStack stack, int light, int overlay, MatrixStack matrices, VertexConsumer vertices);

	@Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At("HEAD"), cancellable = true)
	private void renderItem(ItemStack stack, ModelTransformation.Mode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo info) {
		if (!stack.isEmpty() && stack.getItem() instanceof MHBaseItem item && item instanceof IMHModelItem modelItem) {
			boolean bl = modelItem.shouldRender(renderMode);
			if (bl) model = this.models.getModelManager().getModel(new ModelIdentifier("monhun:" + modelItem.getModelName() + "#inventory"));
			else if(modelItem.renderNormal()) return;
			matrices.push();

			model.getTransformation().getTransformation(renderMode).apply(leftHanded, matrices);
			matrices.translate(-0.5D, -0.5D, -0.5D);
			if (!model.isBuiltin() && bl) {
				RenderLayer renderLayer = RenderLayers.getItemLayer(stack, true);
				this.renderBakedItemModel(model, stack, light, overlay, matrices,
						ItemRenderer.getDirectItemGlintConsumer(vertexConsumers, renderLayer, true, stack.hasGlint()));
			} else {
				this.builtinModelItemRenderer.render(stack, renderMode, matrices, vertexConsumers, light, overlay);
			}

			matrices.pop();
			info.cancel();
		}
	}
}
