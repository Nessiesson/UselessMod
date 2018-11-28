package nessiesson.uselessmod.mixins;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(WorldClient.class)
public abstract class MixinWorldClient {
	@Inject(method = "invalidateRegionAndSetBlock", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/client/multiplayer/WorldClient;invalidateBlockReceiveRegion(IIIIII)V"), cancellable = true)
	private void fixPistonBlinking(BlockPos pos, IBlockState state, CallbackInfoReturnable<Boolean> cir) {
		if (state.getBlock() == Blocks.PISTON_EXTENSION) {
			cir.setReturnValue(false);
		}
	}

	@Redirect(method = "playMoodSoundAndCheckLight", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/multiplayer/WorldClient;ambienceTicks:I"))
	private int overrideCaveAmbienceTicks(WorldClient worldClient) {
		return 0;
	}

	@Redirect(method = "playMoodSoundAndCheckLight", at = @At(value = "INVOKE", ordinal = 0, target = "Ljava/util/Random;nextInt(I)I", remap = false))
	private int useConstantLightLevel(Random random, int bound) {
		return 8;
	}
}
