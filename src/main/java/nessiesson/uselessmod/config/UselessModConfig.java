package nessiesson.uselessmod.config;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mumfrey.liteloader.core.LiteLoader;
import com.mumfrey.liteloader.modconfig.ConfigStrategy;
import com.mumfrey.liteloader.modconfig.Exposable;
import com.mumfrey.liteloader.modconfig.ExposableOptions;

@ExposableOptions(strategy = ConfigStrategy.Unversioned, filename = "uselessmod.json")
public class UselessModConfig implements Exposable {
	private static UselessModConfig instance;
	@Expose
	@SerializedName("mining_ghostblock_fix")
	public boolean isMiningGhostblockFixEnabled = true;
	@Expose
	@SerializedName("show_block_breaking_particles")
	public boolean showBlockBreakingParticles = false;
	@Expose
	@SerializedName("smaller_item_render_range")
	public boolean smallerItemRenderRange = false;

	public UselessModConfig() {
		if (instance == null) {
			instance = this;
			LiteLoader.getInstance().registerExposable(instance, null);
		}
	}

	static void save() {
		LiteLoader.getInstance().writeConfig(instance);
	}
}
