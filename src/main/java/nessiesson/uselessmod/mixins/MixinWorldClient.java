package nessiesson.uselessmod.mixins;

import nessiesson.uselessmod.Configuration;
import nessiesson.uselessmod.UselessMod;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.profiler.Profiler;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldClient.class)
public abstract class MixinWorldClient extends World {
	protected MixinWorldClient(ISaveHandler ish, WorldInfo wi, WorldProvider wp, Profiler p, boolean c) {
		super(ish, wi, wp, p, c);
	}

	@Inject(method = "<init>", at = @At("RETURN"))
	private void updateTitle(NetHandlerPlayClient npc, WorldSettings ws, int d, EnumDifficulty ed, Profiler p, CallbackInfo ci) {
		UselessMod.updateTitle();
	}

	@Override
	public void updateEntity(Entity entity) {
		if (Configuration.clientEntityUpdates || entity instanceof EntityPlayer || entity instanceof EntityFireworkRocket) {
			super.updateEntity(entity);
		}
	}

	@Override
	public float getRainStrength(float delta) {
		return Configuration.showRain ? this.prevRainingStrength + (this.rainingStrength - this.prevRainingStrength) * delta : 0F;
	}
}