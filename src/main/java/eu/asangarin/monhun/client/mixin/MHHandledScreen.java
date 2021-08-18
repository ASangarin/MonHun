package eu.asangarin.monhun.client.mixin;

import eu.asangarin.monhun.MonHun;
import eu.asangarin.monhun.util.interfaces.ICustomSlotHighlight;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(HandledScreen.class)
public class MHHandledScreen {
	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;drawSlotHighlight(Lnet/minecraft/client/util/math/MatrixStack;III)V"))
	private void drawSlotHighlight(MatrixStack matrices, int x, int y, int z) {
		if (MonHun.getConfig().getClient().getRendering().isUseCustomSlotHighlight()
				&& this instanceof ICustomSlotHighlight customSlotHighlight) {
			customSlotHighlight.drawCustomSlotHighlight(matrices, x, y, z);
		} else HandledScreen.drawSlotHighlight(matrices, x, y, z);
	}
}
