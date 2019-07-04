package nessiesson.uselessmod;

import com.mumfrey.liteloader.Tickable;
import com.mumfrey.liteloader.core.LiteLoader;
import nessiesson.uselessmod.mixins.ISoundHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import java.io.File;

// [10:42 PM] Robi: nessie go add ctrl mousewheel to useful mod so u can instant resize gui xd

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
		EntityPlayerSP p = minecraft.player;
		if (p.capabilities.isFlying) {
			GameSettings s = minecraft.gameSettings;
			if (!(GameSettings.isKeyDown(s.keyBindForward) || GameSettings.isKeyDown(s.keyBindBack) || GameSettings.isKeyDown(s.keyBindLeft) || GameSettings.isKeyDown(s.keyBindRight))) {
				p.motionX = 0.0;
				p.motionZ = 0.0;
			}
		}
	}

	private void debugFeedback(String string) {
		ITextComponent tag = new TextComponentTranslation("debug.prefix");
		ITextComponent text = new TextComponentString(string);

		tag.setStyle(new Style().setColor(TextFormatting.YELLOW).setBold(true));
		ITextComponent message = new TextComponentString("").appendSibling(tag).appendText(" ").appendSibling(text);

		Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(message);
	}
}
