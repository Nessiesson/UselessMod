package nessiesson.uselessmod.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.audio.PositionedSoundRecord;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MusicTicker.class)
public abstract class MixinMusicTicker {
	@Shadow
	@Final
	private Minecraft mc;
	@Shadow
	private ISound currentMusic;
	@Shadow
	private int timeUntilNextMusic;

	//TODO: stop overwrite.
	@Overwrite
	public void update() {
		MusicTicker.MusicType musicType = this.mc.getAmbientMusicType();

		if (this.currentMusic != null) {
			if (!musicType.getMusicLocation().getSoundName().equals(this.currentMusic.getSoundLocation())) {
				this.mc.getSoundHandler().stopSound(this.currentMusic);
				this.timeUntilNextMusic = 100;
			}

			if (!this.mc.getSoundHandler().isSoundPlaying(this.currentMusic)) {
				this.currentMusic = null;
				this.timeUntilNextMusic = 100;
			}
		}

		if (this.currentMusic == null && this.timeUntilNextMusic-- <= 0) {
			this.currentMusic = PositionedSoundRecord.getMusicRecord(musicType.getMusicLocation());
			this.mc.getSoundHandler().playSound(this.currentMusic);
			this.timeUntilNextMusic = 100;
		}
	}
}
