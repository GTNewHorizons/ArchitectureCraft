// -----------------------------------------------------------------
//
// ArchitectureCraft - Base class for special shape renderers
//
// -----------------------------------------------------------------

package gcewing.architecture.legacy.rendering;

import static gcewing.architecture.legacy.blocks.blocks.BaseBlockUtils.getTileEntityWorld;

import net.minecraft.world.IBlockAccess;

import gcewing.architecture.ArchitectureCraft;
import gcewing.architecture.common.tile.TileShape;
import gcewing.architecture.compat.BlockPos;
import gcewing.architecture.compat.Trans3;

public abstract class RenderShape {

    protected final IBlockAccess blockWorld;
    protected final BlockPos blockPos;
    protected final TileShape te;
    protected final ITexture[] textures;
    protected final Trans3 t;
    protected final IRenderTarget target;

    public RenderShape(TileShape te, ITexture[] textures, Trans3 t, IRenderTarget target) {
        this.te = te;
        this.blockWorld = getTileEntityWorld(te);
        this.blockPos = te.getPos();
        this.textures = textures;
        this.t = t;
        this.target = target;
    }

    protected abstract void render();

    protected IModel getModel(String name) {
        return ArchitectureCraft.mod.getModel(name);
    }

}
