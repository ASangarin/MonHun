package eu.asangarin.monhun.client.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import eu.asangarin.monhun.MonHun;
import eu.asangarin.monhun.components.MHComponents;
import eu.asangarin.monhun.config.ZennyRenderType;
import eu.asangarin.monhun.managers.MHItems;
import eu.asangarin.monhun.managers.MHStatusEffects;
import eu.asangarin.monhun.util.enums.client.CustomHeartType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.tag.FluidTags;
import net.minecraft.text.OrderedText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.text.DecimalFormat;
import java.util.Random;

@Mixin(InGameHud.class)
public abstract class MHInGameHud extends DrawableHelper {
	private static final Identifier MH_ICONS_TEXTURE = MonHun.i("textures/gui/mhicons.png");

	private float heatTicks = 0;

	@Shadow
	protected abstract PlayerEntity getCameraPlayer();

	@Shadow
	@Final
	private MinecraftClient client;

	@Shadow
	protected abstract int getHeartRows(int heartCount);

	@Shadow
	private int scaledWidth;

	@Shadow
	protected abstract int getHeartCount(LivingEntity entity);

	@Shadow
	protected abstract LivingEntity getRiddenEntity();

	@Shadow
	private int scaledHeight;

	@Shadow
	private int ticks;

	@Shadow
	@Final
	private Random random;

	@Shadow
	@Final
	private static Identifier VIGNETTE_TEXTURE;

	@Inject(method = "renderHealthBar", at = @At("HEAD"), cancellable = true)
	private void drawFireHearts(MatrixStack matrices, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, CallbackInfo ci) {
		RenderSystem.setShaderTexture(0, GUI_ICONS_TEXTURE);
		if (!player.hasStatusEffect(StatusEffects.POISON) && !player.hasStatusEffect(StatusEffects.WITHER) && !player.isFreezing() && player.world
				.getDimension().isUltrawarm() && !player.hasStatusEffect(MHStatusEffects.COOL_DRINK)) {
			if (heatTicks < 1.0f) heatTicks += 0.01f;
			int i = 9 * (player.world.getLevelProperties().isHardcore() ? 5 : 0);
			int hardcore = 9 * (player.world.getLevelProperties().isHardcore() ? 1 : 0);
			int j = MathHelper.ceil((double) maxHealth / 2.0D);
			int k = MathHelper.ceil((double) absorption / 2.0D);
			int l = j * 2;

			for (int m = j + k - 1; m >= 0; --m) {
				int n = m / 10;
				int o = m % 10;
				int p = x + o * 8;
				int q = y - n * lines;
				if (lastHealth + absorption <= 4) {
					q += this.random.nextInt(2);
				}

				if (m < j && m == regeneratingHeartIndex) {
					q -= 2;
				}

				RenderSystem.setShaderTexture(0, GUI_ICONS_TEXTURE);
				this.drawHeart(matrices, CustomHeartType.CONTAINER, p, q, i, blinking, false);
				int r = m * 2;
				boolean bl = m >= j;
				if (bl) {
					int s = r - l;
					if (s < absorption) {
						boolean bl2 = s + 1 == absorption;
						this.drawHeart(matrices, CustomHeartType.ABSORBING, p, q, i, false, bl2);
					}
				}

				RenderSystem.setShaderTexture(0, MH_ICONS_TEXTURE);
				boolean bl4;
				if (blinking && r < health) {
					bl4 = r + 1 == health;

					this.drawHeart(matrices, CustomHeartType.FIRE, p, q, hardcore, true, bl4);
				}

				if (r < lastHealth) {
					bl4 = r + 1 == lastHealth;
					this.drawHeart(matrices, CustomHeartType.FIRE, p, q, hardcore, false, bl4);
				}
			}

			RenderSystem.setShaderTexture(0, GUI_ICONS_TEXTURE);
			ci.cancel();
		} else if (heatTicks > 0.0f) heatTicks -= 0.01f;
	}

