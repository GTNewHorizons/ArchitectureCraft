package gcewing.architecture.client.texture;

import net.minecraft.util.IIcon;

public class TextureSprite extends ArchitectureTexture {

    public final IIcon icon;

    public TextureSprite(IIcon icon) {
        this.icon = icon;
        red = green = blue = 1.0;
    }

    public double interpolateU(double u) {
        return icon.getInterpolatedU(u * 16);
    }

    public double interpolateV(double v) {
        return icon.getInterpolatedV(v * 16);
    }

    @Override
    public String toString() {
        return String.format(
                "BaseTexture.Sprite(%.4f,%.4f,%.4f,%.4f)",
                interpolateU(0),
                interpolateV(0),
                interpolateU(1),
                interpolateV(1));
    }

}
