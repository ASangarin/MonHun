package eu.asangarin.monhun.client.mixin;

import eu.asangarin.monhun.client.MHModelManager;
import eu.asangarin.monhun.util.enums.MHWeaponType;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(ModelLoader.class)
public abstract class MHModelLoader {
	private boolean hasLoaded = false;

	@Shadow
	public abstract UnbakedModel getOrLoadModel(Identifier id);

	@Shadow
	@Final
	private Map<Identifier, UnbakedModel> unbakedModels;

	@Shadow
	@Final
	private Map<Identifier, UnbakedModel> modelsToBake;

	@Inject(method = "loadModelFromJson", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ResourceManager;getResource(Lnet/minecraft/util/Identifier;)Lnet/minecraft/resource/Resource;"), cancellable = true)
	public void loadModelFromJson(Identifier id, CallbackInfoReturnable<JsonUnbakedModel> cir) {
		if (!id.getNamespace().equals("monhun")) return;

		String json = MHModelManager.getItemJson(id.getPath());
		if (json.isEmpty()) {
			System.out.println("[Model] Ident not registered: " + id);
			return;
		} else if (json.equals("internal")) return;

		JsonUnbakedModel model = JsonUnbakedModel.deserialize(json);
		model.id = id.toString();
		cir.setReturnValue(model);
	}

	@Inject(method = "addModel", at = @At("HEAD"))
	private void addModel(ModelIdentifier modelId, CallbackInfo ci) {
		if (hasLoaded) return;
		addCustomModel(new ModelIdentifier("monhun:binoculars_in_hand#inventory"));
		for(MHWeaponType weaponType : MHWeaponType.values())
			addCustomModel(new ModelIdentifier("monhun:" + weaponType.getName() + "_gui#inventory"));
		hasLoaded = true;
	}

	private void addCustomModel(ModelIdentifier model) {
		UnbakedModel unbakedModel = this.getOrLoadModel(model);
		this.unbakedModels.put(model, unbakedModel);
		this.modelsToBake.put(model, unbakedModel);
	}
}
