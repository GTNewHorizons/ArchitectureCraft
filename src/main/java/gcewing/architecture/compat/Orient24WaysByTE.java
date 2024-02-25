package gcewing.architecture.compat;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

import gcewing.architecture.common.tile.TileArchitecture;

public class Orient24WaysByTE extends Orient1Way {

    public Trans3 localToGlobalTransformation(IBlockAccess world, BlockPos pos, IBlockState state, Vector3 origin) {
        TileEntity te = world.getTileEntity(pos.x, pos.y, pos.z);
        if (te instanceof TileArchitecture bte) {
            return Trans3.sideTurn(origin, bte.side, bte.turn);
        } else return super.localToGlobalTransformation(world, pos, state, origin);
    }

}
