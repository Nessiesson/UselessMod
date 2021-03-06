package nessiesson.uselessmod.mixins;

import nessiesson.uselessmod.Configuration;
import net.minecraft.client.gui.GuiChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(GuiChat.class)
public abstract class MixinGuiChat {
	@ModifyConstant(method = "initGui", constant = @Constant(intValue = 256))
	private int increaseLimit(int orig) {
		return Configuration.extendedChat ? 2048 : orig;
	}
}
