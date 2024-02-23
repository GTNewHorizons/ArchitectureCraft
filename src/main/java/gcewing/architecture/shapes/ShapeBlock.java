// ------------------------------------------------------------------------------
//
// ArchitectureCraft - ShapeBlock
//
// ------------------------------------------------------------------------------

package gcewing.architecture.shapes;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gcewing.architecture.blocks.BaseBlock;
import gcewing.architecture.blocks.BaseBlockUtils;
import gcewing.architecture.blocks.EnumWorldBlockLayer;
import gcewing.architecture.blocks.IOrientationHandler;
import gcewing.architecture.compat.BlockPos;
import gcewing.architecture.compat.Trans3;
import gcewing.architecture.compat.Vector3;
import gcewing.architecture.interfaces.IBlockState;
import gcewing.architecture.interfaces.IProperty;
import gcewing.architecture.properties.PropertyInteger;
import gcewing.architecture.utils.BaseOrientation;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

import static gcewing.architecture.blocks.BaseBlockUtils.getBlockHarvestLevel;
import static gcewing.architecture.blocks.BaseBlockUtils.getBlockHarvestTool;
import static gcewing.architecture.blocks.BaseBlockUtils.getPlayerBreakSpeed;
import static gcewing.architecture.blocks.BaseBlockUtils.getWorldBlockState;
import static gcewing.architecture.blocks.BaseBlockUtils.getWorldTileEntity;
import static gcewing.architecture.utils.BaseUtils.newMovingObjectPosition;

public class ShapeBlock extends BaseBlock<ShapeTE> {

    protected AxisAlignedBB boxHit;

    public static final IProperty<Integer> LIGHT = PropertyInteger.create("light", 0, 15);

    @Override
    protected void defineProperties() {
        super.defineProperties();
        addProperty(LIGHT);
    }

    @Override
    public int getNumSubtypes() {
        return 16;
    }

    public ShapeBlock() {
        super(Material.rock, ShapeTE.class);
        // renderID = -1;
    }

    @Override
    public IOrientationHandler getOrientationHandler() {
        return BaseOrientation.orient24WaysByTE;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World world, BlockPos pos, Vec3 start, Vec3 end) {
        MovingObjectPosition result = null;
        double nearestDistance = 0;
        IBlockState state = getWorldBlockState(world, pos);
        List<AxisAlignedBB> list = getGlobalCollisionBoxes(world, pos, state, null);
        if (list != null) {
            int n = list.size();
            for (int i = 0; i < n; i++) {
                AxisAlignedBB box = list.get(i);
                MovingObjectPosition mp = box.calculateIntercept(start, end);
                if (mp != null) {
                    mp.subHit = i;
                    double d = start.squareDistanceTo(mp.hitVec);
                    if (result == null || d < nearestDistance) {
                        result = mp;
                        nearestDistance = d;
                    }
                }
            }
        }
        if (result != null) {
            // setBlockBounds(list.get(result.subHit));
            int i = result.subHit;
            boxHit = list.get(i).offset(-pos.getX(), -pos.getY(), -pos.getZ());
            result = newMovingObjectPosition(result.hitVec, result.sideHit, pos);
            result.subHit = i;
        }
        return result;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos) {
        if (boxHit != null) {
            ShapeTE te = ShapeTE.get(world, pos);
            if (te != null && te.shape.kind.highlightZones()) {
                // System.out.printf("ShapeBlock.setBlockBoundsBasedOnState: boxHit = %s\n", boxHit);
                setBlockBounds(boxHit);
                return;
            }
        }
        IBlockState state = getWorldBlockState(world, pos);
        AxisAlignedBB box = getLocalBounds(world, pos, state, null);
        if (box != null) {
            // System.out.printf("ShapeBlock.setBlockBoundsBasedOnState: local bounds = %s\n", box);
            setBlockBounds(box);
        } else {
            // System.out.printf("ShapeBlock.setBlockBoundsBasedOnState: calling super\n");
            super.setBlockBoundsBasedOnState(world, pos);
        }
    }

    public void setBlockBounds(AxisAlignedBB box) {
        setBlockBounds(
                (float) box.minX,
                (float) box.minY,
                (float) box.minZ,
                (float) box.maxX,
                (float) box.maxY,
                (float) box.maxZ);
    }

    @Override
    public void addCollisionBoxesToList(World world, BlockPos pos, IBlockState state, AxisAlignedBB clip, List result,
            Entity entity) {
        List<AxisAlignedBB> list = getGlobalCollisionBoxes(world, pos, state, entity);
        if (list != null) for (AxisAlignedBB box : list) if (clip.intersectsWith(box)) result.add(box);
    }

    protected List<AxisAlignedBB> getGlobalCollisionBoxes(IBlockAccess world, BlockPos pos, IBlockState state,
            Entity entity) {
        ShapeTE te = (ShapeTE) getWorldTileEntity(world, pos);
        if (te != null) {
            Trans3 t = te.localToGlobalTransformation();
            return getCollisionBoxes(te, world, pos, state, t, entity);
        }
        return null;
    }

