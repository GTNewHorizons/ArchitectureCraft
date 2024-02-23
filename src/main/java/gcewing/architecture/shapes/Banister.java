package gcewing.architecture.shapes;

import gcewing.architecture.compat.BlockPos;
import gcewing.architecture.compat.Trans3;
import gcewing.architecture.compat.Vector3;
import gcewing.architecture.interfaces.IBlockState;
import gcewing.architecture.utils.BaseUtils;
import gcewing.architecture.utils.Generic;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import static gcewing.architecture.blocks.BaseBlockUtils.getMetaFromBlockState;
import static gcewing.architecture.utils.BaseDirections.F_DOWN;
import static gcewing.architecture.utils.BaseDirections.F_EAST;
import static gcewing.architecture.utils.BaseDirections.F_NORTH;
import static gcewing.architecture.utils.BaseDirections.F_SOUTH;
import static gcewing.architecture.utils.BaseDirections.F_UP;
import static gcewing.architecture.utils.BaseDirections.F_WEST;
import static gcewing.architecture.utils.BaseUtils.oppositeFacing;

public class Banister extends Model {

    public Banister(String modelName) {
        super(modelName, Generic.tbOffset);
    }

    public boolean orientOnPlacement(EntityPlayer player, ShapeTE te, BlockPos npos, IBlockState nstate, TileEntity nte, EnumFacing otherFace, Vector3 hit) {
        // System.out.printf("Banister.orientOnPlacement: nstate = %s\n", nstate);
        if (!player.isSneaking()) {
            Block nblock = nstate.getBlock();
            boolean placedOnStair = false;
            int nside = -1; // Side that the neighbouring block is placed on
            int nturn = -1; // Turn of the neighbouring block
            if (nblock instanceof BlockStairs && (otherFace == F_UP || otherFace == F_DOWN)) {
                placedOnStair = true;
                nside = stairsSide(nstate);
                nturn = BaseUtils.turnToFace(F_SOUTH, stairsFacing(nstate));
                if (nside == 1 && (nturn & 1) == 0) nturn ^= 2;
            } else if (nblock instanceof ShapeBlock) {
                if (nte instanceof ShapeTE) {
                    placedOnStair = true;
                    nside = ((ShapeTE) nte).side;
                    nturn = ((ShapeTE) nte).turn;
                }
            }
            if (placedOnStair) {
                int side = oppositeFacing(otherFace).ordinal();
                if (side == nside) {
                    Vector3 h = Trans3.sideTurn(side, 0).ip(hit);
                    double offx = te.shape.offsetXForPlacementHit(side, nturn, hit);
                    te.setSide(side);
                    te.setTurn(nturn & 3);
                    te.setOffsetX(offx);
                    return true;
                }
            }
        }
        return super.orientOnPlacement(player, te, npos, nstate, nte, otherFace, hit);
    }

    private static final EnumFacing[] stairsFacingMap = { F_WEST, F_EAST, F_SOUTH, F_NORTH };

    private static EnumFacing stairsFacing(IBlockState state) {
        int meta = getMetaFromBlockState(state);
        return stairsFacingMap[meta & 3];
    }

    private static int stairsSide(IBlockState state) {
        int meta = getMetaFromBlockState(state);
        return (meta >> 2) & 1;
    }

    @Override
    public double placementOffsetX() {
        return 6 / 16d;
    }

}
