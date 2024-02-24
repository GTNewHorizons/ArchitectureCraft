// ------------------------------------------------------------------------------
//
// ArchitectureCraft - Hammer
//
// ------------------------------------------------------------------------------

package gcewing.architecture.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import gcewing.architecture.common.tile.TileShape;
import gcewing.architecture.compat.BlockPos;

public class ItemHammer extends ItemArchitecture {

    public ItemHammer() {
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
            te.onHammerUse(player, side, hitX, hitY, hitZ);
            return true;
        }
        return false;
    }

}
