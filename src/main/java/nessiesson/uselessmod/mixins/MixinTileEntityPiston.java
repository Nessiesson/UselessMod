package nessiesson.uselessmod.mixins;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TileEntityPiston.class)
public abstract class MixinTileEntityPiston extends TileEntity {
	@Shadow
	private float progress;

	/**
	 * @author nessie
	 * @reason lazy
	 */
	@SideOnly(Side.CLIENT)
	@Overwrite
	public float getProgress(float partialTicks) {
		if (this.tileEntityInvalid && Math.abs(this.progress - 1F) < 1E-5F) {
			return 1F;
		}

		return Math.min(1F, (2F * this.progress + partialTicks) / 3F);
	}
}
