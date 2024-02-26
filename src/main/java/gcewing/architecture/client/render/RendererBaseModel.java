// ------------------------------------------------------------------------------------------------
//
// Greg's Mod Base for 1.7 Version B - Render block using model + textures
//
// ------------------------------------------------------------------------------------------------

package gcewing.architecture.client.render;

// import net.minecraft.block.state.IBlockState;

import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;

import gcewing.architecture.client.render.model.IArchitectureModel;
import gcewing.architecture.client.render.target.IRenderTarget;
import gcewing.architecture.common.block.IBlockArchitecture;
import gcewing.architecture.compat.BlockPos;
import gcewing.architecture.compat.EnumWorldBlockLayer;
import gcewing.architecture.compat.IBlockState;
import gcewing.architecture.compat.Trans3;
import gcewing.architecture.compat.Vector3;

public class RendererBaseModel implements ICustomRenderer {

    protected final IArchitectureModel model;
    protected final ITexture[] textures;
    protected final Vector3 origin;

    // private static Trans3 itemTrans = Trans3.blockCenterSideTurn(0, 0);

    public RendererBaseModel(IArchitectureModel model, ITexture... textures) {
        this(model, Vector3.zero, textures);
    }

    public RendererBaseModel(IArchitectureModel model, Vector3 origin, ITexture... textures) {
        this.model = model;
        this.textures = textures;
        this.origin = origin;
    }

    public void renderBlock(IBlockAccess world, BlockPos pos, IBlockState state, IRenderTarget target,
            EnumWorldBlockLayer layer, Trans3 t) {
        IBlockArchitecture block = (IBlockArchitecture) state.getBlock();
        Trans3 t2 = t.t(block.localToGlobalTransformation(world, pos, state, null, Vector3.zero)).translate(origin);
        model.render(t2, target, textures);
    }

    public void renderItemStack(ItemStack stack, IRenderTarget target, Trans3 t) {
        model.render(t.translate(origin), target, textures);
    }

}
