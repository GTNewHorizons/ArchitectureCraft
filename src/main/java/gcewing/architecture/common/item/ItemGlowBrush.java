// ------------------------------------------------------------------------------
//
// ArchitectureCraft - Hammer
//
// ------------------------------------------------------------------------------

package gcewing.architecture.common.item;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.EnumSkyBlock;
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
        if (world.isRemote) return false;

        TileShape te = TileShape.get(world, pos);
        if (te == null) return false;

        Block currentBlock = world.getBlock(pos.x, pos.y, pos.z);
        boolean makingGlow = !player.isSneaking();
        boolean alreadyGlow = currentBlock == ArchitectureCraft.content.blockShapeSE;
        if (makingGlow == alreadyGlow) return true;

        // Save all shape state
        NBTTagCompound savedNBT = new NBTTagCompound();
        te.writeToNBT(savedNBT);

        Block newBlock = makingGlow ? ArchitectureCraft.content.blockShapeSE : ArchitectureCraft.content.blockShape;
        world.setBlock(pos.x, pos.y, pos.z, newBlock, 0, 3);

        TileShape newTE = TileShape.get(world, pos);
        if (newTE != null) {
            newTE.readFromNBT(savedNBT);
            // Restore correct coordinates overwritten by readFromNBT
            newTE.xCoord = pos.x;
            newTE.yCoord = pos.y;
            newTE.zCoord = pos.z;
            newTE.markChanged();
        }

        world.updateLightByType(EnumSkyBlock.Block, pos.x, pos.y, pos.z);
        return true;
    }
}
