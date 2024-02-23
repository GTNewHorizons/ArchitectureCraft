package gcewing.architecture.utils;

import gcewing.architecture.blocks.Orient1Way;
import gcewing.architecture.compat.BlockPos;
import gcewing.architecture.compat.Trans3;
import gcewing.architecture.compat.Vector3;
import gcewing.architecture.interfaces.IBlockState;
import gcewing.architecture.tile.BaseTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

public class Orient24WaysByTE extends Orient1Way {

    public Trans3 localToGlobalTransformation(IBlockAccess world, BlockPos pos, IBlockState state, Vector3 origin) {
        TileEntity te = world.getTileEntity(pos.x, pos.y, pos.z);
        if (te instanceof BaseTileEntity bte) {
            return Trans3.sideTurn(origin, bte.side, bte.turn);
        } else return super.localToGlobalTransformation(world, pos, state, origin);
    }

}
