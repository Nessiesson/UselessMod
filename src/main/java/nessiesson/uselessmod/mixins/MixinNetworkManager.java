package nessiesson.uselessmod.mixins;
import nessiesson.uselessmod.UselessMod;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkManager.class)
public abstract class MixinNetworkManager {
	@Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("INVOKE"))
	private void countSendPackets(Packet<?> packetIn, CallbackInfo ci) {
		UselessMod.sendPacketsThisTick++;
	}
}
