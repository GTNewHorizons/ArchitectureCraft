package gcewing.architecture.common.shape;

import static gcewing.architecture.compat.Directions.EAST;
import static gcewing.architecture.compat.Directions.NORTH;
import static gcewing.architecture.compat.Directions.SOUTH;
import static gcewing.architecture.compat.Directions.WEST;

import net.minecraft.util.EnumFacing;

import gcewing.architecture.client.render.ITexture;
import gcewing.architecture.client.render.RenderRoof;
import gcewing.architecture.client.render.target.IRenderTarget;
import gcewing.architecture.common.tile.TileShape;
import gcewing.architecture.compat.Trans3;

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

    final static ThreadLocal<RenderRoof> renderRoof = ThreadLocal.withInitial(RenderRoof::new);

    public void renderShape(TileShape te, ITexture[] textures, IRenderTarget target, Trans3 t, boolean renderBase,
            boolean renderSecondary) {
        final RenderRoof renderRoof = Roof.renderRoof.get();
        renderRoof.prepare(te, textures, t, target, renderBase, renderSecondary);
        renderRoof.render();
    }

    static {
        Profile.declareOpposite(RoofProfile.Left, RoofProfile.Right);
    }

    @Override
    public Object profileForLocalFace(Shape shape, EnumFacing face) {
        int dir = face.ordinal();
        switch (shape) {
            case RoofTile, RoofOverhang:
                switch (dir) {
                    case EAST:
                        return RoofProfile.Left;
                    case WEST:
                        return RoofProfile.Right;
                }
                break;
            case RoofOuterCorner, RoofOverhangOuterCorner:
                switch (dir) {
                    case SOUTH:
                        return RoofProfile.Left;
                    case WEST:
                        return RoofProfile.Right;
                }
                break;
            case RoofInnerCorner, RoofOverhangInnerCorner:
                switch (dir) {
                    case EAST:
                        return RoofProfile.Left;
                    case NORTH:
                        return RoofProfile.Right;
                }
                break;
            case RoofRidge, RoofSmartRidge, RoofOverhangRidge:
                return RoofProfile.Ridge;
            case RoofValley, RoofSmartValley, RoofOverhangValley:
                return RoofProfile.Valley;
        }
        return RoofProfile.None;
    }

}
