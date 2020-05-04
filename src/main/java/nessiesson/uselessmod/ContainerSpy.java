package nessiesson.uselessmod;

import net.minecraft.block.BlockChest;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;

// 1. Query block and register position.
// 2. Wait for Open Window packet and Block Action packets (They are sent in that order).
// 3. Somehow try to figure out how to determine which window belongs to which block action.

public class ContainerSpy {
	final Minecraft mc = Minecraft.getMinecraft();
	final Map<BlockPos, ContainerInstance> map = new HashMap<>();

	public void queryBlock(final BlockPos pos) {
		//if (mc.world.getBlockState(pos).getBlock() instanceof BlockContainer) {
		if (mc.world.getBlockState(pos).getBlock() instanceof BlockChest) {
			mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, EnumFacing.DOWN, EnumHand.MAIN_HAND, 0F, 0F, 0F));
		}
	}

	public static class ContainerInstance {
		private int users;
		private NonNullList<ItemStack> inv;
	}
}
