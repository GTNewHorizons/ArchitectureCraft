package gcewing.architecture.client.render;

import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;

import gcewing.architecture.client.render.target.IRenderTarget;
import gcewing.architecture.compat.BlockPos;
import gcewing.architecture.compat.EnumWorldBlockLayer;
import gcewing.architecture.compat.IBlockState;
import gcewing.architecture.compat.Trans3;

public interface ICustomRenderer {

    void renderBlock(IBlockAccess world, BlockPos pos, IBlockState state, IRenderTarget target,
            EnumWorldBlockLayer layer, Trans3 t);

    void renderItemStack(ItemStack stack, IRenderTarget target, Trans3 t);
}
