package nessiesson.uselessmod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, clientSideOnly = true)
public class UselessMod {
	public static final KeyBinding highlightEntities = new KeyBinding("key.uselessmod.highlight_entities", KeyConflictContext.IN_GAME, Keyboard.KEY_C, Reference.NAME);
	public static Map<AxisAlignedBB, Integer> beaconsToRender = new HashMap<>();
	public static Map<String, List<ChatLine>> chatHistory = new HashMap<>();
	public static Map<String, List<String>> tabCompleteHistory = new HashMap<>();
	public static boolean toggleBeaconArea = false;
	public static long lastTimeUpdate;
	public static ContainerSpy spy;
	public static double mspt;
	public static int sendPacketsThisTick = 0;
	public static int[] sendPackets = new int[20];
	public static int receivedPacketsThisTick = 0;
	public static int[] receivedPackets = new int[20];
	public static ServerData currentServer;
	public static long tickCounter = 0;

	private static final KeyBinding reloadAudioEngineKey = new KeyBinding("key.uselessmod.reload_audio", KeyConflictContext.IN_GAME, Keyboard.KEY_B, Reference.NAME);
	private static final KeyBinding toggleBeaconAreaKey = new KeyBinding("key.uselessmod.toggle_beacon_area", KeyConflictContext.IN_GAME, Keyboard.KEY_J, Reference.NAME);
	private static final KeyBinding spyKey = new KeyBinding("key.uselessmod.spy", KeyConflictContext.IN_GAME, Keyboard.KEY_Y, Reference.NAME);
	private static final Minecraft mc = Minecraft.getMinecraft();
	private static final StepAssistHelper stepAssistHelper = new StepAssistHelper();
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
		ClientRegistry.registerKeyBinding(toggleBeaconAreaKey);
		ClientRegistry.registerKeyBinding(highlightEntities);
		ClientRegistry.registerKeyBinding(spyKey);
		spy = new ContainerSpy();
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

		if (toggleBeaconAreaKey.isPressed()) {
			toggleBeaconArea = !toggleBeaconArea;
		}

		if (spyKey.isPressed()) {
			if (mc.player.isSneaking()) {
				spy.resetChests();
			} else {
				spy.startFindingInventories();
			}
		}
	}

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		this.tickCounter++;
		if(currentServer != null) {
			System.out.println(currentServer.serverIP);
		}
		if (mc.world != null) {
			if (!this.mc.isIntegratedServerRunning()) {
				UselessMod.currentServer = this.mc.getCurrentServerData();
			}

			for (AxisAlignedBB axisalignedbb : UselessMod.beaconsToRender.keySet()) {
				UselessMod.beaconsToRender.put(axisalignedbb, UselessMod.beaconsToRender.get(axisalignedbb) - 1);
			}
			if (!mc.player.isSneaking() && spyKey.isKeyDown() && mc.world.getTotalWorldTime() % 10 == 0) {
				spy.startFindingInventories();
			}
			for(int i = sendPackets.length-1; i > 0; --i) {
				sendPackets[i] = sendPackets[i-1];
			}
			sendPackets[0] = sendPacketsThisTick;
			for(int i = receivedPackets.length-1; i > 0; --i) {
				receivedPackets[i] = receivedPackets[i-1];
			}
			receivedPackets[0] = receivedPacketsThisTick;
			sendPacketsThisTick = 0;
			receivedPacketsThisTick = 0;
		}

		if (event.phase == TickEvent.Phase.END) {
			final EntityPlayerSP player = mc.player;
			if(player != null) {
				stepAssistHelper.update(player);
			}
			if (Configuration.noFall && player.fallDistance > 2F && !player.isElytraFlying()) {
				player.connection.sendPacket(new CPacketPlayer(true));
			}

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

	private void updateTitle() {
		Display.setTitle(this.originalTitle + " - " + mc.getSession().getUsername());
	}

	private void debugFeedback() {
		final ITextComponent tag = new TextComponentTranslation("debug.prefix");
		final ITextComponent text = new TextComponentTranslation("uselessmod.reload_audio");
		tag.setStyle(new Style().setColor(TextFormatting.YELLOW).setBold(true));
		final ITextComponent message = new TextComponentString("").appendSibling(tag).appendText(" ").appendSibling(text);
		mc.ingameGUI.getChatGUI().printChatMessage(message);
	}
}
