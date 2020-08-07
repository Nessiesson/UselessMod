package nessiesson.uselessmod.mixins;

import nessiesson.uselessmod.UselessMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiOverlayDebug;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;

@Mixin(GuiOverlayDebug.class)
public abstract class MixinGuiOverlayDebug {
	@Shadow @Final private Minecraft mc;

	@Inject(method = "call", at = @At("RETURN"))
	private void tpsDebugText(CallbackInfoReturnable<List<String>> cir) {
		final double mspt = UselessMod.mspt;
		final String currentLine = cir.getReturnValue().get(4);
		cir.getReturnValue().set(4, String.format("%s, mspt: \u2248%.1f, tps: \u2248%.1f", currentLine, mspt, 1000D / mspt));
	}

	@Inject(method = "call", at = @At("RETURN"))
	private void packetCount(CallbackInfoReturnable<List<String>> cir) {
		final int rx_per_second = Arrays.stream(UselessMod.receivedPackets).sum();
		final int tx_per_second = Arrays.stream(UselessMod.sendPackets).sum();
		cir.getReturnValue().add(5, String.format("Networking packets: %drx/s %dtx/s", rx_per_second, tx_per_second));
	}

	@Inject(method = "call", at = @At("RETURN"))
	private void displayRegion(CallbackInfoReturnable<List<String>> cir) {
		BlockPos blockpos = new BlockPos(this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().getEntityBoundingBox().minY, this.mc.getRenderViewEntity().posZ);
		String regionName = "r." + (blockpos.getX() >> 9) + "." + (blockpos.getZ() >> 9) + ".mca";
		cir.getReturnValue().add(11, String.format("Region: %s", regionName));
	}

	@Redirect(method = "call", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getVersion()Ljava/lang/String;"))
	private String pureVanilla(Minecraft mc) {
		return "vanilla++";
	}
}
