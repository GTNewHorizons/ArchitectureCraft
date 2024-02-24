package gcewing.architecture.legacy.rendering;

import net.minecraft.util.IIcon;

public class Debug extends Sprite {

    public Debug(IIcon icon) {
        super(icon);
    }

    @Override
    public double interpolateU(double u) {
        double iu = super.interpolateU(u);
        System.out.printf("BaseTexture: %s u (%s - %s)\n", icon.getIconName(), icon.getMinU(), icon.getMaxU());
        System.out.printf("BaseTexture: u %s --> %s\n", u, iu);
        return iu;
    }

    public double interpolateV(double v) {
        double iv = super.interpolateV(v);
        System.out.printf("BaseTexture: %s v (%s - %s)\n", icon.getIconName(), icon.getMinV(), icon.getMaxV());
        System.out.printf("BaseTexture: v %s --> %s\n", v, iv);
        return iv;
    }

}
