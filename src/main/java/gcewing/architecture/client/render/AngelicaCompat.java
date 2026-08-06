package gcewing.architecture.client.render;

import net.coderbot.iris.Iris;
import net.irisshaders.iris.api.v0.IrisApi;
import net.minecraft.block.Block;

public class AngelicaCompat {

    public void setShaderMaterialOverride(Block block, int meta) {
        Iris.setShaderMaterialOverride(block, meta);
    }

    public void resetShaderMaterialOverride() {
        Iris.resetShaderMaterialOverride();
    }

    public boolean isShaderPackInUse() {
        return IrisApi.getInstance().isShaderPackInUse();
    }
}
