
package nessiesson.uselessmod.mixins;

import nessiesson.uselessmod.Configuration;
import nessiesson.uselessmod.UselessMod;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.ServerSelectionList;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.network.ServerPinger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.UnknownHostException;

@Mixin(GuiMultiplayer.class)
public abstract class MixinGuiMultiplayer {

	@Shadow @Final private ServerPinger oldServerPinger;

	@Shadow private ServerList savedServerList;

	@Shadow public ServerSelectionList serverListSelector;
	private int tick = 0;

	@Inject(method = "updateScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ServerPinger;pingPendingNetworks()V", ordinal = 0))
	private void dynamicListUpdates(CallbackInfo ci) throws UnknownHostException {
		if (Configuration.dynamicServerListUpdates) {
			tick++;
			int listSize = serverListSelector.height - (serverListSelector.height - serverListSelector.bottom) - serverListSelector.top;
			int visibleSlots = listSize / serverListSelector.getSlotHeight();
			int startIndex = serverListSelector.getAmountScrolled() / serverListSelector.getSlotHeight();
			if (tick >= 300) {
				tick = 0;
				for(int j = startIndex; j < visibleSlots + 1; j++) {
					try {
						this.oldServerPinger.ping(this.savedServerList.getServerData(j));
					} catch (Exception e) {
						;
					}
				}
				this.serverListSelector.updateOnlineServers(this.savedServerList);
			}
		}
	}

	@Inject(method = "connectToServer", at = @At(value = "HEAD"))
	private void updateServerData(ServerData server, CallbackInfo ci) {
		UselessMod.currentServer = server;
	}
}