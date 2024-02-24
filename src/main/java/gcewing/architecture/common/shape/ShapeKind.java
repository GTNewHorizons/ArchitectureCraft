// ------------------------------------------------------------------------------
//
// ArchitectureCraft - Shape kinds
//
// ------------------------------------------------------------------------------

package gcewing.architecture.common.shape;

import static gcewing.architecture.legacy.blocks.BaseBlockUtils.getMetaFromBlockState;
import static gcewing.architecture.legacy.blocks.BaseBlockUtils.getTileEntityPos;
import static gcewing.architecture.legacy.blocks.BaseBlockUtils.getTileEntityWorld;
import static gcewing.architecture.legacy.utils.BaseDirections.DOWN;
import static gcewing.architecture.legacy.utils.BaseDirections.EAST;
import static gcewing.architecture.legacy.utils.BaseDirections.F_DOWN;
import static gcewing.architecture.legacy.utils.BaseDirections.F_EAST;
import static gcewing.architecture.legacy.utils.BaseDirections.F_NORTH;
import static gcewing.architecture.legacy.utils.BaseDirections.F_SOUTH;
import static gcewing.architecture.legacy.utils.BaseDirections.F_UP;
import static gcewing.architecture.legacy.utils.BaseDirections.F_WEST;
import static gcewing.architecture.legacy.utils.BaseDirections.NORTH;
import static gcewing.architecture.legacy.utils.BaseDirections.SOUTH;
import static gcewing.architecture.legacy.utils.BaseDirections.UP;
import static gcewing.architecture.legacy.utils.BaseDirections.WEST;
import static gcewing.architecture.legacy.utils.BaseUtils.oppositeFacing;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

import gcewing.architecture.ArchitectureCraft;
import gcewing.architecture.common.block.BlockArchitecture;
import gcewing.architecture.common.tile.TileArchitecture;
import gcewing.architecture.common.tile.TileShape;
import gcewing.architecture.compat.BlockPos;
import gcewing.architecture.compat.Trans3;
import gcewing.architecture.compat.Vector3;
import gcewing.architecture.legacy.blocks.IBlockState;
import gcewing.architecture.legacy.rendering.IRenderTarget;
import gcewing.architecture.legacy.rendering.ITexture;
import gcewing.architecture.legacy.utils.Profile;
import gcewing.architecture.legacy.utils.Utils;

// ------------------------------------------------------------------------------

public abstract class ShapeKind {

    public Object[] profiles; // indexed by local face

    public Object profileForLocalFace(Shape shape, EnumFacing face) {
        if (profiles != null) return profiles[face.ordinal()];
        else return null;
    }

    public double placementOffsetX() {
        return 0;
    }

    public abstract void renderShape(TileShape te, ITexture[] textures, IRenderTarget target, Trans3 t,
            boolean renderBase, boolean renderSecondary);

    public ItemStack newStack(Shape shape, IBlockState materialState, int stackSize) {
        return newStack(shape, materialState.getBlock(), getMetaFromBlockState(materialState), stackSize);
    }

    public ItemStack newStack(Shape shape, IBlockState materialState, int stackSize, boolean shaderEmissive) {
        return newStack(
                shape,
                materialState.getBlock(),
                getMetaFromBlockState(materialState),
                stackSize,
                shaderEmissive);
    }

    public ItemStack newStack(Shape shape, Block materialBlock, int materialMeta, int stackSize) {
        TileShape te = new TileShape(shape, materialBlock, materialMeta);
        int light = materialBlock.getLightValue();
        ItemStack result = TileArchitecture
                .blockStackWithTileEntity(ArchitectureCraft.content.blockShape, stackSize, light, te);
        return result;
    }

    public ItemStack newStack(Shape shape, Block materialBlock, int materialMeta, int stackSize,
            boolean shaderEmissive) {
        TileShape te = new TileShape(shape, materialBlock, materialMeta);
        int light = materialBlock.getLightValue();
        if (shaderEmissive) {
            light = 15;
            ItemStack result = TileArchitecture
                    .blockStackWithTileEntity(ArchitectureCraft.content.blockShapeSE, stackSize, light, te);
            return result;
        }
        ItemStack result = TileArchitecture
                .blockStackWithTileEntity(ArchitectureCraft.content.blockShape, stackSize, light, te);
        return result;
    }

    public boolean orientOnPlacement(EntityPlayer player, TileShape te, BlockPos npos, IBlockState nstate,
            TileEntity nte, EnumFacing otherFace, Vector3 hit) {
        if (nte instanceof TileShape) return orientOnPlacement(player, te, (TileShape) nte, otherFace, hit);
        else return orientOnPlacement(player, te, null, otherFace, hit);
    }

