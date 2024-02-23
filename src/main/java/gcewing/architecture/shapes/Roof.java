package gcewing.architecture.shapes;

import static gcewing.architecture.utils.BaseDirections.EAST;
import static gcewing.architecture.utils.BaseDirections.NORTH;
import static gcewing.architecture.utils.BaseDirections.SOUTH;
import static gcewing.architecture.utils.BaseDirections.WEST;

import net.minecraft.util.EnumFacing;

import gcewing.architecture.BaseModClient;
import gcewing.architecture.compat.Trans3;
import gcewing.architecture.rendering.RenderRoof;
import gcewing.architecture.utils.Profile;

public class Roof extends ShapeKind {

    // static boolean debugPlacement = true;

    @Override
    public boolean acceptsCladding() {
        return true;
    }

    @Override
    public boolean secondaryDefaultsToBase() {
        return true;
    }

    public void renderShape(ShapeTE te, BaseModClient.ITexture[] textures, BaseModClient.IRenderTarget target, Trans3 t,
            boolean renderBase, boolean renderSecondary) {
        new RenderRoof(te, textures, t, target, renderBase, renderSecondary).render();
    }

    static {
        Profile.declareOpposite(RoofProfile.Left, RoofProfile.Right);
    }

    @Override
    public Object profileForLocalFace(Shape shape, EnumFacing face) {
        int dir = face.ordinal();
        switch (shape) {
            case RoofTile:
            case RoofOverhang:
                switch (dir) {
                    case EAST:
                        return RoofProfile.Left;
                    case WEST:
                        return RoofProfile.Right;
                }
                break;
            case RoofOuterCorner:
            case RoofOverhangOuterCorner:
                switch (dir) {
                    case SOUTH:
                        return RoofProfile.Left;
                    case WEST:
                        return RoofProfile.Right;
                }
                break;
            case RoofInnerCorner:
            case RoofOverhangInnerCorner:
                switch (dir) {
                    case EAST:
                        return RoofProfile.Left;
                    case NORTH:
                        return RoofProfile.Right;
                }
                break;
            case RoofRidge:
            case RoofSmartRidge:
            case RoofOverhangRidge:
                return RoofProfile.Ridge;
            case RoofValley:
            case RoofSmartValley:
            case RoofOverhangValley:
                return RoofProfile.Valley;
        }
        return RoofProfile.None;
    }

}
