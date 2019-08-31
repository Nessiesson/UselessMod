package nessiesson.uselessmod.mixins;

import nessiesson.uselessmod.DesktopApi;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.URI;

@SuppressWarnings("RedundantCast")
@Mixin(GuiScreen.class)
public abstract class MixinGuiScreen {
	@Shadow
	protected Minecraft mc;

	@Inject(method = "handleInput", at = {
			@At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiScreen;handleKeyboardInput()V"),
			@At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiScreen;handleMouseInput()V")},
			cancellable = true)
	private void mc31222(CallbackInfo ci) {
		if ((GuiScreen) (Object) this != this.mc.currentScreen) {
			ci.cancel();
		}
	}

	/*// Fix for Robi's setup.
	@Inject(method = "openWebLink", at = @At("HEAD"), cancellable = true)
	private void fixRobisShit(URI url, CallbackInfo ci) {
		ci.cancel();
		try {
			DesktopApi.browse(url);
		} catch (Exception ignored) {
			// noop
		}
	}*/
	@Overwrite
	private void openWebLink(URI url) {
		DesktopApi.browse(url);
	}
}
