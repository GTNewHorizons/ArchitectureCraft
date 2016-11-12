//------------------------------------------------------------------------------------------------
//
//   Greg's Mod Base for 1.10 - Utilities
//
//------------------------------------------------------------------------------------------------

package gcewing.architecture;

import java.util.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.*;
import net.minecraft.util.math.*;

import net.minecraftforge.common.DimensionManager;

public class BaseUtils {

    public static EnumFacing[] facings = EnumFacing.values();

    public static int min(int x, int y) {
        return x < y ? x : y;
    }
    
    public static int max(int x, int y) {
        return x > y ? x : y;
    }

    public static double min(double x, double y) {
        return x < y ? x : y;
    }
    
    public static double max(double x, double y) {
        return x > y ? x : y;
    }
    
    public static int ifloor(double x) {
        return (int)Math.floor(x);
    }
    
    public static int iround(double x) {
        return (int)Math.round(x);
    }
    
    public static int iceil(double x) {
        return (int)Math.ceil(x);
    }
    
    public static Object[] arrayOf(Collection c) {
        int n = c.size();
        Object[] result = new Object[n];
        int i = 0;
        for (Object item : c)
            result[i++] = item;
        return result;
    }
    
    public static int packedColor(double red, double green, double blue) {
        return ((int)(red * 255) << 16) | ((int)(green * 255) << 8) | (int)(blue * 255);
    }
    
    public static int turnToFace(EnumFacing local, EnumFacing global) {
        return (turnToFaceEast(local) - turnToFaceEast(global)) & 3;
    }
    
    public static int turnToFaceEast(EnumFacing f) {
        switch (f) {
            case SOUTH: return 1;
            case WEST: return 2;
            case NORTH: return 3;
            default: return 0;
        }
    }
    
    public static MinecraftServer getMinecraftServer() {
        return DimensionManager.getWorld(0).getMinecraftServer();
    }
    
}
