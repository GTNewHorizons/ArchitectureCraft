// ------------------------------------------------------
//
// ArchitectureCraft - Client Proxy
//
// ------------------------------------------------------

package gcewing.architecture;

// import cpw.mods.fml.client.registry.RenderingRegistry;

import gcewing.architecture.gui.SawbenchGui;
import gcewing.architecture.rendering.CladdingRenderer;
import gcewing.architecture.rendering.RenderWindow;
import gcewing.architecture.shapes.ShapeRenderDispatch;

public class ArchitectureCraftClient extends BaseModClient<ArchitectureCraft> {

    public static final ShapeRenderDispatch shapeRenderDispatch = new ShapeRenderDispatch();

    public ArchitectureCraftClient(ArchitectureCraft mod) {
        super(mod);
        // debugModelRegistration = true;
        RenderWindow.init(this);
    }

    @Override
    void registerScreens() {
        addScreen(ArchitectureCraft.guiSawbench, SawbenchGui.class);
    }

    @Override
    protected void registerBlockRenderers() {
        addBlockRenderer(ArchitectureCraft.blockShape, shapeRenderDispatch);
        addBlockRenderer(ArchitectureCraft.blockShapeSE, shapeRenderDispatch);
    }

    @Override
    protected void registerItemRenderers() {
        addItemRenderer(ArchitectureCraft.itemCladding, new CladdingRenderer());
    }

}
