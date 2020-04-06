package nessiesson.uselessmod.mixins;

import nessiesson.uselessmod.UselessMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiOverlayDebug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(GuiOverlayDebug.class)
public abstract class MixinGuiOverlayDebug {
	@Inject(method = "call", at = @At("RETURN"))
	private void tpsDebugText(CallbackInfoReturnable<List<String>> cir) {
		final double mspt = UselessMod.mspt;
		final String currentLine = cir.getReturnValue().get(4);
		cir.getReturnValue().set(4, String.format("%s, mspt: \u2248%.1f, tps: \u2248%.1f", currentLine, mspt, 1000D / mspt));
	}

	@Redirect(method = "call", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getVersion()Ljava/lang/String;"))
	private String pureVanilla(Minecraft mc) {
		return "vanilla++";
	}
}
