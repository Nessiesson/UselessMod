package nessiesson.uselessmod.mixins;

import nessiesson.uselessmod.Configuration;
import nessiesson.uselessmod.UselessMod;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.init.Blocks;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderGlobal.class)
public abstract class MixinRenderGlobal {
	@Shadow
	@Final
	private Minecraft mc;

	@Inject(method = "notifyLightSet", at = @At("HEAD"), cancellable = true)
	private void noLight(BlockPos pos, CallbackInfo ci) {
		ci.cancel();
	}

	@Inject(method = "isOutlineActive", at = @At("HEAD"), cancellable = true)
	private void highlightAllEntitites(Entity entityIn, Entity viewer, ICamera camera, CallbackInfoReturnable<Boolean> cir) {
		if (this.mc.player.isSpectator() && UselessMod.highlightEntities.isKeyDown()) {
			cir.setReturnValue((entityIn instanceof EntityLivingBase || entityIn instanceof EntityMinecart) && (entityIn.ignoreFrustumCheck || camera.isBoundingBoxInFrustum(entityIn.getEntityBoundingBox()) || entityIn.isRidingOrBeingRiddenBy(this.mc.player)));
		}
	}

	@Redirect(method = "playEvent", at = @At(ordinal = 0, value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/WorldClient;playSound(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/SoundEvent;Lnet/minecraft/util/SoundCategory;FFZ)V"), slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getSoundType(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;)Lnet/minecraft/block/SoundType;")))
	private void noBreakSound(WorldClient worldClient, BlockPos pos, SoundEvent soundIn, SoundCategory category, float volume, float pitch, boolean distanceDelay) {
		if (!Configuration.showExplosion) {
			worldClient.playSound(pos, soundIn, category, volume, pitch, distanceDelay);
		}
	}

	@Inject(method = "notifyBlockUpdate", at = @At("HEAD"), cancellable = true)
	private void onNotifyBlockUpdate(World worldIn, BlockPos pos, IBlockState oldState, IBlockState newState, int flags, CallbackInfo ci) {
		if (newState.getBlock() == Blocks.PISTON_EXTENSION) {
			ci.cancel();
		}
	}
}
