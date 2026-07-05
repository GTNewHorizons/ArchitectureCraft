package gcewing.architecture.compat;

import com.gtnewhorizons.postea.api.ItemStackReplacementManager;

public class PosteaCompat {

    public static void registerTransformers() {
        ItemStackReplacementManager.addTransformationHandler("ArchitectureCraft:shape", new ItemShapeTransformer());
    }
}
