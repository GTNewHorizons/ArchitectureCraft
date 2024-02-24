package gcewing.architecture.legacy.rendering;

import java.util.List;

import net.minecraft.util.AxisAlignedBB;

import gcewing.architecture.compat.Trans3;

public interface IModel {

    AxisAlignedBB getBounds();

    void addBoxesToList(Trans3 t, List list);

    void render(Trans3 t, IRenderTarget renderer, ITexture... textures);
}
