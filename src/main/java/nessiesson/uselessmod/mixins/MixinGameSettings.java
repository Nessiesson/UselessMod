package nessiesson.uselessmod.mixins;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameSettings.class)
public abstract class MixinGameSettings {
	@Shadow
	public float gammaSetting;

	@Shadow
	public abstract float getOptionFloatValue(GameSettings.Options settingOption);

	@Inject(method = "setOptionFloatValue", at = @At("HEAD"), cancellable = true)
	private void overrideGammaValue(GameSettings.Options settingsOption, float value, CallbackInfo ci) {
		if (settingsOption != GameSettings.Options.GAMMA) {
			return;
		}
		ci.cancel();

		if (value >= 105.0F) {
			value = 1000.0F;
		} else if (value >= 100.0F) {
			value = 1.0F;
		} else {
			value = Math.min(1.0F, value * 1.1F / 100.0F);
		}
		this.gammaSetting = value;
	}

	@Inject(method = "getKeyBinding", at = @At("HEAD"), cancellable = true)
	private void overrideGammaText(GameSettings.Options settingOption, CallbackInfoReturnable<String> cir) {
		if (settingOption != GameSettings.Options.GAMMA) {
			return;
		}
		cir.cancel();

		final float f = this.getOptionFloatValue(settingOption);
		String s = I18n.format(settingOption.getTranslation()) + ": ";

		if (f > 1.0F) {
			s += "GammaBright";
		} else if (f == 1.0F) {
			s += I18n.format("options.gamma.max");
		} else if (f > 0.0F) {
			s += "+" + (int) (f * 100.0F) + "%";
		} else {
			s += I18n.format("options.gamma.min");
		}

		cir.setReturnValue(s);
	}
}
