// ------------------------------------------------------------------------------------------------
//
// Greg's Mod Base for 1.7 Version B - Utilities
//
// ------------------------------------------------------------------------------------------------

package gcewing.architecture.legacy.utils;

import java.util.Collection;

import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import gcewing.architecture.compat.BlockPos;

public class BaseUtils {

    public static final EnumFacing[] facings = EnumFacing.values();
    public static final EnumFacing[] horizontalFacings = { EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.NORTH,
            EnumFacing.EAST };

    public static int ifloor(double x) {
        return (int) Math.floor(x);
    }

    public static int iround(double x) {
        return (int) Math.round(x);
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
