package gcewing.architecture.legacy.utils;

import static gcewing.architecture.legacy.utils.BaseUtils.horizontalFacings;
import static gcewing.architecture.legacy.utils.BaseUtils.iround;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import gcewing.architecture.common.block.BlockArchitecture;
import gcewing.architecture.compat.BlockPos;
import gcewing.architecture.compat.Trans3;
import gcewing.architecture.compat.Vector3;
import gcewing.architecture.legacy.blocks.blocks.IBlockState;
import gcewing.architecture.legacy.blocks.blocks.IOrientationHandler;
import gcewing.architecture.legacy.blocks.blocks.IProperty;
import gcewing.architecture.legacy.properties.PropertyTurn;

public class Orient4WaysByState implements IOrientationHandler {

    // public static IProperty FACING = PropertyDirection.create("facing", Plane.HORIZONTAL);
    public static final IProperty<EnumFacing> FACING = new PropertyTurn("facing");

    public void defineProperties(BlockArchitecture block) {
        block.addProperty(FACING);
    }

    public IBlockState onBlockPlaced(Block block, World world, BlockPos pos, EnumFacing side, float hitX, float hitY,
            float hitZ, IBlockState baseState, EntityLivingBase placer) {
        EnumFacing dir = getHorizontalFacing(placer);
        if (BaseOrientation.debugPlacement)
            System.out.printf("BaseOrientation.Orient4WaysByState: Placing block with FACING = %s\n", dir);
        return baseState.withProperty(FACING, dir);
    }

    protected EnumFacing getHorizontalFacing(Entity entity) {
        return horizontalFacings[iround(entity.rotationYaw / 90.0) & 3];
    }

    public Trans3 localToGlobalTransformation(IBlockAccess world, BlockPos pos, IBlockState state, Vector3 origin) {
        EnumFacing f = state.getValue(FACING);
        if (BaseOrientation.debugOrientation) System.out.printf(
                "BaseOrientation.Orient4WaysByState.localToGlobalTransformation: for %s: facing = %s\n",
                state,
                f);
        int i = switch (f) {
            case NORTH -> 0;
            case WEST -> 1;
            case SOUTH -> 2;
            case EAST -> 3;
            default -> 0;
        };
        return new Trans3(origin).turn(i);
    }

}
