package nessiesson.uselessmod;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.RayTraceResult;

public class CommandSpy extends CommandBase {
	@Override
	public String getName() {
		return "spy";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "spy";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
		final Minecraft mc = Minecraft.getMinecraft();
		final RayTraceResult vec = mc.objectMouseOver;
		if (vec.typeOfHit != RayTraceResult.Type.BLOCK) return;
		UselessMod.spy.queryBlock(vec.getBlockPos());
	}
}
