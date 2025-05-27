package gcewing.architecture.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.ForgeHooksClient;

import com.gtnewhorizons.angelica.api.ThreadSafeISBRH;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import gcewing.architecture.ArchitectureCraftClient;
import gcewing.architecture.client.render.target.RenderTargetWorld;
import gcewing.architecture.common.block.BlockArchitecture;
import gcewing.architecture.compat.BlockPos;
import gcewing.architecture.compat.EnumWorldBlockLayer;
import gcewing.architecture.compat.IBlockState;
import gcewing.architecture.compat.Trans3;

@ThreadSafeISBRH(perThread = false)
public class BlockRenderDispatcher implements ISimpleBlockRenderingHandler {

    private final ArchitectureCraftClient client;
    public final int renderID;

    public BlockRenderDispatcher(ArchitectureCraftClient baseModClient) {
        this.client = baseModClient;
        renderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(renderID, this);
    }

    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
            RenderBlocks rb) {
        boolean result = false;
        BlockPos pos = new BlockPos(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);
        BlockArchitecture<?> baseBlock = (BlockArchitecture<?>) block;
        IBlockState state = baseBlock.getStateFromMeta(meta);
        ICustomRenderer renderer = client.getCustomBlockRenderer(world, pos, state);
        if (renderer != null) {
            int pass = ForgeHooksClient.getWorldRenderPass();
            for (EnumWorldBlockLayer layer : ArchitectureCraftClient.passLayers[pass + 1]) {
                if (baseBlock.canRenderInLayer(layer)) {
                    RenderTargetWorld target = new RenderTargetWorld(
                            world,
                            pos,
                            Tessellator.instance,
                            rb.overrideBlockTexture,
                            false);
                    Trans3 t = Trans3.blockCenter(pos);
                    renderer.renderBlock(world, pos, state, target, layer, t);
                    if (target.end()) result = true;
                }
            }
        }
        return result;
    }

    public void renderInventoryBlock(Block block, int meta, int modelId, RenderBlocks renderer) {}

    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    public int getRenderId() {
        return renderID;
    }

}
