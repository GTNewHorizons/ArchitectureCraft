// ------------------------------------------------------------------------------
//
// ArchitectureCraft - Hammer
//
// ------------------------------------------------------------------------------

package gcewing.architecture.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import gcewing.architecture.ArchitectureCraft;
import gcewing.architecture.common.tile.TileShape;
import gcewing.architecture.compat.BlockPos;

public class ItemGlowBrush extends ItemArchitecture {

    public ItemGlowBrush() {
        setMaxStackSize(1);
    }

    @Override
    public CreativeTabs getCreativeTab() {
        return CreativeTabs.tabTools;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side,
            float hitX, float hitY, float hitZ) {
        TileShape te = TileShape.get(world, pos);
        if (te != null) {
            NBTTagCompound shape = new NBTTagCompound();
            te.writeToNBT(shape);
            if (player.isSneaking()) {
                // set the block to the non-glowing variant, and set it's meta to 0 for it to give no light
                world.setBlock(pos.x, pos.y, pos.z, ArchitectureCraft.content.blockShape, 0, 3);
            } else {
                // set the block to the glow variant, and set it's meta to 15 for it to actual give off light
                world.setBlock(pos.x, pos.y, pos.z, ArchitectureCraft.content.blockShapeSE, 15, 3);
            }
            TileShape newTile = TileShape.get(world, pos);
            if (newTile != null) {
                newTile.readFromNBT(shape);
            }
            return true;
        }
        return false;
    }
}
