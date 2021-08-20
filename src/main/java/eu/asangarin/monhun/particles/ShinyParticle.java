package eu.asangarin.monhun.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.AnimatedParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

@Environment(EnvType.CLIENT)
public class ShinyParticle {
	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<DefaultParticleType> {
		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
			return new ShinyParticle.Sparkle(clientWorld, d, e, f, this.spriteProvider);
		}
	}

	@Environment(EnvType.CLIENT)
	public static class Sparkle extends AnimatedParticle {
		Sparkle(ClientWorld clientWorld, double d, double e, double f, SpriteProvider spriteProvider) {
			super(clientWorld, d, e, f, spriteProvider, 0.0F);
			setColorAlpha(0.99f);
			this.scale *= 0.75F;
			this.maxAge = 48 + this.random.nextInt(12);
			this.setSpriteForAge(spriteProvider);
		}

		public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
			this.setColorAlpha(age > 46 ? 0f : (age > 24 ? (1f - ((float) (age - 24) / 24)) : 1f));
			super.buildGeometry(vertexConsumer, camera, tickDelta);
		}

		public float getSize(float tickDelta) {
			return (((float) this.age + tickDelta - 1.0F) * 0.003F);
		}
	}
}
