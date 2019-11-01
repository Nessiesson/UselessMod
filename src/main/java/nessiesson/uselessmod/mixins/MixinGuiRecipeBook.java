package nessiesson.uselessmod.mixins;

import nessiesson.uselessmod.Configuration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.recipebook.GuiRecipeBook;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.InventoryCrafting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiRecipeBook.class)
public abstract class MixinGuiRecipeBook {
	@Shadow
	private InventoryCrafting craftingSlots;

	@Inject(method = "updateStackedContents", at = @At("RETURN"))
	private void craftingHax(CallbackInfo ci) {
		if (Configuration.hackerman && !this.craftingSlots.isEmpty()) {
			final Minecraft mc = Minecraft.getMinecraft();
			final EntityPlayerSP player = mc.player;
			if (GuiScreen.isShiftKeyDown() && GuiScreen.isCtrlKeyDown()) {
				mc.playerController.windowClick(player.openContainer.windowId, 0, 1, ClickType.QUICK_MOVE, player);
			}
		}
	}
}
