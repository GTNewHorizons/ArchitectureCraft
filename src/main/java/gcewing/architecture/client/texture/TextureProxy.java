package gcewing.architecture.client.texture;

import gcewing.architecture.client.render.ITexture;

public class TextureProxy extends ArchitectureTexture {

    public final ITexture base;

    public TextureProxy(ITexture base) {
        this.base = base;
        this.location = base.location();
        this.tintIndex = base.tintIndex();
        this.red = base.red();
        this.green = base.green();
        this.blue = base.blue();
        this.isEmissive = base.isEmissive();
        this.isProjected = base.isProjected();
        this.baseBlock = base.baseBlock();
        this.baseMeta = base.baseMeta();
    }

    @Override
    public boolean isSolid() {
        return base.isSolid();
    }

    public double interpolateU(double u) {
        return base.interpolateU(u);
    }

    public double interpolateV(double v) {
        return base.interpolateV(v);
    }

}
