// ------------------------------------------------------------------------------
//
// ArchitectureCraft - Window shape kinds
//
// ------------------------------------------------------------------------------

package gcewing.architecture.shapes;

import static gcewing.architecture.blocks.BaseBlockUtils.getTileEntityWorld;
import static gcewing.architecture.shapes.ShapeKind.Window.FrameKind.Corner;
import static gcewing.architecture.shapes.ShapeKind.Window.FrameKind.None;
import static gcewing.architecture.shapes.ShapeKind.Window.FrameKind.Plain;
import static gcewing.architecture.shapes.ShapeKind.Window.Window;
import static gcewing.architecture.utils.BaseDirections.F_DOWN;
import static gcewing.architecture.utils.BaseDirections.F_EAST;
import static gcewing.architecture.utils.BaseDirections.F_SOUTH;
import static gcewing.architecture.utils.BaseDirections.F_UP;
import static gcewing.architecture.utils.BaseDirections.F_WEST;
import static gcewing.architecture.utils.BaseUtils.oppositeFacing;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;

import gcewing.architecture.compat.Trans3;
import gcewing.architecture.compat.Vector3;
import gcewing.architecture.utils.BaseUtils;

public class WindowShapeKinds {

    // ------------------------------------------------------------------------------

    public static final Window PlainWindow = new PlainWindow();

    public static class PlainWindow extends Window {

        {
            frameSides = new EnumFacing[] { F_DOWN, F_EAST, F_UP, F_WEST };
            frameAlways = new boolean[] { false, false, false, false };
            frameKinds = new FrameKind[] { Plain, Plain, None, None, Plain, Plain };
            frameOrientations = new EnumFacing[] { F_EAST, F_EAST, null, null, F_UP, F_UP };
            frameTrans = new Trans3[] { Trans3.ident, Trans3.ident.rotZ(90), Trans3.ident.rotZ(180),
                    Trans3.ident.rotZ(270), };
        }

        @Override
        public boolean orientOnPlacement(EntityPlayer player, ShapeTE te, ShapeTE nte, EnumFacing face, Vector3 hit) {
            if (nte != null && !player.isSneaking()) {
                if (nte.shape.kind instanceof PlainWindow) {
                    te.setSide(nte.side);
                    te.setTurn(nte.turn);
                    return true;
                }
                if (nte.shape.kind instanceof CornerWindow) {
                    EnumFacing nlf = nte.localFace(face);
                    FrameKind nfk = ((Window) nte.shape.kind).frameKindForLocalSide(nlf);
                    if (nfk == FrameKind.Plain) {
                        EnumFacing lf = oppositeFacing(face);
                        te.setSide(nte.side);
                        switch (nlf) {
                            case SOUTH:
                                te.setTurn(BaseUtils.turnToFace(F_WEST, lf));
                                return true;
                            case WEST:
                                te.setTurn(BaseUtils.turnToFace(F_EAST, lf));
                                return true;
                        }
                    }
                }
            }
            return super.orientOnPlacement(player, te, nte, face, hit);
        }

    }

    // ------------------------------------------------------------------------------

    public static final Window MullionWindow = new MullionWindow();

    public static class MullionWindow extends PlainWindow {

        @Override
        protected void addCentreBoxesToList(double r, double s, Trans3 t, List list) {
            t.addBox(-r, -0.5, -s, r, 0.5, s, list);
        }

        @Override
        protected void addGlassBoxesToList(double r, double s, double w, double[] e, Trans3 t, List list) {
            t.addBox(-e[3], -e[0], -w, -r, e[2], w, list);
            t.addBox(r, -e[0], -w, e[1], e[2], w, list);
        }

    }

    // ------------------------------------------------------------------------------

    public static final Window CornerWindow = new CornerWindow();

    public static class CornerWindow extends Window {

        {
            frameSides = new EnumFacing[] { F_DOWN, F_SOUTH, F_UP, F_WEST };
            frameAlways = new boolean[] { false, false, false, false };
            frameKinds = new FrameKind[] { Corner, Corner, None, Plain, Plain, None };
            frameOrientations = new EnumFacing[] { F_EAST, F_EAST, null, F_UP, F_UP, null };
            frameTrans = new Trans3[] { Trans3.ident, Trans3.ident.rotY(-90).rotZ(90), Trans3.ident.rotY(-90).rotZ(180),
                    Trans3.ident.rotZ(270), };

        }

        @Override
        protected void addCentreBoxesToList(double r, double s, Trans3 t, List list) {
            t.addBox(-r, -0.5, -r, r, 0.5, r, list);
        }

        @Override
        protected void addFrameBoxesToList(int i, double r, double s, Trans3 ts, List list) {
            if ((i & 1) == 0) {
                ts.addBox(-0.5, -0.5, -s, s, -0.5 + r, s, list);
                ts.addBox(-s, -0.5, -s, s, -0.5 + r, 0.5, list);
            } else super.addFrameBoxesToList(i, r, s, ts, list);
        }

        @Override
        protected void addGlassBoxesToList(double r, double s, double w, double[] e, Trans3 t, List list) {
            t.addBox(-e[3], -e[0], -w, -s, e[2], w, list);
            t.addBox(-w, -e[0], s, w, e[2], e[1], list);
        }

        @Override
        public boolean orientOnPlacement(EntityPlayer player, ShapeTE te, ShapeTE nte, EnumFacing face, Vector3 hit) {
            if (nte != null && !player.isSneaking()) {
                if (nte.shape.kind instanceof Window nsk) {
                    EnumFacing nlf = nte.localFace(face);
                    FrameKind nfk = nsk.frameKindForLocalSide(nlf);
                    switch (nfk) {
                        case Corner:
                            te.setSide(nte.side);
                            te.setTurn(nte.turn);
                            return true;
                        case Plain:
                            EnumFacing nfo = nte.globalFace(nsk.frameOrientationForLocalSide(nlf));
                            return orientFromAdjacentCorner(te, nfo, hit)
                                    || orientFromAdjacentCorner(te, oppositeFacing(nfo), hit);
                    }
                }
            }
            return super.orientOnPlacement(player, te, nte, face, hit);
        }

        protected boolean orientFromAdjacentCorner(ShapeTE te, EnumFacing face, Vector3 hit) {
            ShapeTE nte = ShapeTE.get(getTileEntityWorld(te), te.getPos().offset(oppositeFacing(face)));
            if (nte != null && nte.shape.kind instanceof Window nsk) {
                EnumFacing nlf = nte.localFace(face);
                FrameKind nfk = nsk.frameKindForLocalSide(nlf);
                if (nfk == FrameKind.Corner) {
                    te.setSide(nte.side);
                    te.setTurn(nte.turn);
                    return true;
                }
            }
            return false;
        }
    }

    // ------------------------------------------------------------------------------

}
