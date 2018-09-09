package nessiesson.uselessmod.mixins;

import net.minecraft.client.gui.GuiFlatPresets;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.FlatLayerInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;

@Mixin(GuiFlatPresets.class)
public abstract class MixinGuiFlatPresets {
	@Shadow
	private static void registerPreset(String name, Item icon, Biome biome, List<String> features, FlatLayerInfo... layers) {
	}

	@Inject(method = "<clinit>", at = @At(value = "INVOKE", ordinal = 0, shift = At.Shift.BEFORE, target = "Lnet/minecraft/client/gui/GuiFlatPresets;registerPreset(Ljava/lang/String;Lnet/minecraft/item/Item;Lnet/minecraft/world/biome/Biome;Ljava/util/List;[Lnet/minecraft/world/gen/FlatLayerInfo;)V"))
	private static void addSciCraftFlatPreset(CallbackInfo ci) {
		MixinGuiFlatPresets.registerPreset("SciCraft flatworld", Item.getItemFromBlock(Blocks.STAINED_GLASS), Biomes.FOREST, Arrays.asList(""), new FlatLayerInfo(1, Blocks.STAINED_GLASS));
	}
}
