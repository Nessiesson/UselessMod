package nessiesson.uselessmod.mixins;

import nessiesson.uselessmod.Configuration;
import net.minecraft.client.renderer.chunk.RenderChunk;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderChunk.class)
public abstract class MixinRenderChunkOptifine {
	@Dynamic
	@Inject(method = "isPlayerUpdate", at = @At("HEAD"), cancellable = true, remap = false)
	private void stopDumbBlinking(CallbackInfoReturnable<Boolean> ci) {
		if (Configuration.lessOptifineBlinkingPistons) {
			ci.setReturnValue(true);
		}
	}
}
