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

	@Inject(method = "playMusic", at = @At("RETURN"))
	private void continuousMusic(MusicTicker.MusicType requestedMusicType, CallbackInfo ci) {
		this.timeUntilNextMusic = 100;
	}
}
