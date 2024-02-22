// ------------------------------------------------------------------------------
//
// ArchitectureCraft - Cladding Item
//
// ------------------------------------------------------------------------------

package gcewing.architecture.items;

import static gcewing.architecture.blocks.BaseBlockUtils.getBlockStateFromMeta;
import static gcewing.architecture.blocks.BaseBlockUtils.getMetaFromBlockState;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import gcewing.architecture.blocks.BaseBlockUtils;
import gcewing.architecture.interfaces.IBlockState;
import gcewing.architecture.utils.Utils;

public class CladdingItem extends Item {

    public ItemStack newStack(IBlockState state, int stackSize) {
        Block block = state.getBlock();
        int meta = getMetaFromBlockState(state);
        return newStack(block, meta, stackSize);
    }

    public ItemStack newStack(Block block, int meta, int stackSize) {
        ItemStack result = new ItemStack(this, stackSize, meta);
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("block", BaseBlockUtils.getNameForBlock(block));
        result.setTagCompound(nbt);
        return result;
    }

    public IBlockState blockStateFromStack(ItemStack stack) {
        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt != null) {
            Block block = Block.getBlockFromName(nbt.getString("block"));
            if (block != null) return getBlockStateFromMeta(block, stack.getItemDamage());
        }
        return null;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List lines, boolean par4) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null) {
            Block block = Block.getBlockFromName(tag.getString("block"));
            int meta = stack.getItemDamage();
            if (block != null) lines.add(Utils.displayNameOfBlock(block, meta));
        }
    }

    // 1.7 only
    @Override
    public int getSpriteNumber() {
        return 0;
    }
}
