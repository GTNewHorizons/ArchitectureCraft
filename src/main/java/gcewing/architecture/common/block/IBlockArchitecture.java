package gcewing.architecture.common.block;

import net.minecraft.world.IBlockAccess;

import gcewing.architecture.client.texture.ITextureConsumer;
import gcewing.architecture.common.render.ModelSpec;
import gcewing.architecture.compat.BlockPos;
import gcewing.architecture.compat.IBlockState;
import gcewing.architecture.compat.Trans3;
import gcewing.architecture.compat.Vector3;

public interface IBlockArchitecture extends ITextureConsumer {

    void setRenderType(int id);

    String getQualifiedRendererClassName();

    ModelSpec getModelSpec(IBlockState state);

    Trans3 localToGlobalTransformation(IBlockAccess world, BlockPos pos, IBlockState state, Vector3 origin);

}
