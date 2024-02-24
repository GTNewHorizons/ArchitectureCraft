package gcewing.architecture.common.shape;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import gcewing.architecture.ArchitectureCraft;
import gcewing.architecture.client.render.ITexture;
import gcewing.architecture.client.render.target.IRenderTarget;
import gcewing.architecture.common.tile.TileShape;
import gcewing.architecture.compat.Trans3;

public class Cladding extends ShapeKind {

    public void renderShape(TileShape te, ITexture[] textures, IRenderTarget target, Trans3 t, boolean renderBase,
            boolean renderSecondary) {}

    public ItemStack newStack(Shape shape, Block materialBlock, int materialMeta, int stackSize) {
        return ArchitectureCraft.content.itemCladding.newStack(materialBlock, materialMeta, stackSize);
    }

}
