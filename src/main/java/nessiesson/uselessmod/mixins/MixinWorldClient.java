package nessiesson.uselessmod.mixins;

import net.minecraft.client.multiplayer.WorldClient;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(WorldClient.class)
public abstract class MixinWorldClient {
	@Redirect(method = "playMoodSoundAndCheckLight", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/multiplayer/WorldClient;ambienceTicks:I"))
	private int overrideCaveAmbienceTicks(WorldClient worldClient) {
		return 0;
	}

	@Redirect(method = "playMoodSoundAndCheckLight", at = @At(value = "INVOKE", ordinal = 0, target = "Ljava/util/Random;nextInt(I)I", remap = false))
	private int useConstantLightLevel(Random random, int bound) {
		return 8;
	}
}
