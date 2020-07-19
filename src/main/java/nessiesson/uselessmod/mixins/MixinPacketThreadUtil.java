package nessiesson.uselessmod.mixins;
import nessiesson.uselessmod.UselessMod;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.util.IThreadListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PacketThreadUtil.class)
public abstract class MixinPacketThreadUtil {
	@Inject(method = "checkThreadAndEnqueue", at = @At("INVOKE"))
	private static < T extends INetHandler> void countSendPackets(Packet<T> packetIn, T processor, IThreadListener scheduler, CallbackInfo ci) {
		UselessMod.receivedPacketsThisTick++;
	}
}
