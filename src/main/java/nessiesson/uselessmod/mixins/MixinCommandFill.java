package nessiesson.uselessmod.mixins;

import net.minecraft.command.CommandFill;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(CommandFill.class)
public abstract class MixinCommandFill {
	@ModifyConstant(method = "execute", constant = @Constant(intValue = 32768))
	private int removeFillLimit(int value) {
		return Integer.MAX_VALUE;
	}
}
