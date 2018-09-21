package nessiesson.uselessmod.mixins;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderGlobal.class)
public abstract class MixinRenderGlobal {
	@Inject(method = "notifyLightSet", at = @At("HEAD"), cancellable = true)
	private void noLight(BlockPos pos, CallbackInfo ci) {
		ci.cancel();
	}
}
