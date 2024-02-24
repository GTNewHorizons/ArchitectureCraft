package gcewing.architecture.legacy.rendering;

import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;

import gcewing.architecture.compat.BlockPos;
import gcewing.architecture.compat.Trans3;
import gcewing.architecture.legacy.blocks.EnumWorldBlockLayer;
import gcewing.architecture.legacy.blocks.IBlockState;

public interface ICustomRenderer {

    void renderBlock(IBlockAccess world, BlockPos pos, IBlockState state, IRenderTarget target,
            EnumWorldBlockLayer layer, Trans3 t);

    void renderItemStack(ItemStack stack, IRenderTarget target, Trans3 t);
}
