// ------------------------------------------------------------------------------
//
// ArchitectureCraft - Hammer
//
// ------------------------------------------------------------------------------

package gcewing.architecture;

import net.minecraft.creativetab.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

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
