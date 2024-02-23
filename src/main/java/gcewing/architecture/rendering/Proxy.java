package gcewing.architecture.rendering;

import gcewing.architecture.BaseModClient;

public class Proxy extends BaseTexture {

    public final BaseModClient.ITexture base;

    public Proxy(BaseModClient.ITexture base) {
        this.base = base;
        this.location = base.location();
        this.tintIndex = base.tintIndex();
        this.red = base.red();
        this.green = base.green();
        this.blue = base.blue();
        this.isEmissive = base.isEmissive();
        this.isProjected = base.isProjected();
    }

    // @Override
    // public ResourceLocation location() {
    // return base.location();
    // }

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
