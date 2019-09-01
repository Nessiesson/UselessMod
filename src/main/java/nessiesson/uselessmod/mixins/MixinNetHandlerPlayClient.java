package nessiesson.uselessmod.mixins;

import nessiesson.uselessmod.UselessMod;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public abstract class MixinNetHandlerPlayClient {
	@Inject(method = "handleTimeUpdate", at = @At("RETURN"))
	private void onTimeUpdate(SPacketTimeUpdate packetIn, CallbackInfo ci) {
		final long currentTime = System.nanoTime();
		final long dt = currentTime - UselessMod.lastTimeUpdate;
		UselessMod.lastTimeUpdate = currentTime;

		if (dt > 0L) {
			UselessMod.mspt = Math.max(50.0, dt * 5e-8);
		}
	}

	@Inject(method = "handleSoundEffect", at = @At("HEAD"), cancellable = true)
	private void removeNPE(SPacketSoundEffect packetIn, CallbackInfo ci) {
		if (packetIn == null) {
			ci.cancel();
		}
	}
}
