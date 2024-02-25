// ------------------------------------------------------
//
// ArchitectureCraft - Utilities
//
// ------------------------------------------------------

package gcewing.architecture.util;

import static java.lang.Math.PI;
import static java.lang.Math.atan2;
import static java.lang.Math.round;

import java.util.Collection;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import gcewing.architecture.compat.BlockPos;
import gcewing.architecture.compat.Vector3;

public class Utils {

    public static final EnumFacing[] facings = EnumFacing.values();
    public static final EnumFacing[] horizontalFacings = { EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.NORTH,
            EnumFacing.EAST };

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

    public static int ifloor(double x) {
        return (int) Math.floor(x);
    }

    public static int iround(double x) {
        return (int) round(x);
    }

    public static Object[] arrayOf(Collection<?> c) {
        int n = c.size();
        Object[] result = new Object[n];
        int i = 0;
        for (Object item : c) result[i++] = item;
        return result;
    }

    public static int packedColor(double red, double green, double blue) {
        return ((int) (red * 255) << 16) | ((int) (green * 255) << 8) | (int) (blue * 255);
    }

    public static int turnToFace(EnumFacing local, EnumFacing global) {
        return (turnToFaceEast(local) - turnToFaceEast(global)) & 3;
    }

    public static int turnToFaceEast(EnumFacing f) {
        return switch (f) {
            case SOUTH -> 1;
            case WEST -> 2;
            case NORTH -> 3;
            default -> 0;
        };
    }

    public static EnumFacing oppositeFacing(EnumFacing dir) {
        return facings[dir.ordinal() ^ 1];
    }

    public static boolean facingAxesEqual(EnumFacing facing1, EnumFacing facing2) {
        return (facing1.ordinal() & 6) == (facing2.ordinal() & 6);
    }

    public static int getStackMetadata(ItemStack stack) {
        return stack.getItem().getMetadata(stack.getItemDamage());
    }

    public static MovingObjectPosition newMovingObjectPosition(Vec3 hitVec, int sideHit, BlockPos pos) {
        return new MovingObjectPosition(pos.x, pos.y, pos.z, sideHit, hitVec, true);
    }

    public static AxisAlignedBB boxUnion(AxisAlignedBB box1, AxisAlignedBB box2) {
        return box1.func_111270_a(box2);
    }
}
