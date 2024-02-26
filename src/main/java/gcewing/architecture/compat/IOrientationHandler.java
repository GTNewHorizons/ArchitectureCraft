package gcewing.architecture.compat;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import gcewing.architecture.common.block.BlockArchitecture;

public interface IOrientationHandler {

    void defineProperties(BlockArchitecture block);

    IBlockState onBlockPlaced(Block block, World world, BlockPos pos, EnumFacing side, float hitX, float hitY,
            float hitZ, IBlockState baseState, EntityLivingBase placer);

    Trans3 localToGlobalTransformation(IBlockAccess world, BlockPos pos, IBlockState state, TileEntity te,
            Vector3 origin);
}
