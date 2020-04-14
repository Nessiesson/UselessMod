package nessiesson.uselessmod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, clientSideOnly = true)
public class UselessMod {
	public final static KeyBinding highlightEntities = new KeyBinding("key.uselessmod.highlight_entities", KeyConflictContext.IN_GAME, Keyboard.KEY_C, Reference.NAME);
	private static final float DEFAULT_MOVEMENT_SPEED = 0.6F;
	private static final Minecraft mc = Minecraft.getMinecraft();
	private final static KeyBinding reloadAudioEngineKey = new KeyBinding("key.uselessmod.reload_audio", KeyConflictContext.IN_GAME, Keyboard.KEY_B, Reference.NAME);
	public static long lastTimeUpdate;
	public static double mspt;
	private String originalTitle;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
		this.originalTitle = Display.getTitle();
		updateTitle();
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		ClientRegistry.registerKeyBinding(reloadAudioEngineKey);
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
			this.debugFeedback();
		}
	}

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			final EntityPlayerSP player = mc.player;
			if (Configuration.flightInertiaCancellation && player != null && player.capabilities.isFlying) {
				final GameSettings settings = mc.gameSettings;
				if (!(GameSettings.isKeyDown(settings.keyBindForward) || GameSettings.isKeyDown(settings.keyBindBack) || GameSettings.isKeyDown(settings.keyBindLeft) || GameSettings.isKeyDown(settings.keyBindRight))) {
					player.motionX = player.motionZ = 0D;
				}
			}
		}
	}

	@SubscribeEvent
	public void onGuiChanged(GuiOpenEvent event) {
		if (event.getGui() instanceof GuiMultiplayer) {
			this.updateTitle();
		}
	}

	@SubscribeEvent
	public void onLivingUpdateEvent(LivingEvent.LivingUpdateEvent event) {
		if (!(event.getEntityLiving() instanceof EntityPlayerSP)) {
			return;
		}

		final EntityPlayerSP player = (EntityPlayerSP) event.getEntityLiving();
		if (Configuration.stepAssist) {
			player.stepHeight = player.isSneaking() ? DEFAULT_MOVEMENT_SPEED : 1.5F;
		} else {
			player.stepHeight = DEFAULT_MOVEMENT_SPEED;
		}
	}

	private void updateTitle() {
		Display.setTitle(this.originalTitle + " - " + Minecraft.getMinecraft().getSession().getUsername());
	}

	private void debugFeedback() {
		final ITextComponent tag = new TextComponentTranslation("debug.prefix");
		final ITextComponent text = new TextComponentTranslation("uselessmod.reload_audio");
		tag.setStyle(new Style().setColor(TextFormatting.YELLOW).setBold(true));
		final ITextComponent message = new TextComponentString("").appendSibling(tag).appendText(" ").appendSibling(text);
		mc.ingameGUI.getChatGUI().printChatMessage(message);
	}
}
