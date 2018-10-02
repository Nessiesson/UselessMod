package nessiesson.uselessmod.mixins;

import net.minecraft.client.audio.MusicTicker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MusicTicker.class)
public abstract class MixinMusicTicker {
	@Shadow
	private int timeUntilNextMusic;

	@Inject(method = "update", at = @At(value = "INVOKE", ordinal = 1, shift = At.Shift.AFTER, target = "Ljava/lang/Math;min(II)I", remap = false))
	private void continuousMusic(CallbackInfo ci) {
		if (this.timeUntilNextMusic > 100) {
			this.timeUntilNextMusic = 100;
		}
	}
}
