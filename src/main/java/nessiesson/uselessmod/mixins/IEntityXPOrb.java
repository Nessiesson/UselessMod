package nessiesson.uselessmod.mixins;

import net.minecraft.entity.item.EntityXPOrb;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityXPOrb.class)
public interface IEntityXPOrb {
	@Accessor
	void setXpValue(int xp);
}
