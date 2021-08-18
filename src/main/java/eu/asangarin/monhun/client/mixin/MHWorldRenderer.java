package eu.asangarin.monhun.client.mixin;

import eu.asangarin.monhun.util.interfaces.INoBox;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class MHWorldRenderer {
	@Inject(method = "drawBlockOutline", at = @At("HEAD"), cancellable = true)
	private void drawBlockOutline(MatrixStack matrices, VertexConsumer vertexConsumer, Entity entity, double d, double e, double f, BlockPos blockPos, BlockState blockState, CallbackInfo ci) {
		if(entity instanceof PlayerEntity player && player.getAbilities().creativeMode) return;
		if (blockState.getBlock() instanceof INoBox) ci.cancel();
	}
}
