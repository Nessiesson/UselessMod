package nessiesson.uselessmod.mixins;

import nessiesson.uselessmod.Configuration;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFirework;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(ParticleFirework.Spark.class)
public abstract class MixinParticleFireWorkDOLSpark extends Particle {
	private static final Random RNG = new Random();

	protected MixinParticleFireWorkDOLSpark(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	@Inject(method = "<init>", at = @At("RETURN"))
	private void rainbowFireworksTrail(World world, double x, double y, double z, double dx, double dy, double dz, ParticleManager manager, CallbackInfo ci) {
		if (Configuration.tasteTheRainbow) {
			this.particleRed = RNG.nextFloat();
			this.particleGreen = RNG.nextFloat();
			this.particleBlue = RNG.nextFloat();
		}
	}
}
