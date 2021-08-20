package eu.asangarin.monhun.managers;

import eu.asangarin.monhun.particles.ShinyParticle;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;

public class MHParticles {
	public static final DefaultParticleType SHINY = FabricParticleTypes.simple();

	public static void registerFactories() {
		ParticleFactoryRegistry.getInstance().register(SHINY, ShinyParticle.Factory::new);
	}
}
