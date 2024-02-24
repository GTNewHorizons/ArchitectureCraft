// ------------------------------------------------------------------------------
//
// ArchitectureCraft - Shape profile utilities
//
// ------------------------------------------------------------------------------

package gcewing.architecture.legacy.utils;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.EnumFacing;

import gcewing.architecture.common.shape.Shape;
import gcewing.architecture.compat.Trans3;

public class Profile {

    protected static final Map opposites = new HashMap();

    public static Object getProfileGlobal(Shape shape, int side, int turn, EnumFacing globalFace) {
        EnumFacing localFace = Trans3.sideTurnRotations[side][turn].it(globalFace);
        return shape.kind.profileForLocalFace(shape, localFace);
    }

    public static boolean matches(Object profile1, Object profile2) {
        Object opposite1 = opposites.get(profile1);
        if (opposite1 != null) return opposite1 == profile2;
        else return profile1 == profile2;
    }

    public static void declareOpposite(Object profile1, Object profile2) {
        opposites.put(profile1, profile2);
        opposites.put(profile2, profile1);
    }

}
