package nessiesson.uselessmod.mixins;

import net.minecraft.client.settings.GameSettings;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(targets = "net/minecraft/client/settings/GameSettings$Options")
public abstract class MixinGameSettings$Options {
	static {
		GameSettings.Options.GAMMA.setValueMax(110.0F);
	}
}