    protected List<AxisAlignedBB> getLocalCollisionBoxes(IBlockAccess world, BlockPos pos, IBlockState state,
            Entity entity) {
        ShapeTE te = (ShapeTE) getWorldTileEntity(world, pos);
        if (te != null) {
            Trans3 t = te.localToGlobalTransformation(Vector3.zero);
            return getCollisionBoxes(te, world, pos, state, t, entity);
        }
        return null;
    }

    protected AxisAlignedBB getLocalBounds(IBlockAccess world, BlockPos pos, IBlockState state, Entity entity) {
        ShapeTE te = (ShapeTE) getWorldTileEntity(world, pos);
        if (te != null) {
            Trans3 t = te.localToGlobalTransformation(Vector3.blockCenter);
            return te.shape.kind.getBounds(te, world, pos, state, entity, t);
        }
        return null;
    }

    protected List<AxisAlignedBB> getCollisionBoxes(ShapeTE te, IBlockAccess world, BlockPos pos, IBlockState state,
            Trans3 t, Entity entity) {
        List<AxisAlignedBB> list = new ArrayList<>();
        te.shape.kind.addCollisionBoxesToList(te, world, pos, state, entity, t, list);
        return list;
    }

    @Override
    public boolean canHarvestBlock(IBlockState state, EntityPlayer player) {
        // System.out.printf("ShapeBlock.canHarvestBlock: by %s\n", player);
        return true;
    }

    @Override
    protected ArrayList<ItemStack> getDropsFromTileEntity(IBlockAccess world, BlockPos pos, IBlockState state,
            TileEntity te, int fortune) {
        ArrayList<ItemStack> result = new ArrayList<>();
        if (te instanceof ShapeTE ste) {
            ItemStack stack = ste.shape.kind.newStack(ste.shape, ste.baseBlockState, 1);
            result.add(stack);
            if (ste.secondaryBlockState != null) {
                stack = ste.shape.kind.newSecondaryMaterialStack(ste.secondaryBlockState);
                result.add(stack);
            }
        }
        return result;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos) {
        ShapeTE te = ShapeTE.get(world, pos);
        if (te != null) return te.newItemStack(1);
        else return null;
    }

    public IBlockState getBaseBlockState(IBlockAccess world, BlockPos pos) {
        ShapeTE te = getTileEntity(world, pos);
        if (te != null) return te.baseBlockState;
        return null;
    }

    @Override
    public float getPlayerRelativeBlockHardness(EntityPlayer player, World world, BlockPos pos) {
        float result = 1.0F;
        IBlockState base = getBaseBlockState(world, pos);
        if (base != null) {
            // System.out.printf("ShapeBlock.getPlayerRelativeBlockHardness: base = %s\n", base);
            result = acBlockStrength(base, player, world, pos);
        }
        return result;
    }

    public static float acBlockStrength(IBlockState state, EntityPlayer player, World world, BlockPos pos) {
        float hardness = BaseBlockUtils.getBlockHardness(state.getBlock(), world, pos);
        if (hardness < 0.0F) return 0.0F;
        float strength = getPlayerBreakSpeed(player, state, pos) / hardness;
        if (!acCanHarvestBlock(state, player)) return strength / 100F;
        else return strength / 30F;
    }

    public static boolean acCanHarvestBlock(IBlockState state, EntityPlayer player) {
        Block block = state.getBlock();
        if (block.getMaterial().isToolNotRequired()) return true;
        ItemStack stack = player.inventory.getCurrentItem();
        String tool = getBlockHarvestTool(state);
        if (stack == null || tool == null) return player.canHarvestBlock(block);
        int toolLevel = stack.getItem().getHarvestLevel(stack, tool);
        if (toolLevel < 0) return player.canHarvestBlock(block);
        else return toolLevel >= getBlockHarvestLevel(state);
    }

    @Override
    public IBlockState getParticleState(IBlockAccess world, BlockPos pos) {
        IBlockState base = getBaseBlockState(world, pos);
        if (base != null) return base;
        else return getDefaultState();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side,
            float hitX, float hitY, float hitZ) {
        ItemStack stack = player.inventory.getCurrentItem();
        if (stack != null) {
            ShapeTE te = ShapeTE.get(world, pos);
            if (te != null) return te.applySecondaryMaterial(stack, player);
        }
        return false;
    }

    @Override
    public boolean canRenderInLayer(EnumWorldBlockLayer layer) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public float getAmbientOcclusionLightValue() {
        return 0.8f;
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        int result = world.getBlockMetadata(x, y, z);
        // System.out.printf("ShapeBlock.getLightValue: at (%s, %s, %s) --> %s\n", x, y, z, result);
        return result;
    }

}