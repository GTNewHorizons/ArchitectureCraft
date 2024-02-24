package gcewing.architecture.client.render.model;

import java.util.List;

import net.minecraft.util.AxisAlignedBB;

import gcewing.architecture.client.render.ITexture;
import gcewing.architecture.client.render.target.IRenderTarget;
import gcewing.architecture.compat.Trans3;

public interface IArchitectureModel {

    AxisAlignedBB getBounds();

    void addBoxesToList(Trans3 t, List list);

    void render(Trans3 t, IRenderTarget renderer, ITexture... textures);
}
