package gcewing.architecture.shapes;

import gcewing.architecture.ArchitectureCraft;
import gcewing.architecture.BaseModClient;
import gcewing.architecture.compat.Trans3;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class Cladding extends ShapeKind {

    public void renderShape(ShapeTE te, BaseModClient.ITexture[] textures, BaseModClient.IRenderTarget target, Trans3 t, boolean renderBase,
            boolean renderSecondary) {}

    public ItemStack newStack(Shape shape, Block materialBlock, int materialMeta, int stackSize) {
        return ArchitectureCraft.itemCladding.newStack(materialBlock, materialMeta, stackSize);
    }

}
