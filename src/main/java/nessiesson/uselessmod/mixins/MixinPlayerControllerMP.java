package nessiesson.uselessmod.mixins;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PlayerControllerMP.class)
public abstract class MixinPlayerControllerMP {
	// Client-side fix for instant mining ghost blocks.
	@Inject(method = "clickBlock", at = @At(value = "INVOKE", shift = At.Shift.AFTER,
			target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;"
					+ "onPlayerDestroyBlock(Lnet/minecraft/util/math/BlockPos;)Z"),
			locals = LocalCapture.CAPTURE_FAILSOFT)
	private void onInstantMine(BlockPos loc, EnumFacing face, CallbackInfoReturnable<Boolean> cir, PlayerInteractEvent.LeftClickBlock event, IBlockState iblockstate, boolean flag) {
		/*if (!LiteModUselessMod.config.isMiningGhostblockFixEnabled) {
			return;
		}*/

		Minecraft mc = Minecraft.getMinecraft();
		if (iblockstate.getBlockHardness(mc.world, loc) > 0.0f) {
			NetHandlerPlayClient connection = mc.getConnection();
			connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(loc, face, EnumHand.MAIN_HAND, 0f, 0f, 0f));
		}
	}
}
