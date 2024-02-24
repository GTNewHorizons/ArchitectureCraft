// ------------------------------------------------------------------------------------------------
//
// Greg's Mod Base for 1.7 Version B - Utilities
//
// ------------------------------------------------------------------------------------------------

package gcewing.architecture.legacy.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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

    public static Class<?> classForName(String name) {
        try {
            return Class.forName(name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Field getFieldDef(Class<?> cls, String unobfName, String obfName) {
        try {
            Field field;
            try {
                field = cls.getDeclaredField(unobfName);
            } catch (NoSuchFieldException e) {
                field = cls.getDeclaredField(obfName);
            }
            field.setAccessible(true);
            return field;
        } catch (Exception e) {
            throw new RuntimeException(
                    String.format("Cannot find field %s or %s of %s", unobfName, obfName, cls.getName()),
                    e);
        }
    }

    public static int getIntField(Object obj, Field field) {
        try {
            return field.getInt(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void setIntField(Object obj, Field field, int value) {
        try {
            field.setInt(obj, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Method getMethodDef(Class<?> cls, String unobfName, String obfName, Class<?>... params) {
        try {
            Method meth;
            try {
                meth = cls.getDeclaredMethod(unobfName, params);
            } catch (NoSuchMethodException e) {
                meth = cls.getDeclaredMethod(obfName, params);
            }
            meth.setAccessible(true);
            return meth;
        } catch (Exception e) {
            throw new RuntimeException(
                    String.format("Cannot find method %s or %s of %s", unobfName, obfName, cls.getName()),
                    e);
        }
    }

    public static Object invokeMethod(Object target, Method meth, Object... args) {
        try {
            return meth.invoke(target, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    // public static int getMetaFromState(IBlockState state) {
    // return ((BaseBlock)state.getBlock()).getMetaFromState(state);
    // }

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
