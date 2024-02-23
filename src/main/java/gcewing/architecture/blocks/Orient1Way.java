package gcewing.architecture.blocks;

import gcewing.architecture.compat.BlockPos;
import gcewing.architecture.compat.Trans3;
import gcewing.architecture.compat.Vector3;
import gcewing.architecture.interfaces.IBlockState;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class Orient1Way implements IOrientationHandler {

    public void defineProperties(BaseBlock block) {}

    public IBlockState onBlockPlaced(Block block, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState baseState,
            EntityLivingBase placer) {
        return baseState;
    }

    public Trans3 localToGlobalTransformation(IBlockAccess world, BlockPos pos, IBlockState state, Vector3 origin) {
        return new Trans3(origin);
    }

}
