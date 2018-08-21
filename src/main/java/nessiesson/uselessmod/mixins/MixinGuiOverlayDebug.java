package nessiesson.uselessmod.mixins;

import net.minecraft.client.gui.GuiOverlayDebug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(GuiOverlayDebug.class)
public abstract class MixinGuiOverlayDebug {
	@Inject(method = "call", at = @At("RETURN"))
	private void hotStream(CallbackInfoReturnable<List<String>> cir) {
		cir.getReturnValue().set(0, "More Vanilla Than Carpet 1.12.0 (DuggedSMP/Hot Stream)");
	}
}
