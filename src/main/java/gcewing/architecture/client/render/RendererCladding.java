// ------------------------------------------------------------------------------
//
// ArchitectureCraft - Cladding Item Renderer
//
// ------------------------------------------------------------------------------

package gcewing.architecture.client.render;

import static gcewing.architecture.compat.BlockCompatUtils.getBlockStateFromMeta;
import static gcewing.architecture.compat.BlockCompatUtils.getSpriteForBlockState;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import gcewing.architecture.ArchitectureCraft;
import gcewing.architecture.client.render.model.IArchitectureModel;
import gcewing.architecture.client.render.target.IRenderTarget;
import gcewing.architecture.client.texture.ArchitectureTexture;
import gcewing.architecture.compat.BlockPos;
import gcewing.architecture.compat.EnumWorldBlockLayer;
import gcewing.architecture.compat.IBlockState;
import gcewing.architecture.compat.Trans3;

public class RendererCladding implements ICustomRenderer {

    public void renderBlock(IBlockAccess world, BlockPos pos, IBlockState state, IRenderTarget target,
            EnumWorldBlockLayer layer, Trans3 t) {}

    public void renderItemStack(ItemStack stack, IRenderTarget target, Trans3 t) {
        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt != null) {
            String blockName = nbt.getString("block");
            int meta = stack.getItemDamage();
            Block block = Block.getBlockFromName(blockName);
            if (block != null) {
                IBlockState state = getBlockStateFromMeta(block, meta);
                if (state != null) {
                    IIcon sprite = getSpriteForBlockState(state);
                    if (sprite != null) {
                        ITexture texture = ArchitectureTexture.fromSprite(sprite);
                        IArchitectureModel model = ArchitectureCraft.mod.getModel("shape/cladding.smeg");
                        model.render(t, target, texture);
                    }
                }
            }
        }
    }

}
