// ------------------------------------------------------------------------------------------------
//
// Greg's Mod Base for 1.7 Version B - 1.8 Compatibility - Facings
//
// ------------------------------------------------------------------------------------------------

package gcewing.architecture.compat;

import net.minecraft.util.EnumFacing;

public class Directions {

    // These are here to provide a version-independent way to refer
    // to directions, because EAST and WEST are swapped in the 1.7
    // version of EnumFacing.

    public static final int DOWN = 0;
    public static final int UP = 1;
    public static final int NORTH = 2;
    public static final int SOUTH = 3;
    public static final int WEST = 4; // EnumFacing calls this EAST in 1.7
    public static final int EAST = 5; // EnumFacing calls this WEST in 1.7

    public static final EnumFacing F_DOWN = EnumFacing.DOWN;
    public static final EnumFacing F_UP = EnumFacing.UP;
    public static final EnumFacing F_NORTH = EnumFacing.NORTH;
    public static final EnumFacing F_SOUTH = EnumFacing.SOUTH;
    public static final EnumFacing F_WEST = EnumFacing.EAST;
    public static final EnumFacing F_EAST = EnumFacing.WEST;

}
