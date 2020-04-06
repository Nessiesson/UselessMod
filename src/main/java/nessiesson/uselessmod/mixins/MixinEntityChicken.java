package nessiesson.uselessmod.mixins;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityChicken.class)
public abstract class MixinEntityChicken extends EntityAnimal {
	public MixinEntityChicken(World world) {
		super(world);
	}

	@Inject(method = "onLivingUpdate", at = @At("RETURN"))
	private void derpyChicken(CallbackInfo ci) {
		this.rotationPitch = -90F;
	}
}
