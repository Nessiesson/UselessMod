package nessiesson.uselessmod.mixins;

import nessiesson.uselessmod.LiteModUselessMod;
import net.minecraft.client.gui.GuiOverlayDebug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(GuiOverlayDebug.class)
public abstract class MixinGuiOverlayDebug {
	@Inject(method = "call", at = @At("RETURN"))
	private void tpsDebugText(CallbackInfoReturnable<List<String>> cir) {
		cir.getReturnValue().set(3, String.format("%s, MSPT: %.1f, TPS: %.1f", cir.getReturnValue().get(3), LiteModUselessMod.MSPT, 1000.0 / LiteModUselessMod.MSPT));
	}
}
