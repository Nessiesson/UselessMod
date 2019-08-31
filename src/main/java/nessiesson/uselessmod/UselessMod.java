package nessiesson.uselessmod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, clientSideOnly = true)
public class UselessMod {
	public static KeyBinding highlightEntities = new KeyBinding("key.uselessmod.highlight_entities", KeyConflictContext.IN_GAME, Keyboard.KEY_C, Reference.NAME);
	private static KeyBinding reloadAudioEngineKey = new KeyBinding("key.uselessmod.reload_audio", KeyConflictContext.IN_GAME, Keyboard.KEY_B, Reference.NAME);
	private static KeyBinding hideSidebarScoreboard = new KeyBinding("key.uselessmod.toggle_scoreboard", KeyConflictContext.IN_GAME, Keyboard.KEY_V, Reference.NAME);
	public static boolean isScoreboardHidden;
	public static long lastTimeUpdate;
	public static double mspt;
	private static Logger logger;
	private static Configuration config;
	private static Minecraft mc = Minecraft.getMinecraft();

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		MinecraftForge.EVENT_BUS.register(this);

		Display.setTitle(Display.getTitle() + " - " + mc.getSession().getUsername());
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		ClientRegistry.registerKeyBinding(reloadAudioEngineKey);
		ClientRegistry.registerKeyBinding(hideSidebarScoreboard);
		ClientRegistry.registerKeyBinding(highlightEntities);
	}

	@SubscribeEvent
	public void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals(Reference.MODID)) {
			ConfigManager.sync(Reference.MODID, Config.Type.INSTANCE);
		}
	}

	@SubscribeEvent
	public void onKeyPressed(InputEvent.KeyInputEvent event) {
		if (reloadAudioEngineKey.isPressed()) {
			mc.getSoundHandler().sndManager.reloadSoundSystem();
			this.debugFeedback("uselessmod.reload_audio");
		}

		if (hideSidebarScoreboard.isPressed()) {
			isScoreboardHidden = !isScoreboardHidden;
		}
	}

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			final EntityPlayerSP p = mc.player;
			if (Configuration.flightInertiaCancellation && p != null && p.capabilities.isFlying) {
				GameSettings s = mc.gameSettings;
				if (!(GameSettings.isKeyDown(s.keyBindForward) || GameSettings.isKeyDown(s.keyBindBack) || GameSettings.isKeyDown(s.keyBindLeft) || GameSettings.isKeyDown(s.keyBindRight))) {
					p.motionX = 0.0;
					p.motionZ = 0.0;
				}
			}
		}
	}

	private void debugFeedback(String string) {
		ITextComponent tag = new TextComponentTranslation("debug.prefix");
		ITextComponent text = new TextComponentTranslation(string);

		tag.setStyle(new Style().setColor(TextFormatting.YELLOW).setBold(true));
		ITextComponent message = new TextComponentString("").appendSibling(tag).appendText(" ").appendSibling(text);

		mc.ingameGUI.getChatGUI().printChatMessage(message);
	}
}
