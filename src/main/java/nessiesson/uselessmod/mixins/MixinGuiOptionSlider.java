package nessiesson.uselessmod.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiOptionSlider;
import net.minecraft.client.settings.GameSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiOptionSlider.class)
public abstract class MixinGuiOptionSlider {
	@Shadow
	private float sliderValue;

	@Inject(method = "<init>(IIILnet/minecraft/client/settings/GameSettings$Options;FF)V", at = @At("RETURN"))
	private void fixGammaSlider(int buttonId, int x, int y, GameSettings.Options optionIn, float minValueIn, float maxValue, CallbackInfo ci) {
		if (optionIn == GameSettings.Options.GAMMA) {
			final float f = Minecraft.getMinecraft().gameSettings.getOptionFloatValue(optionIn);
			this.sliderValue = Math.min(1.0F, f);
		}
	}
}
