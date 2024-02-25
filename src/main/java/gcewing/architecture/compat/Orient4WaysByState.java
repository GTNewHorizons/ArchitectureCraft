package gcewing.architecture.compat;

import static gcewing.architecture.util.Utils.horizontalFacings;
import static gcewing.architecture.util.Utils.iround;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import gcewing.architecture.common.block.BlockArchitecture;
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
        return baseState.withProperty(FACING, dir);
    }

    protected EnumFacing getHorizontalFacing(Entity entity) {
        return horizontalFacings[iround(entity.rotationYaw / 90.0) & 3];
    }

    public Trans3 localToGlobalTransformation(IBlockAccess world, BlockPos pos, IBlockState state, Vector3 origin) {
        EnumFacing f = state.getValue(FACING);
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
