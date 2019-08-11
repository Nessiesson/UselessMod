package nessiesson.uselessmod.mixins;

import com.google.common.collect.Multimap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Comparator;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {
	@Shadow
	public abstract NBTTagList getEnchantmentTagList();

	@Redirect(method = "getTooltip", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/Multimap;isEmpty()Z", remap = false))
	private boolean noAttributes(Multimap multimap) {
		return true;
	}

	@Redirect(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getEnchantmentTagList()Lnet/minecraft/nbt/NBTTagList;"))
	private NBTTagList sortEnchantments(ItemStack item) {
		final NBTTagList list = this.getEnchantmentTagList();
		final Comparator comp = Comparator.<NBTTagCompound>comparingInt(t -> t.getShort("lvl"))
				.thenComparingInt(t -> t.getShort("id"))
				.reversed();
		((INBTTagList) list).getTagList().sort(comp);
		return list;
	}
}
