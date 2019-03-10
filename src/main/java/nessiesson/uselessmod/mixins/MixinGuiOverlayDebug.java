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
		final double mspt = LiteModUselessMod.mspt;
		final String currentLine = cir.getReturnValue().get(4);
		cir.getReturnValue().set(4, String.format("%s, mspt: %.1f, tps: %.1f", currentLine, mspt, 1000.0 / mspt));
	}
}