	@Inject(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHealthBar(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/entity/player/PlayerEntity;IIIIFIIIZ)V", shift = At.Shift.AFTER), cancellable = true)
	private void drawFrozenHunger(MatrixStack matrices, CallbackInfo ci) {
		PlayerEntity player = this.getCameraPlayer();
		if (player.hasStatusEffect(MHStatusEffects.FROZEN_HUNGER) && !player.hasStatusEffect(MHStatusEffects.HOT_DRINK)) {
			RenderSystem.setShaderTexture(0, MH_ICONS_TEXTURE);
			LivingEntity livingEntity = this.getRiddenEntity();
			int k = player.getHungerManager().getFoodLevel();
			int o = this.scaledHeight - 39;
			int n = this.scaledWidth / 2 + 91;
			int t = o - 10;
			int x = this.getHeartCount(livingEntity);
			int z;
			int aa;
			int ad;
			if (x == 0) {
				this.client.getProfiler().swap("food");
				for (z = 0; z < 10; ++z) {
					aa = o;

					if (player.getHungerManager().getSaturationLevel() <= 0.0F && this.ticks % (k * 3 + 1) == 0)
						aa = o + (this.random.nextInt(3) - 1);

					ad = n - z * 8 - 9;
					this.drawTexture(matrices, ad, aa, 18, 18, 9, 9);
					if (z * 2 + 1 < k) this.drawTexture(matrices, ad, aa, 0, 18, 9, 9);

					if (z * 2 + 1 == k) this.drawTexture(matrices, ad, aa, 9, 18, 9, 9);
				}

				t -= 10;
			}
			RenderSystem.setShaderTexture(0, GUI_ICONS_TEXTURE);
			drawAir(matrices, player, x, t);
			ci.cancel();
		}
	}

	private void drawAir(MatrixStack matrices, PlayerEntity player, int x, int t) {
		int n = this.scaledWidth / 2 + 91;
		this.client.getProfiler().swap("air");
		int z = player.getMaxAir();
		int aa = Math.min(player.getAir(), z);
		if (player.isSubmergedIn(FluidTags.WATER) || aa < z) {
			int ab = this.getHeartRows(x) - 1;
			t -= ab * 10;
			int ah = MathHelper.ceil((double) (aa - 2) * 10.0D / (double) z);
			int ad = MathHelper.ceil((double) aa * 10.0D / (double) z) - ah;

			for (int aj = 0; aj < ah + ad; ++aj) {
				if (aj < ah) {
					this.drawTexture(matrices, n - aj * 8 - 9, t, 16, 18, 9, 9);
				} else {
					this.drawTexture(matrices, n - aj * 8 - 9, t, 25, 18, 9, 9);
				}
			}
		}

		this.client.getProfiler().pop();
	}

	private void drawHeart(MatrixStack matrices, CustomHeartType type, int x, int y, int v, boolean blinking, boolean halfHeart) {
		this.drawTexture(matrices, x, y, type.getU(halfHeart, blinking), v, 9, 9);
	}

	@Inject(method = "renderVignetteOverlay", at = @At("HEAD"))
	private void renderVignetteOverlay(Entity entity, CallbackInfo ci) {
		if (heatTicks < 0.1f) return;
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SrcFactor.ONE,
				GlStateManager.DstFactor.ZERO);
		float g = MathHelper.clamp(heatTicks, 0.0F, 1.0F);
		RenderSystem.setShaderColor(0.0F, g, g, 1.0F);

		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, VIGNETTE_TEXTURE);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
		bufferBuilder.vertex(0.0D, this.scaledHeight, -90.0D).texture(0.0F, 1.0F).next();
		bufferBuilder.vertex(this.scaledWidth, this.scaledHeight, -90.0D).texture(1.0F, 1.0F).next();
		bufferBuilder.vertex(this.scaledWidth, 0.0D, -90.0D).texture(1.0F, 0.0F).next();
		bufferBuilder.vertex(0.0D, 0.0D, -90.0D).texture(0.0F, 0.0F).next();
		tessellator.draw();
		RenderSystem.depthMask(true);
		RenderSystem.enableDepthTest();
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.defaultBlendFunc();
	}

	private static final DecimalFormat format = new DecimalFormat("#.##");

	@Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
	private void renderCrosshair(MatrixStack matrices, CallbackInfo ci) {
		PlayerEntity player = this.getCameraPlayer();
		if (player.isUsingItem() && player.getActiveItem().isOf(MHItems.BINOCULARS)) {
			if (player.world != null) {
				HitResult result = updateRayCast(MinecraftClient.getInstance().getTickDelta());
				if (result != null) {
					OrderedText text;
					double distance;
					switch (result.getType()) {
						case BLOCK -> {
							text = player.world.getBlockState(((BlockHitResult) result).getBlockPos()).getBlock().getName().asOrderedText();
							distance = distanceTo(result.getPos(), player.getPos());
						}
						case ENTITY -> {
							Entity entity = ((EntityHitResult) result).getEntity();
							text = entity.getName().asOrderedText();
							distance = entity.distanceTo(player);
						}
						case MISS -> {
							text = new TranslatableText("binocular.monhun.too_far").formatted(Formatting.RED).asOrderedText();
							distance = 0.0d;
						}
						default -> throw new IllegalStateException("Unexpected value: " + result.getType());
					}

					matrices.push();
					RenderSystem.enableBlend();
					RenderSystem.defaultBlendFunc();
					drawCenteredTextWithShadow(matrices, client.textRenderer, text, scaledWidth / 2, scaledHeight / 2 + 64, 0xFFFFFF);
					if (distance > 0.1d) {
						drawCenteredTextWithShadow(matrices, client.textRenderer,
								new TranslatableText("binocular.monhun.distance", format.format(distance)).asOrderedText(), scaledWidth / 2,
								scaledHeight / 2 + 52, 0xFFFFFF);
					}
					RenderSystem.disableBlend();
					matrices.pop();
					RenderSystem.setShaderTexture(0, GUI_ICONS_TEXTURE);
				}
			}

			ci.cancel();
		}
	}

	@Inject(method = "render", at = @At("TAIL"))
	private void renderZenny(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
		ZennyRenderType renderType = MonHun.getConfig().getClient().getRendering().getRenderZennyAndPoints();
		if (renderType == ZennyRenderType.NEVER) return;

		String zenny = Integer.toString(MHComponents.MONEY.get(getCameraPlayer()).getZenny());
		String points = Integer.toString(MHComponents.MONEY.get(getCameraPlayer()).getPoints());
		RenderSystem.enableBlend();
		RenderSystem.setShaderTexture(0, MH_ICONS_TEXTURE);
		this.drawTexture(matrices, scaledWidth - 96, 22, 168, 0, 88, 14);
		this.drawTexture(matrices, scaledWidth - 96, 6, 168, 16, 88, 14);
		client.textRenderer.draw(matrices, zenny, scaledWidth - (20 + (6 * zenny.length())), 10, 0xFFFFFF);
		client.textRenderer.draw(matrices, points, scaledWidth - (32 + (6 * points.length())), 26, 0xFFFFFF);
		client.textRenderer.draw(matrices, "z", scaledWidth - 20, 10, 0xFFFFFF);
		client.textRenderer.draw(matrices, "pts", scaledWidth - 32, 26, 0xFFFFFF);
		RenderSystem.disableBlend();
	}

	public float distanceTo(Vec3d vec1, Vec3d vec2) {
		float f = (float) (vec1.getX() - vec2.getX());
		float g = (float) (vec1.getY() - vec2.getY());
		float h = (float) (vec1.getZ() - vec2.getZ());
		return MathHelper.sqrt(f * f + g * g + h * h);
	}

	public HitResult updateRayCast(float tickDelta) {
		HitResult hitResult = null;
		Entity entity = this.client.getCameraEntity();
		if (entity != null) {
			if (this.client.world != null) {
				double d = MonHun.getConfig().getClient().getItems().getBinocularsMaxRange();
				hitResult = entity.raycast(d, tickDelta, false);
				Vec3d vec3d = entity.getCameraPosVec(tickDelta);
				double e = d * d;
				if (hitResult != null) {
					e = hitResult.getPos().squaredDistanceTo(vec3d);
				}

				Vec3d vec3d2 = entity.getRotationVec(1.0F);
				Vec3d vec3d3 = vec3d.add(vec3d2.x * d, vec3d2.y * d, vec3d2.z * d);
				Box box = entity.getBoundingBox().stretch(vec3d2.multiply(d)).expand(1.0D, 1.0D, 1.0D);
				EntityHitResult entityHitResult = ProjectileUtil
						.raycast(entity, vec3d, vec3d3, box, (entityx) -> !entityx.isSpectator() && entityx.collides(), e);
				if (entityHitResult != null) {
					hitResult = entityHitResult;
				}
			}
		}

		return hitResult;
	}
}
