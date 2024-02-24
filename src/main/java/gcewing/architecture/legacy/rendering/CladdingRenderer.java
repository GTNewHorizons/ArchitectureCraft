// ------------------------------------------------------------------------------
//
// ArchitectureCraft - Cladding Item Renderer
//
// ------------------------------------------------------------------------------

package gcewing.architecture.legacy.rendering;

import static gcewing.architecture.legacy.blocks.blocks.BaseBlockUtils.getBlockStateFromMeta;
import static gcewing.architecture.legacy.blocks.blocks.BaseBlockUtils.getSpriteForBlockState;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import gcewing.architecture.ArchitectureCraft;
import gcewing.architecture.compat.BlockPos;
import gcewing.architecture.compat.Trans3;
import gcewing.architecture.legacy.blocks.blocks.EnumWorldBlockLayer;
import gcewing.architecture.legacy.blocks.blocks.IBlockState;

public class CladdingRenderer implements ICustomRenderer {

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
                        ITexture texture = BaseTexture.fromSprite(sprite);
                        IModel model = ArchitectureCraft.mod.client.getModel("shape/cladding.smeg");
                        model.render(t, target, texture);
                    }
                }
            }
        }
    }

}
