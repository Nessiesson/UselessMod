package nessiesson.uselessmod.mixins;

import nessiesson.uselessmod.Configuration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ServerListEntryNormal;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.text.TextFormatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerListEntryNormal.class)
public abstract class MixinServerListEntryNormal {
	@Shadow @Final private ServerData server;

	@Shadow @Final private Minecraft mc;

	private int textWidth = 0;

	@Redirect(method = "drawEntry", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawString(Ljava/lang/String;III)I", ordinal = 0))
	private int hideServerNames(FontRenderer fontRenderer, String text, int x, int y, int color) {
		return Configuration.showServerNames ? fontRenderer.drawString(text, x, y, color) : 0;
	}

	@Redirect(method = "drawEntry", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawString(Ljava/lang/String;III)I", ordinal = 2))
	private int alwaysShowPing(FontRenderer fontRenderer, String text, int x, int y, int color) {
		if (Configuration.alwaysShowPing) {
			long ms = this.server.pingToServer < 0 ? 0 : this.server.pingToServer;
			textWidth = this.mc.fontRenderer.getStringWidth(" " + ms + "ms");
			String s2 = (this.server.version > 340 || this.server.version < 340) ? TextFormatting.DARK_RED + this.server.gameVersion : this.server.populationInfo;
			fontRenderer.drawString(ms + "ms", x + this.mc.fontRenderer.getStringWidth(s2) + 15 - textWidth, y, color);
			x -= textWidth;
		}
		return fontRenderer.drawString(text, x, y, color);
	}

	@Redirect(method = "drawEntry", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;drawModalRectWithCustomSizedTexture(IIFFIIFF)V", ordinal = 0))
	private void moveConnectionIcon(int x, int y, float u, float v, int width, int height, float textureWidth, float textureHeight) {
		if (Configuration.alwaysShowPing) {
			x -= textWidth;
		}
		Gui.drawModalRectWithCustomSizedTexture(x, y, u, v, width, height, textureWidth, textureHeight);
	}
}
