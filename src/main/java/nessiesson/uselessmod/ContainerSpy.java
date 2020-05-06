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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

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

	public void startFindingInventories() {
		final EntityPlayerSP player = this.mc.player;
		final double x = player.posX;
		final double y = player.posY;
		final double z = player.posZ;
		final float range = this.mc.playerController.getBlockReachDistance();
		final float rangeSq = this.mc.playerController.getBlockReachDistance();
		final BlockPos from = new BlockPos(x - range, y - range, z - range);
		final BlockPos to = new BlockPos(x + range, y + range, z + range);
		this.sendStartPacket();
		for (final BlockPos pos : BlockPos.getAllInBox(from, to)) {
			final Block block = this.mc.world.getBlockState(pos).getBlock();
			System.out.println(block + " " + player.getDistanceSq(pos) + " " + rangeSq);
			if (player.getDistanceSq(pos) < rangeSq && block instanceof BlockChest) {
				player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, EnumFacing.DOWN, EnumHand.MAIN_HAND, 0F, 0F, 0F));
				player.connection.sendPacket(new CPacketCloseWindow());
			}
		}

		this.sendStopPacket();
	}

	public void onOpenWindow(final int windowId, final int slotCount) {
		System.out.println(windowId + " " + slotCount);
	}

	public void onGetContent(final int windowId, final List<ItemStack> stacks) {
		System.out.println(windowId + " " + stacks);
	}

	public void onChatReceived(final ITextComponent component) {
		System.out.println((component instanceof TextComponentString) + " " + (component instanceof TextComponentTranslation));
	}

	private void sendStartPacket() {
		this.mc.player.connection.sendPacket(new CPacketChatMessage("/help " + this.startId));
	}

	private void sendStopPacket() {
		this.mc.player.connection.sendPacket(new CPacketChatMessage("/help " + this.stopId));
	}

	private static class SimpleContainer {
		private int windowId;
		private List<ItemStack> inv;
	}
}