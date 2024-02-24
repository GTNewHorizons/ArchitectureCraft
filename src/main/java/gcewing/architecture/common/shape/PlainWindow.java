package gcewing.architecture.common.shape;

import static gcewing.architecture.compat.Directions.F_DOWN;
import static gcewing.architecture.compat.Directions.F_EAST;
import static gcewing.architecture.compat.Directions.F_UP;
import static gcewing.architecture.compat.Directions.F_WEST;
import static gcewing.architecture.legacy.utils.BaseUtils.oppositeFacing;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;

import gcewing.architecture.common.tile.TileShape;
import gcewing.architecture.compat.Trans3;
import gcewing.architecture.compat.Vector3;
import gcewing.architecture.legacy.utils.BaseUtils;

public class PlainWindow extends Window {

    {
        frameSides = new EnumFacing[] { F_DOWN, F_EAST, F_UP, F_WEST };
        frameAlways = new boolean[] { false, false, false, false };
        frameKinds = new FrameKind[] { FrameKind.Plain, FrameKind.Plain, FrameKind.None, FrameKind.None,
                FrameKind.Plain, FrameKind.Plain };
        frameOrientations = new EnumFacing[] { F_EAST, F_EAST, null, null, F_UP, F_UP };
        frameTrans = new Trans3[] { Trans3.ident, Trans3.ident.rotZ(90), Trans3.ident.rotZ(180),
                Trans3.ident.rotZ(270), };
    }

    @Override
    public boolean orientOnPlacement(EntityPlayer player, TileShape te, TileShape nte, EnumFacing face, Vector3 hit) {
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
