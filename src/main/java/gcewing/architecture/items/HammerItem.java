// ------------------------------------------------------------------------------
//
// ArchitectureCraft - Hammer
//
// ------------------------------------------------------------------------------

package gcewing.architecture.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import gcewing.architecture.compat.BlockPos;
import gcewing.architecture.shapes.ShapeTE;

public class HammerItem extends BaseItem {

    public HammerItem() {
        setMaxStackSize(1);
    }

    @Override
    public CreativeTabs getCreativeTab() {
        return CreativeTabs.tabTools;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side,
            float hitX, float hitY, float hitZ) {
        ShapeTE te = ShapeTE.get(world, pos);
        if (te != null) {
            te.onHammerUse(player, side, hitX, hitY, hitZ);
            return true;
        }
        return false;
    }

}
