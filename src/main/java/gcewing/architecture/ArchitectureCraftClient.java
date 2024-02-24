// ------------------------------------------------------
//
// ArchitectureCraft - Client Proxy
//
// ------------------------------------------------------

package gcewing.architecture;

// import cpw.mods.fml.client.registry.RenderingRegistry;

import gcewing.architecture.common.shape.ShapeRenderDispatch;
import gcewing.architecture.legacy.BaseModClient;
import gcewing.architecture.legacy.rendering.CladdingRenderer;
import gcewing.architecture.legacy.rendering.RenderWindow;

public class ArchitectureCraftClient extends BaseModClient<ArchitectureCraft> {

    public static final ShapeRenderDispatch shapeRenderDispatch = new ShapeRenderDispatch();

    public ArchitectureCraftClient(ArchitectureCraft mod) {
        super(mod);
        // debugModelRegistration = true;
        RenderWindow.init(this);
    }

    @Override
    protected void registerBlockRenderers() {
        addBlockRenderer(ArchitectureCraft.content.blockShape, shapeRenderDispatch);
        addBlockRenderer(ArchitectureCraft.content.blockShapeSE, shapeRenderDispatch);
    }

    @Override
    protected void registerItemRenderers() {
        addItemRenderer(ArchitectureCraft.content.itemCladding, new CladdingRenderer());
    }

}
