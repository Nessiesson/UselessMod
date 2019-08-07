package nessiesson.uselessmod.mixins;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMainMenu.class)
public abstract class MixinGuiMainMenu {
	@Shadow
	private GuiButton realmsButton;

	@Inject(method = "addSingleplayerMultiplayerButtons", at = @At("RETURN"))
	private void hideRealmsButton(int p_73969_1_, int p_73969_2_, CallbackInfo ci) {
		this.realmsButton.visible = false;
	}
}
