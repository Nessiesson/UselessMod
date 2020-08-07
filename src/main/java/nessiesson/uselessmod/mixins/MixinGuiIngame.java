package nessiesson.uselessmod.mixins;

import nessiesson.uselessmod.UselessMod;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.resources.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(GuiIngameMenu.class)
public abstract class MixinGuiIngame  extends GuiScreen {
	@Inject(method = "updateScreen", at = @At("INVOKE"))
	public void addReloadButton(CallbackInfo ci) {
		if (!this.mc.isIntegratedServerRunning()) {
			UselessMod.currentServer = this.mc.getCurrentServerData();
			if (GuiScreen.isShiftKeyDown()) {
				(this.buttonList.get(0)).displayString = I18n.format("menu.relog");
			} else
				(this.buttonList.get(0)).displayString = I18n.format("menu.disconnect");
		}
	}
	@Inject(method = "actionPerformed", at = @At("INVOKE"))
	public void saveChat(GuiButton button, CallbackInfo ci){
		String currentServer = "SINGLEPLAYER";
		if (UselessMod.currentServer != null) {
			currentServer = UselessMod.currentServer.serverIP;
		}
		UselessMod.tabCompleteHistory.put(currentServer, new ArrayList<>(this.mc.ingameGUI.getChatGUI().getSentMessages()));
		UselessMod.chatHistory.put(currentServer, new ArrayList<>(this.mc.ingameGUI.getChatGUI().chatLines));
	}
	@Inject(method = "actionPerformed", at = @At("RETURN"))
	public void handleRelogButton(GuiButton button, CallbackInfo ci){
		if (!this.mc.isIntegratedServerRunning() && GuiScreen.isShiftKeyDown() && button.id == 1) {
			this.mc.displayGuiScreen(new GuiConnecting(this, this.mc, UselessMod.currentServer));
		}
	}

}
