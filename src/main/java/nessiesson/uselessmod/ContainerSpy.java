package nessiesson.uselessmod;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 1. Query block and register position.
// 2. Wait for Open Window packet and Block Action packets (They are sent in that order).
// 3. Somehow try to figure out how to determine which window belongs to which block action.

// ALTERNATIVELY:
// 1. Query block and register position, type, and order.
// 2. Keep track of stuff...
// 3. Assign position in order of blocks successfully(?) opened.

// ALTERNATIVELY:
// 1. Query block and register position, type, and order.
// 2. Use some info packet sent from client to server
// 3. `/help NUMBER`

public class ContainerSpy {
	private final int startId = 2137433547;
	private final int stopId = startId + 1;
	private final Minecraft mc = Minecraft.getMinecraft();
	private final Map<BlockPos, SimpleContainer> map = new HashMap<>();
	private final List<BlockPos> fifo = new ArrayList<>();
	private boolean isRunning = false;

	public void startFindingInventories() {
		final EntityPlayerSP player = this.mc.player;
		final double x = player.posX;
		final double y = player.posY;
		final double z = player.posZ;
		final float range = this.mc.playerController.getBlockReachDistance();
		final float rangeSq = range * range;
		final BlockPos from = new BlockPos(x - range, y - range, z - range);
		final BlockPos to = new BlockPos(x + range, y + range, z + range);
		this.sendStart();
		for (final BlockPos pos : BlockPos.getAllInBox(from, to)) {
			final Block block = this.mc.world.getBlockState(pos).getBlock();
			if (player.getDistanceSq(pos) < rangeSq && block instanceof BlockChest) {
				this.fifo.add(pos);
				player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, EnumFacing.DOWN, EnumHand.MAIN_HAND, 0F, 0F, 0F));
				player.connection.sendPacket(new CPacketCloseWindow());
			}
		}

		this.sendStop();
	}

	public boolean onOpenWindow(final int windowId, final int slotCount) {
		if (!this.isRunning) {
			return false;
		}

		final BlockPos current = this.fifo.get(0);
		this.map.put(current, new SimpleContainer(windowId, slotCount));
		this.fifo.remove(0);
		return true;
	}

	// https://wiki.vg/index.php?title=Protocol&oldid=14204#Open_Window
	public void onGetContent(final int windowId, final List<ItemStack> stacks) {
		System.out.println(stacks);
	}

	public boolean onChatReceived(final ITextComponent componentIn) {
		if (!(componentIn instanceof TextComponentTranslation)) {
			return false;
		}

		final TextComponentTranslation component = (TextComponentTranslation) componentIn;
		if (!component.getKey().equals("commands.generic.num.tooBig")) {
			return false;
		}

		final int returnedId = Integer.parseInt(String.valueOf(component.getFormatArgs()[0]));
		if (returnedId == this.startId) {
			this.isRunning = true;
		} else if (returnedId == this.stopId) {
			this.isRunning = false;
		}

		return returnedId == this.startId || returnedId == this.stopId;
	}

	private void sendStart() {
		this.mc.player.connection.sendPacket(new CPacketChatMessage("/help " + this.startId));
	}

	private void sendStop() {
		this.mc.player.connection.sendPacket(new CPacketChatMessage("/help " + this.stopId));
	}

	private static class SimpleContainer {
		private int windowId;
		private List<ItemStack> inv;

		public SimpleContainer(final int id, final int slotCount) {
			this.windowId = id;
			this.inv = NonNullList.withSize(slotCount, ItemStack.EMPTY);
		}
	}
}
