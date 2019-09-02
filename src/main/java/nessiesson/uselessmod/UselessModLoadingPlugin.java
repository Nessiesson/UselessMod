package nessiesson.uselessmod;

import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import javax.annotation.Nullable;
import java.util.Map;

@IFMLLoadingPlugin.MCVersion(ForgeVersion.mcVersion)
public class UselessModLoadingPlugin implements IFMLLoadingPlugin {
	public UselessModLoadingPlugin() {
		MixinBootstrap.init();
		Mixins.addConfiguration("mixins.uselessmod.json");
	}

	// @formatter:off
	@Override public String getAccessTransformerClass() { return null; }
	@Override public String[] getASMTransformerClass() { return null; }
	@Nullable @Override public String getSetupClass() { return null; }
	@Override public String getModContainerClass() { return null; }
	@Override public void injectData(Map<String, Object> data) {}
	// @formatter:on
}
