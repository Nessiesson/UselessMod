package nessiesson.uselessmod.mixins;

import nessiesson.uselessmod.LiteModUselessMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketSetSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(NetHandlerPlayClient.class)
public abstract class MixinNetHandlerPlayClient {
	@Shadow
	private Minecraft client;

	//TODO: handle armour and offhand slots.
	@Inject(method = "handleSetSlot", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void playToolBreakSound(SPacketSetSlot packetIn, CallbackInfo ci, EntityPlayer player, ItemStack stack, int i) {
		if (packetIn.getWindowId() == 0 && LiteModUselessMod.shouldPlayBreakSound && stack.isEmpty() && player.inventory.currentItem == i - 36) {
			LiteModUselessMod.shouldPlayBreakSound = false;
			this.client.player.renderBrokenItemStack(LiteModUselessMod.whichToolShouldBreak);
		}
	}
}
