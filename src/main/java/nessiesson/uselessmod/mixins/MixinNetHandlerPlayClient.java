package nessiesson.uselessmod.mixins;

import nessiesson.uselessmod.LiteModUselessMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketCollectItem;
import net.minecraft.network.play.server.SPacketSetSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(NetHandlerPlayClient.class)
public abstract class MixinNetHandlerPlayClient {
	@Shadow
	private Minecraft client;

	//TODO: handle armour and offhand slots.
	@Inject(method = "handleSetSlot", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void playToolBreakSound(SPacketSetSlot packetIn, CallbackInfo ci, EntityPlayer player, ItemStack stack, int i) {
		if (packetIn.getWindowId() == 0 && LiteModUselessMod.shouldPlayBreakSound && stack.isEmpty() && player.inventory.currentItem == i - 36) {
			LiteModUselessMod.shouldPlayBreakSound = false;
			this.client.player.renderBrokenItemStack(LiteModUselessMod.whichToolShouldBreak);
		}
	}

	@Inject(method = "handleCollectItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/WorldClient;playSound(DDDLnet/minecraft/util/SoundEvent;Lnet/minecraft/util/SoundCategory;FFZ)V", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
	private void simulateMending(SPacketCollectItem packetIn, CallbackInfo ci, Entity entity, EntityLivingBase player) {
		EntityXPOrb orb = (EntityXPOrb) entity;
		ItemStack itemstack = EnchantmentHelper.getEnchantedItem(Enchantments.MENDING, player);

		if (!itemstack.isEmpty() && itemstack.isItemDamaged()) {
			int i = Math.min(this.xpToDurability(orb.getXpValue()), itemstack.getItemDamage());
			((IEntityXPOrb) orb).setXpValue(orb.getXpValue() - this.durabilityToXp(i));
			itemstack.setItemDamage(itemstack.getItemDamage() - i);
		}
	}

	private int durabilityToXp(int durability) {
		return durability / 2;
	}

	private int xpToDurability(int xp) {
		return xp * 2;
	}
}
