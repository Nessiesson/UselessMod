package nessiesson.uselessmod.mixins;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase extends Entity {
	public MixinEntityLivingBase(World worldIn) {
		super(worldIn);
	}

	@Redirect(method = "travel", at = @At(value = "FIELD", ordinal = 1,
			target = "Lnet/minecraft/world/World;isRemote:Z"))
	private boolean fixElytraLanding(World world) {
		return world.isRemote && !((Object) this instanceof EntityPlayer);
	}
}