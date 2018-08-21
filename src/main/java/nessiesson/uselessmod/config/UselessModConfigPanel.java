package nessiesson.uselessmod.config;

import com.mumfrey.liteloader.client.gui.GuiCheckbox;
import com.mumfrey.liteloader.modconfig.AbstractConfigPanel;
import com.mumfrey.liteloader.modconfig.ConfigPanelHost;
import nessiesson.uselessmod.LiteModUselessMod;
import net.minecraft.entity.item.EntityItem;

public class UselessModConfigPanel extends AbstractConfigPanel {
	@Override
	protected void addOptions(ConfigPanelHost host) {
		final int SPACING = 16;
		int controlId = 0;

		this.addControl(new GuiCheckbox(controlId, 0, SPACING * controlId++, "Enable client-side ghost block mining fix."), new ConfigOptionListener<GuiCheckbox>() {
			@Override
			public void actionPerformed(GuiCheckbox control) {
				LiteModUselessMod.config.isMiningGhostblockFixEnabled = control.checked = !control.checked;
			}
		}).checked = LiteModUselessMod.config.isMiningGhostblockFixEnabled;

		this.addControl(new GuiCheckbox(controlId, 0, SPACING * controlId++, "Show block breaking particles."), new ConfigOptionListener<GuiCheckbox>() {
			@Override
			public void actionPerformed(GuiCheckbox control) {
				LiteModUselessMod.config.showBlockBreakingParticles = control.checked = !control.checked;
			}
		}).checked = LiteModUselessMod.config.showBlockBreakingParticles;

		this.addControl(new GuiCheckbox(controlId, 0, SPACING * controlId++, "Smaller item render distance."), new ConfigOptionListener<GuiCheckbox>() {
			@Override
			public void actionPerformed(GuiCheckbox control) {
				LiteModUselessMod.config.smallerItemRenderRange = control.checked = !control.checked;
			}
		}).checked = LiteModUselessMod.config.smallerItemRenderRange;
	}

	@Override
	public String getPanelTitle() {
		return "UselessConfiguration";
	}

	@Override
	public void onPanelHidden() {
		UselessModConfig.save();
	}
}
