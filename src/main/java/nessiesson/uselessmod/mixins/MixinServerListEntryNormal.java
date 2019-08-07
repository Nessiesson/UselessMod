package nessiesson.uselessmod.mixins;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ServerListEntryNormal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerListEntryNormal.class)
public abstract class MixinServerListEntryNormal {
	@Redirect(method = "drawEntry", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawString(Ljava/lang/String;III)I", ordinal = 0))
	private int hideServerNames(FontRenderer fontRenderer, String text, int x, int y, int color) {
		// noop
		return 0;
	}
}
