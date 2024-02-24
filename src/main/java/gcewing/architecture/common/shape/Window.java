package gcewing.architecture.common.shape;

import static gcewing.architecture.compat.Directions.EAST;
import static gcewing.architecture.compat.Directions.NORTH;
import static gcewing.architecture.compat.Directions.SOUTH;
import static gcewing.architecture.compat.Directions.WEST;
import static gcewing.architecture.legacy.utils.BaseUtils.facingAxesEqual;
import static gcewing.architecture.legacy.utils.BaseUtils.oppositeFacing;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

import gcewing.architecture.client.render.ITexture;
import gcewing.architecture.client.render.RenderWindow;
import gcewing.architecture.client.render.target.IRenderTarget;
import gcewing.architecture.common.tile.TileShape;
import gcewing.architecture.compat.BlockCompatUtils;
import gcewing.architecture.compat.BlockPos;
import gcewing.architecture.compat.IBlockState;
import gcewing.architecture.compat.Trans3;
import gcewing.architecture.compat.Vector3;

public abstract class Window extends ShapeKind {

    public EnumFacing[] frameSides;
    public boolean[] frameAlways;
    public FrameKind[] frameKinds;
    public EnumFacing[] frameOrientations;
    public Trans3[] frameTrans;

    @Override
    public boolean orientOnPlacement(EntityPlayer player, TileShape te, TileShape nte, EnumFacing otherFace,
            Vector3 hit) {
        int turn = -1;
        // If click is on side of a non-window block, orient perpendicular to it
        if (!player.isSneaking() && (nte == null || !(nte.shape.kind instanceof Window))) {
            turn = switch (otherFace.ordinal()) {
                case EAST, WEST -> 0;
                case NORTH, SOUTH -> 1;
                default -> turn;
            };
        }
        if (turn >= 0) {
            te.setSide(0);
            te.setTurn(turn);
            return true;
        } else return false;
    }

    public FrameKind frameKindForLocalSide(EnumFacing side) {
        return frameKinds[side.ordinal()];
    }

    public EnumFacing frameOrientationForLocalSide(EnumFacing side) {
        return frameOrientations[side.ordinal()];
    }

    @Override
    public boolean canPlaceUpsideDown() {
        return false;
    }

    @Override
    public double sideZoneSize() {
        return 1 / 8d; // 3/32d;
    }

    @Override
    public boolean highlightZones() {
        return true;
    }

    public void renderShape(TileShape te, ITexture[] textures, IRenderTarget target, Trans3 t, boolean renderBase,
            boolean renderSecondary) {
        new RenderWindow(te, textures, t, target, renderBase, renderSecondary).render();
    }

    // @Override
    // public void chiselUsedOnCentre(ShapeTE te, EntityPlayer player) {
    // if (te.secondaryBlockState != null) {
    // ItemStack stack = BaseUtils.blockStackWithState(te.secondaryBlockState, 1);
    // dropSecondaryMaterial(te, player, stack);
    // }
    // }

    @Override
    public ItemStack newSecondaryMaterialStack(IBlockState state) {
        return BlockCompatUtils.blockStackWithState(state, 1);
    }

    @Override
    public boolean isValidSecondaryMaterial(IBlockState state) {
        Block block = state.getBlock();
        return block == Blocks.glass_pane || block == Blocks.stained_glass_pane;
    }

    @Override
    public void addCollisionBoxesToList(TileShape te, IBlockAccess world, BlockPos pos, IBlockState state,
            Entity entity, Trans3 t, List list) {
        final double r = 1 / 8d, s = 3 / 32d;
        double[] e = new double[4];
        addCentreBoxesToList(r, s, t, list);
        for (int i = 0; i <= 3; i++) {
            boolean frame = frameAlways[i] || !isConnectedGlobal(te, t.t(frameSides[i]));
            if (entity == null || frame) {
                Trans3 ts = t.t(frameTrans[i]);
                addFrameBoxesToList(i, r, s, ts, list);
            }
            e[i] = frame ? 0.5 - r : 0.5;
        }
        if (te.secondaryBlockState != null) addGlassBoxesToList(r, s, 1 / 32d, e, t, list);
    }

    protected void addCentreBoxesToList(double r, double s, Trans3 t, List list) {}

    protected void addFrameBoxesToList(int i, double r, double s, Trans3 ts, List list) {
        ts.addBox(-0.5, -0.5, -s, 0.5, -0.5 + r, s, list);
    }

    protected void addGlassBoxesToList(double r, double s, double w, double[] e, Trans3 t, List list) {
        t.addBox(-e[3], -e[0], -w, e[1], e[2], w, list);
    }

    protected boolean isConnectedGlobal(TileShape te, EnumFacing globalDir) {
        return getConnectedWindowGlobal(te, globalDir) != null;
    }

    public TileShape getConnectedWindowGlobal(TileShape te, EnumFacing globalDir) {
        EnumFacing thisLocalDir = te.localFace(globalDir);
        FrameKind thisFrameKind = frameKindForLocalSide(thisLocalDir);
        if (thisFrameKind != FrameKind.None) {
            EnumFacing thisOrient = frameOrientationForLocalSide(thisLocalDir);
            TileShape nte = te.getConnectedNeighbourGlobal(globalDir);
            if (nte != null && nte.shape.kind instanceof Window otherKind) {
                EnumFacing otherLocalDir = nte.localFace(oppositeFacing(globalDir));
                FrameKind otherFrameKind = otherKind.frameKindForLocalSide(otherLocalDir);
                if (otherFrameKind != FrameKind.None) {
                    EnumFacing otherOrient = otherKind.frameOrientationForLocalSide(otherLocalDir);
                    if (framesMatch(
                            thisFrameKind,
                            otherFrameKind,
                            te.globalFace(thisOrient),
                            nte.globalFace(otherOrient)))
                        return nte;
                }
            }
        }
        return null;
    }

    protected boolean framesMatch(FrameKind kind1, FrameKind kind2, EnumFacing orient1, EnumFacing orient2) {
        if (kind1 == kind2) {
            return switch (kind1) {
                case Plain -> facingAxesEqual(orient1, orient2);
                default -> orient1 == orient2;
            };
        }
        return false;
    }

    // protected EnumFacing getFrameOrientationGlobal(ShapeTE te, EnumFacing globalDir) {
    // Trans3 t = te.localToGlobalRotation();
    // EnumFacing localDir = t.it(globalDir);
    // return frameOrientations[localDir.ordinal()];
    // }

}
