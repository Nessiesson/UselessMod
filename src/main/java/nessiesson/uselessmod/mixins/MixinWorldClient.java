package nessiesson.uselessmod.mixins;

import nessiesson.uselessmod.Configuration;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;

@Mixin(WorldClient.class)
public abstract class MixinWorldClient extends World {
	protected MixinWorldClient(ISaveHandler ish, WorldInfo wi, WorldProvider wp, Profiler p, boolean c) {
		super(ish, wi, wp, p, c);
	}

	@Override
	public void updateEntity(Entity entity) {
		if (Configuration.clientEntityUpdates || entity instanceof EntityPlayer || entity instanceof EntityFireworkRocket) {
			super.updateEntity(entity);
		}
	}

	@Override
	public float getRainStrength(float delta) {
		return Configuration.showRain ? this.prevRainingStrength + (this.rainingStrength - this.prevRainingStrength) * delta : 0F;
	}

	@Inject(method = "showBarrierParticles", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;randomDisplayTick(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Ljava/util/Random;)V", shift = At.Shift.AFTER), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
	private void showB36(int x, int y, int z, int offset, Random random, boolean holdingBarrier, BlockPos.MutableBlockPos pos, CallbackInfo ci, int i, int j, int k, IBlockState state) {
		final Block block = state.getBlock();
		final EntityPlayer player = Minecraft.getMinecraft().player;
		if (holdingBarrier && block == Blocks.BARRIER || block == Blocks.PISTON_EXTENSION && (player.isSpectator() || player.isCreative()) && this.getTileEntity(pos) == null) {
			this.spawnParticle(EnumParticleTypes.BARRIER, (float) i + 0.5F, (float) j + 0.5F, (float) k + 0.5F, 0D, 0D, 0D);
		}
	}
}
