package nessiesson.uselessmod.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerControllerMP.class)
public abstract class MixinPlayerControllerMP {
	@Inject(method = "clickBlock", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;onPlayerDestroyBlock(Lnet/minecraft/util/math/BlockPos;)Z"))
	private void onInstantMine(BlockPos loc, EnumFacing face, CallbackInfoReturnable<Boolean> cir) {
		final NetHandlerPlayClient connection = Minecraft.getMinecraft().getConnection();
		connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, loc, face));
	}
}
