package nessiesson.uselessmod.mixins;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngameMenu.class)
public abstract class MixinGuiIngame  extends GuiScreen {
	private ServerData currentServer;
	@Inject(method = "updateScreen", at = @At("INVOKE"))
	public void addReloadButton(CallbackInfo ci) {
		if (!this.mc.isIntegratedServerRunning()) {
			currentServer = this.mc.getCurrentServerData();
			if (GuiScreen.isShiftKeyDown()) {
				(this.buttonList.get(0)).displayString = I18n.format("menu.relog");
			} else
				(this.buttonList.get(0)).displayString = I18n.format("menu.disconnect");
		}
	}
	@Inject(method = "actionPerformed", at = @At("RETURN"))
	public void handleRelogButton(GuiButton button, CallbackInfo ci){
		if (!this.mc.isIntegratedServerRunning() && GuiScreen.isShiftKeyDown() && button.id == 1) {
			this.mc.displayGuiScreen(new GuiConnecting(this, this.mc, currentServer));
		}
	}
}
