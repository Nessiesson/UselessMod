package nessiesson.uselessmod.mixins;

import net.minecraft.client.gui.BossInfoClient;
import net.minecraft.client.gui.GuiBossOverlay;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiBossOverlay.class)
public abstract class MixinGuiBossOverlay {
	private static final String[] op = {
			"rid is hella gae", "CARPETMOD IS NOT VANILLA IDIOT",
			"mango has best hot streams ofc", "duping is for lazy idiots", "man are u actually retarded?",
			"mind you r own business", "more vanilla than spigot", "I'll build a statue of you on the SciCraft server!",
			"btw u heard of the new scicrap guy moddit?", "only lazy jerks will read this message.", "moronzz best!",
			"JC Denton. 23 years old. No residence. No ancestors. No employer. No -",
			"I am a prototype for a much larger system.", "kk ur floor is burning"
	};

	@Redirect(method = "renderBossHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/BossInfoClient;getName()Lnet/minecraft/util/text/ITextComponent;"))
	private ITextComponent hotterStream(BossInfoClient bossInfoClient) {
		int i = Math.abs(bossInfoClient.getUniqueId().hashCode());
		return new TextComponentString(op[i % op.length]);
	}
}
