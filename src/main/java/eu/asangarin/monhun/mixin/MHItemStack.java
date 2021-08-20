package eu.asangarin.monhun.mixin;

import eu.asangarin.monhun.item.MHDynamicItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class MHItemStack {
	@Shadow
	public abstract Item getItem();

	@Inject(method = "getMaxCount", at = @At("HEAD"), cancellable = true)
	public void getMaxCount(CallbackInfoReturnable<Integer> cir) {
		if (getItem() instanceof MHDynamicItem dynamicItem) cir.setReturnValue(dynamicItem.getStackSize((ItemStack) (Object) this));
	}
}
