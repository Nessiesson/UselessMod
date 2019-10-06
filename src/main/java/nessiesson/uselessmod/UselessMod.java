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
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, clientSideOnly = true)
public class UselessMod {
	final public static KeyBinding highlightEntities = new KeyBinding("key.uselessmod.highlight_entities", KeyConflictContext.IN_GAME, Keyboard.KEY_C, Reference.NAME);
	final private static KeyBinding reloadAudioEngineKey = new KeyBinding("key.uselessmod.reload_audio", KeyConflictContext.IN_GAME, Keyboard.KEY_B, Reference.NAME);
	final private static KeyBinding hideSidebarScoreboard = new KeyBinding("key.uselessmod.toggle_scoreboard", KeyConflictContext.IN_GAME, Keyboard.KEY_V, Reference.NAME);
	public static boolean isScoreboardHidden;
	public static long lastTimeUpdate;
	public static double mspt;
	final private Minecraft mc = Minecraft.getMinecraft();
	private static String originalTitle;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
		originalTitle = Display.getTitle();
		updateTitle();
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
			this.mc.getSoundHandler().sndManager.reloadSoundSystem();
			this.debugFeedback();
		}

		if (hideSidebarScoreboard.isPressed()) {
			isScoreboardHidden = !isScoreboardHidden;
		}
	}

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			final EntityPlayerSP p = this.mc.player;
			if (Configuration.flightInertiaCancellation && p != null && p.capabilities.isFlying) {
				final GameSettings s = this.mc.gameSettings;
				if (!(GameSettings.isKeyDown(s.keyBindForward) || GameSettings.isKeyDown(s.keyBindBack) || GameSettings.isKeyDown(s.keyBindLeft) || GameSettings.isKeyDown(s.keyBindRight))) {
					p.motionX = p.motionZ = 0D;
				}
			}
		}
	}

	public static void updateTitle() {
		Display.setTitle(originalTitle + " - " + Minecraft.getMinecraft().getSession().getUsername());
	}

	private void debugFeedback() {
		ITextComponent tag = new TextComponentTranslation("debug.prefix");
		ITextComponent text = new TextComponentTranslation("uselessmod.reload_audio");
		tag.setStyle(new Style().setColor(TextFormatting.YELLOW).setBold(true));
		ITextComponent message = new TextComponentString("").appendSibling(tag).appendText(" ").appendSibling(text);
		this.mc.ingameGUI.getChatGUI().printChatMessage(message);
	}
}
