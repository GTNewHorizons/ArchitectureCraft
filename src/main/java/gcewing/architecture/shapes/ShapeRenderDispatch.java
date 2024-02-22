// ------------------------------------------------------
//
// ArchitectureCraft - Shape rendering dispatcher
//
// ------------------------------------------------------

package gcewing.architecture.shapes;

import static gcewing.architecture.blocks.BaseBlockUtils.blockCanRenderInLayer;
import static gcewing.architecture.blocks.BaseBlockUtils.getSpriteForBlockState;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import gcewing.architecture.BaseModClient.ICustomRenderer;
import gcewing.architecture.BaseModClient.IRenderTarget;
import gcewing.architecture.BaseModClient.ITexture;
import gcewing.architecture.blocks.EnumWorldBlockLayer;
import gcewing.architecture.compat.BlockPos;
import gcewing.architecture.compat.Trans3;
import gcewing.architecture.interfaces.IBlockState;
import gcewing.architecture.rendering.BaseTexture;

public class ShapeRenderDispatch implements ICustomRenderer {

    // Cannot have any per-render state, because it may be
    // called from more than one thread.

    @Override
    public void renderBlock(IBlockAccess world, BlockPos pos, IBlockState state, IRenderTarget target,
            EnumWorldBlockLayer layer, Trans3 t) {
        ShapeTE te = ShapeTE.get(world, pos);
        if (te != null) {
            Trans3 t2 = t.t(te.localToGlobalRotation());
            boolean renderBase = te.baseBlockState != null
                    && blockCanRenderInLayer(te.baseBlockState.getBlock(), layer);
            boolean renderSecondary = te.secondaryBlockState != null
                    && blockCanRenderInLayer(te.secondaryBlockState.getBlock(), layer);
            renderShapeTE(te, target, t2, renderBase, renderSecondary);
        }
    }

    @Override
    public void renderItemStack(ItemStack stack, IRenderTarget target, Trans3 t) {
        ShapeTE te = new ShapeTE();
        te.readFromItemStack(stack);
        renderShapeTE(
                te,
                target,
                t.t(Trans3.sideTurn(0, 3)),
                te.baseBlockState != null,
                te.secondaryBlockState != null);
    }

    protected void renderShapeTE(ShapeTE te, IRenderTarget target, Trans3 t, boolean renderBase,
            boolean renderSecondary) {
        if (te.shape != null && (renderBase || renderSecondary)) {
            IBlockState base = te.baseBlockState;
            if (base != null) {
                // System.out.printf("ShapeRenderDispatch.renderShapeTE: in pass %s renderBase = %s renderSecondary =
                // %s\n",
                // MinecraftForgeClient.getRenderPass(), renderBase, renderSecondary);
                IIcon icon = getSpriteForBlockState(base);
                IIcon icon2 = getSpriteForBlockState(te.secondaryBlockState);
                if (icon != null) {
                    ITexture[] textures = new ITexture[4];
                    if (renderBase) {
                        textures[0] = BaseTexture.fromSprite(icon);
                        textures[1] = textures[0].projected();
                    }
                    if (renderSecondary) {
                        if (icon2 != null) {
                            textures[2] = BaseTexture.fromSprite(icon2);
                            textures[3] = textures[2].projected();
                        } else renderSecondary = false;
                    }
                    if (renderBase && te.shape.kind.secondaryDefaultsToBase()) {
                        if (icon2 == null || (te.secondaryBlockState != null
                                && te.secondaryBlockState.getBlock().getRenderBlockPass() != 0)) {
                            textures[2] = textures[0];
                            textures[3] = textures[1];
                            renderSecondary = renderBase;
                        }
                    }
                    te.shape.kind.renderShape(te, textures, target, t, renderBase, renderSecondary);
                }
            }
        }
    }

}
