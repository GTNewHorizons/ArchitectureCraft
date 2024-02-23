package gcewing.architecture.blocks;

import net.minecraft.world.IBlockAccess;

import gcewing.architecture.compat.BlockPos;
import gcewing.architecture.compat.Trans3;
import gcewing.architecture.compat.Vector3;
import gcewing.architecture.rendering.ModelSpec;

public interface IBlock extends ITextureConsumer {

    void setRenderType(int id);

    String getQualifiedRendererClassName();

    ModelSpec getModelSpec(IBlockState state);

    int getNumSubtypes();

    Trans3 localToGlobalTransformation(IBlockAccess world, BlockPos pos, IBlockState state, Vector3 origin);

    // IBlockState getParticleState(IBlockAccess world, BlockPos pos);
    Class getDefaultItemClass();
}
