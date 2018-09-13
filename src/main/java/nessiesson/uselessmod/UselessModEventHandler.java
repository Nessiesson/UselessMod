package nessiesson.uselessmod;

import net.minecraft.client.gui.BossInfoClient;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class UselessModEventHandler {
	private static final String[] op = {
			"rid is hella gae", "CARPETMOD IS NOT VANILLA IDIOT",
			"mango has best hot streams ofc", "duping is for lazy idiots", "man are u actually retarded?",
			"mind you r own business", "more vanilla than spigot", "I'll build a statue of you on the SciCraft server!",
			"btw u heard of the new scicrap guy moddit?", "only lazy jerks will read this message.", "moronzz best!",
			"JC Denton. 23 years old. No residence. No ancestors. No employer. No -",
			"I am a prototype for a much larger system.", "kk ur floor is burning"
	};

	@SubscribeEvent
	public static void onRenderBossBar(RenderGameOverlayEvent.BossInfo event) {
		final BossInfoClient bossInfoClient = event.getBossInfo();
		final int i = Math.abs(bossInfoClient.getUniqueId().hashCode());
		bossInfoClient.setName(new TextComponentString(op[i % op.length]));
	}
}