    public boolean orientOnPlacement(EntityPlayer player, TileShape te, TileShape nte, EnumFacing otherFace,
            Vector3 hit) {
        // boolean debug = !te.getWorld().isRemote;
        if (nte != null && !player.isSneaking()) {
            Object otherProfile = Profile.getProfileGlobal(nte.shape, nte.side, nte.turn, otherFace);
            if (otherProfile != null) {
                EnumFacing thisFace = oppositeFacing(otherFace);
                for (int i = 0; i < 4; i++) {
                    int turn = (nte.turn + i) & 3;
                    Object thisProfile = Profile.getProfileGlobal(te.shape, nte.side, turn, thisFace);
                    if (Profile.matches(thisProfile, otherProfile)) {
                        te.setSide(nte.side);
                        te.setTurn(turn);
                        te.setOffsetX(nte.getOffsetX());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean canPlaceUpsideDown() {
        return true;
    }

    public double sideZoneSize() {
        return 1 / 4d;
    }

    public boolean highlightZones() {
        return false;
    }

    public void onChiselUse(TileShape te, EntityPlayer player, EnumFacing face, Vector3 hit) {
        EnumFacing side = zoneHit(face, hit);
        if (side != null) chiselUsedOnSide(te, player, side);
        else chiselUsedOnCentre(te, player);
    }

    public void chiselUsedOnSide(TileShape te, EntityPlayer player, EnumFacing side) {
        te.toggleConnectionGlobal(side);
    }

    public void chiselUsedOnCentre(TileShape te, EntityPlayer player) {
        if (te.secondaryBlockState != null) {
            ItemStack stack = newSecondaryMaterialStack(te.secondaryBlockState);
            if (stack != null) {
                if (!Utils.playerIsInCreativeMode(player)) {
                    BlockArchitecture block = (BlockArchitecture) te.getBlockType();
                    block.spawnAsEntity(getTileEntityWorld(te), getTileEntityPos(te), stack);
                }
                te.setSecondaryMaterial(null);
            }
        }
    }

    public ItemStack newSecondaryMaterialStack(IBlockState state) {
        if (acceptsCladding()) return ArchitectureCraft.content.itemCladding.newStack(state, 1);
        else return null;
    }

    public void onHammerUse(TileShape te, EntityPlayer player, EnumFacing face, Vector3 hit) {
        if (player.isSneaking()) te.setSide((te.side + 1) % 6);
        else {
            double dx = te.getOffsetX();
            if (dx != 0) {
                dx = -dx;
                te.setOffsetX(dx);
            }
            if (dx >= 0) te.setTurn((te.turn + 1) % 4);
        }
        te.markChanged();
    }

    public EnumFacing zoneHit(EnumFacing face, Vector3 hit) {
        double r = 0.5 - sideZoneSize();
        int dir = face.ordinal();
        if (hit.x <= -r && dir != WEST) return F_WEST;
        if (hit.x >= r && dir != EAST) return F_EAST;
        if (hit.y <= -r && dir != DOWN) return F_DOWN;
        if (hit.y >= r && dir != UP) return F_UP;
        if (hit.z <= -r && dir != NORTH) return F_NORTH;
        if (hit.z >= r && dir != SOUTH) return F_SOUTH;
        return null;
    }

    public boolean acceptsCladding() {
        return false;
    }

    public boolean isValidSecondaryMaterial(IBlockState state) {
        return false;
    }

    public boolean secondaryDefaultsToBase() {
        return false;
    }

    public AxisAlignedBB getBounds(TileShape te, IBlockAccess world, BlockPos pos, IBlockState state, Entity entity,
            Trans3 t) {
        List<AxisAlignedBB> list = new ArrayList<>();
        addCollisionBoxesToList(te, world, pos, state, entity, t, list);
        return Utils.unionOfBoxes(list);
    }

    public void addCollisionBoxesToList(TileShape te, IBlockAccess world, BlockPos pos, IBlockState state,
            Entity entity, Trans3 t, List list) {
        int mask = te.shape.occlusionMask;
        int param = mask & 0xff;
        double r, h;
        switch (mask & 0xff00) {
            case 0x000: // 2x2x2 cubelet bitmap
                for (int i = 0; i < 8; i++) if ((mask & (1 << i)) != 0) {
                    Vector3 p = new Vector3(
                            (i & 1) != 0 ? 0.5 : -0.5,
                            (i & 4) != 0 ? 0.5 : -0.5,
                            (i & 2) != 0 ? 0.5 : -0.5);
                    addBox(Vector3.zero, p, t, list);
                }
                break;
            case 0x100: // Square, full size in Y
                r = param / 16.0;
                addBox(new Vector3(-r, -0.5, -r), new Vector3(r, 0.5, r), t, list);
                break;
            case 0x200: // Slab, full size in X and Y
                r = param / 32.0;
                addBox(new Vector3(-0.5, -0.5, -r), new Vector3(0.5, 0.5, r), t, list);
                break;
            case 0x300: // Slab in back corner
                r = ((param & 0xf) + 1) / 16.0; // width and length of slab
                h = ((param >> 4) + 1) / 16.0; // height of slab from bottom
                addBox(new Vector3(-0.5, -0.5, 0.5 - r), new Vector3(-0.5 + r, -0.5 + h, 0.5), t, list);
                break;
            case 0x400: // Slab at back
            case 0x500: // Slabs at back and right
                r = ((param & 0xf) + 1) / 16.0; // thickness of slab
                h = ((param >> 4) + 1) / 16.0; // height of slab from bottom
                addBox(new Vector3(-0.5, -0.5, 0.5 - r), new Vector3(0.5, -0.5 + h, 0.5), t, list);
                if ((mask & 0x100) != 0)
                    addBox(new Vector3(-0.5, -0.5, -0.5), new Vector3(-0.5 + r, -0.5 + h, 0.5), t, list);
                break;
            default: // Full cube
                addBox(new Vector3(-0.5, -0.5, -0.5), new Vector3(0.5, 0.5, 0.5), t, list);
        }
    }

    protected void addBox(Vector3 p0, Vector3 p1, Trans3 t, List list) {
        // addBox(t.p(p0), t.p(p1), list);
        t.addBox(p0, p1, list);
    }

    // ------------------------------------------------------------------------------

    public static final Roof Roof = new Roof();

    // ------------------------------------------------------------------------------

    public static Model Model(String name) {
        return new Model(name, null);
    }

    public static Model Model(String name, Object[] profiles) {
        return new Model(name, profiles);
    }

    // ------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------

    public static final Cladding Cladding = new Cladding();

    // ------------------------------------------------------------------------------

    public static Model Banister(String name) {
        return new Banister(name);
    }

    // ------------------------------------------------------------------------------

}
