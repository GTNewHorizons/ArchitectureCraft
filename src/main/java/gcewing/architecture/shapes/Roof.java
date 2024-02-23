package gcewing.architecture.shapes;

import gcewing.architecture.BaseModClient;
import gcewing.architecture.compat.Trans3;
import gcewing.architecture.rendering.RenderRoof;
import gcewing.architecture.utils.Profile;
import net.minecraft.util.EnumFacing;

import static gcewing.architecture.utils.BaseDirections.EAST;
import static gcewing.architecture.utils.BaseDirections.NORTH;
import static gcewing.architecture.utils.BaseDirections.SOUTH;
import static gcewing.architecture.utils.BaseDirections.WEST;

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

    public void renderShape(ShapeTE te, BaseModClient.ITexture[] textures, BaseModClient.IRenderTarget target, Trans3 t, boolean renderBase,
            boolean renderSecondary) {
        new RenderRoof(te, textures, t, target, renderBase, renderSecondary).render();
    }

    // @Override
    // public boolean orientOnPlacement(EntityPlayer player, ShapeTE te, ShapeTE nte, EnumFacing face,
    // Vector3 hit)
    // {
    //// if (!te.getWorld().isRemote)
    //// System.out.printf("Roof.orientOnPlacement\n");
    // if (!player.isSneaking() && nte != null && nte.shape.kind instanceof Roof) {
    // EnumFacing nlf = nte.localFace(face);
    // Profile np = profileForLocalFace(nte.shape, nlf);
    // Profile p = opposite(np);
    // EnumFacing lf = localFaceForProfile(te.shape, p);
    // if (lf != null) {
    // int turn = BaseUtils.turnToFace(lf, face.getOpposite());
    // if (debugPlacement && !te.getWorld().isRemote) {
    // System.out.printf(
    // "Roof.orientOnPlacement: Aligning profile %s on local side %s of neighbour " +
    // "with profile %s on local side %s\n", np, nlf, p, lf);
    // System.out.printf("Roof.orientOnPlacement: Turning local side %s to face global direction %s\n",
    // lf, face.getOpposite());
    // System.out.printf("Roof.orientOnPlacement: side %s turn %s\n", nte.side, turn);
    // }
    // te.setSide(nte.side);
    // te.setTurn(turn);
    // return true;
    // }
    // }
    // return false;
    // }

    protected enum RoofProfile {
        None,
        Left,
        Right,
        Ridge,
        Valley
    }

    static {
        Profile.declareOpposite(ShapeKind.Roof.RoofProfile.Left, ShapeKind.Roof.RoofProfile.Right);
    }

    // protected RoofProfile opposite(RoofProfile p) {
    // switch (p) {
    // case Left: return RoofProfile.Right;
    // case Right: return RoofProfile.Left;
    // }
    // return p;
    // }

    @Override
    public Object profileForLocalFace(Shape shape, EnumFacing face) {
        int dir = face.ordinal();
        switch (shape) {
            case RoofTile:
            case RoofOverhang:
                switch (dir) {
                    case EAST:
                        return ShapeKind.Roof.RoofProfile.Left;
                    case WEST:
                        return ShapeKind.Roof.RoofProfile.Right;
                }
                break;
            case RoofOuterCorner:
            case RoofOverhangOuterCorner:
                switch (dir) {
                    case SOUTH:
                        return ShapeKind.Roof.RoofProfile.Left;
                    case WEST:
                        return ShapeKind.Roof.RoofProfile.Right;
                }
                break;
            case RoofInnerCorner:
            case RoofOverhangInnerCorner:
                switch (dir) {
                    case EAST:
                        return ShapeKind.Roof.RoofProfile.Left;
                    case NORTH:
                        return ShapeKind.Roof.RoofProfile.Right;
                }
                break;
            case RoofRidge:
            case RoofSmartRidge:
            case RoofOverhangRidge:
                return ShapeKind.Roof.RoofProfile.Ridge;
            case RoofValley:
            case RoofSmartValley:
            case RoofOverhangValley:
                return ShapeKind.Roof.RoofProfile.Valley;
        }
        return ShapeKind.Roof.RoofProfile.None;
    }

}
