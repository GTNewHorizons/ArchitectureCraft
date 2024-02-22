// ------------------------------------------------------
//
// ArchitectureCraft - Utilities
//
// ------------------------------------------------------

package gcewing.architecture.utils;

import static gcewing.architecture.utils.BaseUtils.boxUnion;
import static java.lang.Math.PI;
import static java.lang.Math.atan2;
import static java.lang.Math.round;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;

import gcewing.architecture.compat.Vector3;

public class Utils {

    public static Random random = new Random();

    public static int playerTurn(EntityLivingBase player) {
        return MathHelper.floor_double((player.rotationYaw * 4.0 / 360.0) + 0.5) & 3;
    }

    public static int lookTurn(Vector3 look) {
        double a = atan2(look.x, look.z);
        return (int) round(a * 2 / PI) & 3;
    }

    public static boolean playerIsInCreativeMode(EntityPlayer player) {
        return (player instanceof EntityPlayerMP) && ((EntityPlayerMP) player).theItemInWorldManager.isCreative();
    }

    public static String displayNameOfBlock(Block block, int meta) {
        String name = null;
        Item item = Item.getItemFromBlock(block);
        if (item != null) {
            ItemStack stack = new ItemStack(item, 1, meta);
            name = stack.getDisplayName();
        }
        if (name == null) name = block.getLocalizedName();
        return "Cut from " + name;
    }

    public static AxisAlignedBB unionOfBoxes(List<AxisAlignedBB> list) {
        AxisAlignedBB box = list.get(0);
        int n = list.size();
        for (int i = 1; i < n; i++) box = boxUnion(box, list.get(i));
        return box;
    }
}
