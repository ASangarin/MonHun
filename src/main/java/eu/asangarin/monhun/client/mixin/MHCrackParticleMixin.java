package eu.asangarin.monhun.client.mixin;

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.client.particle.CrackParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrackParticle.class)
public class MHCrackParticleMixin {
	@Inject(method = "<init>(Lnet/minecraft/client/world/ClientWorld;DDDLnet/minecraft/item/ItemStack;)V", at = @At("TAIL"))
	private void injected(ClientWorld world, double x, double y, double z, ItemStack stack, CallbackInfo ci) {
		ItemColorProvider colorProvider = ColorProviderRegistry.ITEM.get(stack.getItem());
		if (colorProvider != null) {
			int color = colorProvider.getColor(stack, 0);
			float r = (float) (color >> 16 & 255) / 255.0F;
			float g = (float) (color >> 8 & 255) / 255.0F;
			float b = (float) (color & 255) / 255.0F;
			((CrackParticle) (Object) this).setColor(r, g, b);
		}
	}
}
