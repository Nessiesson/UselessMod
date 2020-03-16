package nessiesson.uselessmod;

import net.minecraftforge.common.config.Config;

@Config(modid = Reference.MODID)
public class Configuration {
	public static boolean alwaysRenderTileEntities = false;
	public static boolean clientEntityUpdates = true;
	public static boolean flightInertiaCancellation = false;
	public static boolean showBlockBreakingParticles = true;
	public static boolean insaneBlockBreakingParticles = false;
	public static boolean showDeathAnimations = true;
	public static boolean showRain = true;
	public static boolean showServerNames = true;
	public static boolean showShulkerBoxDisplay = false;
	public static boolean sortEnchantmentTooltip = false;
	public static boolean hackerman = false;
	public static boolean showIdealToolMarker = false;
	@Config.RangeInt(min = 0)
	public static int speedyPlace = 4;
	@Config.SlidingOption
	@Config.RangeDouble(min = 0D, max = 1D)
	public static double spectatorMaxSpeed = 0.2;
}
