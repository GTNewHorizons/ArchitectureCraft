// ------------------------------------------------------------------------------------------------
//
// Greg's Mod Base for 1.7 Version B - Block orientation handlers
//
// ------------------------------------------------------------------------------------------------

package gcewing.architecture;

import static gcewing.architecture.BaseUtils.*;

import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

import gcewing.architecture.BaseBlock.IOrientationHandler;

public class BaseOrientation {

    public static final boolean debugPlacement = false;
    public static final boolean debugOrientation = false;

    public static final IOrientationHandler orient4WaysByState = new Orient4WaysByState();
    public static final IOrientationHandler orient24WaysByTE = new Orient24WaysByTE();

    // ------------------------------------------------------------------------------------------------

    public static class Orient4WaysByState implements IOrientationHandler {

        // public static IProperty FACING = PropertyDirection.create("facing", Plane.HORIZONTAL);
        public static final IProperty<EnumFacing> FACING = new PropertyTurn("facing");

        public void defineProperties(BaseBlock block) {
            block.addProperty(FACING);
        }

        public IBlockState onBlockPlaced(Block block, World world, BlockPos pos, EnumFacing side, float hitX,
                float hitY, float hitZ, IBlockState baseState, EntityLivingBase placer) {
            EnumFacing dir = getHorizontalFacing(placer);
            if (debugPlacement)
                System.out.printf("BaseOrientation.Orient4WaysByState: Placing block with FACING = %s\n", dir);
            return baseState.withProperty(FACING, dir);
        }

        protected EnumFacing getHorizontalFacing(Entity entity) {
            return horizontalFacings[iround(entity.rotationYaw / 90.0) & 3];
        }

        public Trans3 localToGlobalTransformation(IBlockAccess world, BlockPos pos, IBlockState state, Vector3 origin) {
            EnumFacing f = state.getValue(FACING);
            if (debugOrientation) System.out.printf(
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

    // ------------------------------------------------------------------------------------------------

    public static class Orient24WaysByTE extends BaseBlock.Orient1Way {

        public Trans3 localToGlobalTransformation(IBlockAccess world, BlockPos pos, IBlockState state, Vector3 origin) {
            TileEntity te = world.getTileEntity(pos.x, pos.y, pos.z);
            if (te instanceof BaseTileEntity bte) {
                return Trans3.sideTurn(origin, bte.side, bte.turn);
            } else return super.localToGlobalTransformation(world, pos, state, origin);
        }

    }

}
