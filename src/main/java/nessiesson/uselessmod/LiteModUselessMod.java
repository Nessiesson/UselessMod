package nessiesson.uselessmod;

import com.mumfrey.liteloader.Tickable;
import com.mumfrey.liteloader.core.LiteLoader;
import nessiesson.uselessmod.mixins.ISoundHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import java.io.File;

public class LiteModUselessMod implements Tickable {
	public static boolean isScoreboardHidden;
	public static long lastTimeUpdate;
	public static double mspt;
	private static KeyBinding reloadAudioEngineKey = new KeyBinding("Reload audio engine", Keyboard.KEY_B, "UselessMod");
	private static KeyBinding hideSidebarScoreboard = new KeyBinding("Hide sidebar scoreboard", Keyboard.KEY_V, "UselessMod");
	public static KeyBinding highlightEntities = new KeyBinding("Highlight Entities (Spectators)", Keyboard.KEY_C, "UselessMod");

	@Override
	public String getVersion() {
		return "@VERSION@";
	}

	@Override
	public void init(File configPath) {
		LiteLoader.getInput().registerKeyBinding(reloadAudioEngineKey);
		LiteLoader.getInput().registerKeyBinding(hideSidebarScoreboard);
		LiteLoader.getInput().registerKeyBinding(highlightEntities);

		Display.setTitle(Display.getTitle() + " - " + Minecraft.getMinecraft().getSession().getUsername());
	}

	@Override
	public void upgradeSettings(String version, File configPath, File oldConfigPath) {
	}

	@Override
	public String getName() {
		return "@NAME@";
	}

	@Override
	public void onTick(Minecraft minecraft, float partialTicks, boolean inGame, boolean clock) {
		if (!inGame) {
			return;
		}
		if (reloadAudioEngineKey.isPressed()) {
			((ISoundHandler) minecraft.getSoundHandler()).getSoundManager().reloadSoundSystem();
			this.debugFeedback("Reloaded sound engine");
		}
		if (hideSidebarScoreboard.isPressed()) {
			isScoreboardHidden = !isScoreboardHidden;
		}
	}

	private void debugFeedback(String string) {
		Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(
				(new TextComponentString(""))
						.appendSibling((new TextComponentString("[Debug]:"))
								.setStyle((new Style()).setColor(TextFormatting.YELLOW).setBold(true)))
						.appendText(" ")
						.appendSibling(new TextComponentString(string))
		);
	}
}
