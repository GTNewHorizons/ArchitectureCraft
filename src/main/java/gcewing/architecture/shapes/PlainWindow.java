package gcewing.architecture.shapes;

import gcewing.architecture.compat.Trans3;
import gcewing.architecture.compat.Vector3;
import gcewing.architecture.utils.BaseUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;

import static gcewing.architecture.shapes.FrameKind.None;
import static gcewing.architecture.shapes.FrameKind.Plain;
import static gcewing.architecture.utils.BaseDirections.F_DOWN;
import static gcewing.architecture.utils.BaseDirections.F_EAST;
import static gcewing.architecture.utils.BaseDirections.F_UP;
import static gcewing.architecture.utils.BaseDirections.F_WEST;
import static gcewing.architecture.utils.BaseUtils.oppositeFacing;

public class PlainWindow extends Window {

    {
        frameSides = new EnumFacing[] { F_DOWN, F_EAST, F_UP, F_WEST };
        frameAlways = new boolean[] { false, false, false, false };
        frameKinds = new FrameKind[] { Plain, Plain, None, None, Plain, Plain };
        frameOrientations = new EnumFacing[] { F_EAST, F_EAST, null, null, F_UP, F_UP };
        frameTrans = new Trans3[] { Trans3.ident, Trans3.ident.rotZ(90), Trans3.ident.rotZ(180), Trans3.ident.rotZ(270), };
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
