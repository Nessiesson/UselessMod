package nessiesson.uselessmod.mixins;

import nessiesson.uselessmod.LiteModUselessMod;
import net.minecraft.client.network.NetHandlerPlayClient;
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
		final long dt = currentTime - LiteModUselessMod.lastTimeUpdate;
		LiteModUselessMod.lastTimeUpdate = currentTime;

		if (dt > 0L) {
			LiteModUselessMod.mspt = Math.max(50.0, dt * 5e-8);
		}
	}
}
