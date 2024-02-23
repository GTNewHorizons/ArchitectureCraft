package gcewing.architecture.rendering;

import net.minecraft.util.ResourceLocation;

public class Image extends BaseTexture {

    // public ResourceLocation location;

    public Image(ResourceLocation location) {
        this.location = location;
    }

    public double interpolateU(double u) {
        return u;
    }

    public double interpolateV(double v) {
        return v;
    }

}
