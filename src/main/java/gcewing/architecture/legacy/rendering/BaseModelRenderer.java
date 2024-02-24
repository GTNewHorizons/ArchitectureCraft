// ------------------------------------------------------------------------------------------------
//
// Greg's Mod Base for 1.7 Version B - Render block using model + textures
//
// ------------------------------------------------------------------------------------------------

package gcewing.architecture.legacy.rendering;

// import net.minecraft.block.state.IBlockState;

import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;

import gcewing.architecture.compat.BlockPos;
import gcewing.architecture.compat.Trans3;
import gcewing.architecture.compat.Vector3;
import gcewing.architecture.legacy.blocks.EnumWorldBlockLayer;
import gcewing.architecture.legacy.blocks.IBlock;
import gcewing.architecture.legacy.blocks.IBlockState;

public class BaseModelRenderer implements ICustomRenderer {

    protected final IModel model;
    protected final ITexture[] textures;
    protected final Vector3 origin;

    // private static Trans3 itemTrans = Trans3.blockCenterSideTurn(0, 0);

    public BaseModelRenderer(IModel model, ITexture... textures) {
        this(model, Vector3.zero, textures);
    }

    public BaseModelRenderer(IModel model, Vector3 origin, ITexture... textures) {
        this.model = model;
        this.textures = textures;
        this.origin = origin;
    }

    public void renderBlock(IBlockAccess world, BlockPos pos, IBlockState state, IRenderTarget target,
            EnumWorldBlockLayer layer, Trans3 t) {
        IBlock block = (IBlock) state.getBlock();
        Trans3 t2 = t.t(block.localToGlobalTransformation(world, pos, state, Vector3.zero)).translate(origin);
        model.render(t2, target, textures);
    }

    public void renderItemStack(ItemStack stack, IRenderTarget target, Trans3 t) {
        model.render(t.translate(origin), target, textures);
    }

}
