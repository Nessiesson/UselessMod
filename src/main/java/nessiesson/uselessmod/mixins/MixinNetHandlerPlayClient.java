package nessiesson.uselessmod.mixins;

import nessiesson.uselessmod.UselessMod;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketBlockAction;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;

@Mixin(NetHandlerPlayClient.class)
public abstract class MixinNetHandlerPlayClient {
	@Shadow
	private WorldClient world;

	@Inject(method = "handleTimeUpdate", at = @At("RETURN"))
	private void onTimeUpdate(SPacketTimeUpdate packetIn, CallbackInfo ci) {
		final long currentTime = System.nanoTime();
		final long dt = currentTime - UselessMod.lastTimeUpdate;
		UselessMod.lastTimeUpdate = currentTime;

		if (dt > 0L) {
			UselessMod.mspt = Math.max(50.0, dt * 5e-8);
		}
	}

	@Redirect(method = "handleChunkData", at = @At(value = "INVOKE", target = "Ljava/util/Iterator;hasNext()Z", remap = false))
	private boolean replaceChunkDataBlockEntityLoop(Iterator<NBTTagCompound> iterator) {
		while (iterator.hasNext()) {
			final NBTTagCompound compound = iterator.next();
			final BlockPos pos = new BlockPos(compound.getInteger("x"), compound.getInteger("y"), compound.getInteger("z"));
			final boolean isPiston = compound.getString("id").equals("minecraft:piston");
			if (isPiston) {
				compound.setFloat("progress", Math.min(compound.getFloat("progress") + 0.5F, 1F));
			}

			TileEntity te = this.world.getTileEntity(pos);
			if (te != null) {
				te.readFromNBT(compound);
			} else {
				if (!isPiston) {
					continue;
				}

				final IBlockState state = this.world.getBlockState(pos);
				if (state.getBlock() != Blocks.PISTON_EXTENSION) {
					continue;
				}

				te = new TileEntityPiston();
				te.readFromNBT(compound);
				this.world.setTileEntity(pos, te);
				te.updateContainingBlockInfo();
			}
		}

		return false;
	}

	@Inject(method = "handleBlockAction", at = @At("RETURN"))
	private void onBlockData(SPacketBlockAction packetIn, CallbackInfo ci) {
		System.out.printf("%s %s %d %d%n", packetIn.getBlockPosition(), packetIn.getBlockType(), packetIn.getData1(), packetIn.getData2());
	}

	@Inject(method = "handleOpenWindow", at = @At("RETURN"))
	private void onHandleOpenWindow(SPacketOpenWindow packetIn, CallbackInfo ci) {
		System.out.printf("%d %s %s %d %d %s%n", packetIn.getWindowId(), packetIn.getGuiId(), packetIn.getWindowTitle(), packetIn.getSlotCount(), packetIn.getEntityId(), packetIn.hasSlots());
	}
}
