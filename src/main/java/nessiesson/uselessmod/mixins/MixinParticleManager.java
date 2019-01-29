package nessiesson.uselessmod.mixins;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleDigging;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleDigging.class)
public abstract class MixinParticleManager extends Particle {
	protected MixinParticleManager(World worldIn, double posXIn, double posYIn, double posZIn) {
		super(worldIn, posXIn, posYIn, posZIn);
	}

	@Inject(method = "<init>", at = @At("RETURN"))
	private void removeRandomParticleMotion(CallbackInfo ci) {
		this.motionX = this.motionY = this.motionZ = 0.0D;
		this.particleGravity = 0.0F;
	}
}
