// ------------------------------------------------------------------------------------------------
//
// Greg's Mod Base for 1.7 Version B - Generic Item
//
// ------------------------------------------------------------------------------------------------

package gcewing.architecture.common.item;

import static gcewing.architecture.util.Utils.facings;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import gcewing.architecture.common.render.ModelSpec;
import gcewing.architecture.compat.BlockPos;

public class ItemArchitecture extends Item implements IHasModel {

    public String[] getTextureNames() {
        return null;
    }

    public ModelSpec getModelSpec(ItemStack stack) {
        return null;
    }

    @Override
    public boolean getHasSubtypes() {
        return false;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
            float hitX, float hitY, float hitZ) {
        return onItemUse(stack, player, world, new BlockPos(x, y, z), facings[side], hitX, hitY, hitZ);
    }

    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side,
            float hitX, float hitY, float hitZ) {
        return false;
    }

}
