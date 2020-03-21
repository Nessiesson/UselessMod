package nessiesson.uselessmod.mixins;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockLiquid.class)
public abstract class MixinBlockLiquid extends Block {
	public MixinBlockLiquid(Material material) {
		super(material);
	}

	@Override
	public boolean isTranslucent(IBlockState state) {
		return true;
	}
}
