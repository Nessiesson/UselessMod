package nessiesson.uselessmod;

import com.mumfrey.liteloader.Configurable;
import com.mumfrey.liteloader.Tickable;
import com.mumfrey.liteloader.core.LiteLoader;
import com.mumfrey.liteloader.modconfig.ConfigPanel;
import nessiesson.uselessmod.config.UselessModConfig;
import nessiesson.uselessmod.config.UselessModConfigPanel;
import nessiesson.uselessmod.mixins.ISoundHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;

import java.io.File;

public class LiteModUselessMod implements Tickable, Configurable {
	public static UselessModConfig config = new UselessModConfig();
	private static KeyBinding reloadAudioEngineKey = new KeyBinding("Reload audio engine", Keyboard.KEY_B, "UselessMod");

	@Override
	public String getVersion() {
		return "@VERSION@";
	}

	@Override
	public void init(File configPath) {
		LiteLoader.getInput().registerKeyBinding(reloadAudioEngineKey);
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
		if (reloadAudioEngineKey.isPressed()) {
			((ISoundHandler) minecraft.getSoundHandler()).getSoundManager().reloadSoundSystem();
			this.debugFeedback("Reloaded sound engine");
		}
	}

	@Override
	public Class<? extends ConfigPanel> getConfigPanelClass() {
		return UselessModConfigPanel.class;
	}

	//Stolen from mc source code.
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
