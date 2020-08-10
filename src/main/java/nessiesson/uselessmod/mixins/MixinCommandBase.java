package nessiesson.uselessmod.mixins;

import nessiesson.uselessmod.Configuration;
import net.minecraft.command.CommandBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CommandBase.class)
public abstract class MixinCommandBase {
	@Inject(method = "checkPermission", at = @At("HEAD"), cancellable = true)
	private void overrideCommandPermissions(CallbackInfoReturnable<Boolean> cir) {
		if (Configuration.alwaysSingleplayerCheats) {
			cir.setReturnValue(true);
		}
	}
}
