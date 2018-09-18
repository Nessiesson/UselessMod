package nessiesson.uselessmod.mixins;

import net.minecraft.block.Block;
import net.minecraft.block.BlockObserver;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraftforge.registries.IForgeRegistryInternal;
import net.minecraftforge.registries.RegistryManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Mixin-implementation of https://github.com/MinecraftForge/MinecraftForge/commit/52702e5cc9c629b3b2baff6a726aabb168aced16
@Mixin(targets = "net/minecraftforge/registries/GameData$BlockCallbacks")
public abstract class MixinMinecraftForgeRegistriesGameData {
	private Block mixinBlock;

	@Inject(method = "onAdd", at = @At("HEAD"), remap = false)
	private void setMixinBlock(IForgeRegistryInternal<Block> owner, RegistryManager stage, int id, Block block, Block oldBlock, CallbackInfo ci) {
		this.mixinBlock = block;
	}

	@Redirect(method = "onAdd", at = @At(value = "INVOKE", ordinal = 2, target = "Lnet/minecraftforge/registries/GameData$ClearableObjectIntIdentityMap;put(Ljava/lang/Object;I)V"), remap = false)
	private void poop(@Coerce ObjectIntIdentityMap<Object> map, Object key, int value) {
		if (this.mixinBlock.getClass() != BlockObserver.class) {
			map.put(key, value);
		}
	}
}
