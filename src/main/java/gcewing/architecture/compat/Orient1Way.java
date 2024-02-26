package gcewing.architecture.compat;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import gcewing.architecture.common.block.BlockArchitecture;

public class Orient1Way implements IOrientationHandler {

    public void defineProperties(BlockArchitecture block) {}

    public IBlockState onBlockPlaced(Block block, World world, BlockPos pos, EnumFacing side, float hitX, float hitY,
            float hitZ, IBlockState baseState, EntityLivingBase placer) {
        return baseState;
    }

    @Override
    public Trans3 localToGlobalTransformation(IBlockAccess world, BlockPos pos, IBlockState state, TileEntity te,
            Vector3 origin) {
        return new Trans3(origin);
    }

}
