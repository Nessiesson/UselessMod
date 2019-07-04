package nessiesson.uselessmod.mixins;

import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Mixin(World.class)
public abstract class MixinWorld {
	@Shadow
	@Final
	public Profiler profiler;
	@Shadow
	@Final
	public List<TileEntity> tickableTileEntities;
	@Shadow
	@Final
	public List<TileEntity> loadedTileEntityList;
	@Shadow
	@Final
	private List<TileEntity> tileEntitiesToBeRemoved;
	private TileEntity te;

	@Inject(method = "updateEntities", at = @At(value = "INVOKE", target = "Ljava/lang/Class;getSimpleName()Ljava/lang/String;", remap = false), locals = LocalCapture.CAPTURE_FAILSOFT)
	private void keepACopy(CallbackInfo ci, Iterator iterator, TileEntity tileentity) {
		this.te = tileentity;
	}

	@Redirect(method = "updateEntities", at = @At(value = "INVOKE", target = "Ljava/lang/Class;getSimpleName()Ljava/lang/String;", remap = false))
	private String mc117087(Class aClass) {
		if (this.profiler.profilingEnabled) {
			return te.getClass().getSimpleName();
		}
		return "";
	}

	@Inject(method = "updateEntities", at = @At(value = "FIELD", target = "Lnet/minecraft/world/World;tileEntitiesToBeRemoved:Ljava/util/List;", ordinal = 0))
	private void tileEntityRemovalFix(CallbackInfo ci) {
		if (!this.tileEntitiesToBeRemoved.isEmpty()) {
			Set<TileEntity> remove = Collections.newSetFromMap(new java.util.IdentityHashMap<>());
			remove.addAll(this.tileEntitiesToBeRemoved);
			this.tickableTileEntities.removeAll(remove);
			this.loadedTileEntityList.removeAll(remove);
			this.tileEntitiesToBeRemoved.clear();
		}
	}
}
