package gcewing.architecture.rendering;

public class Solid extends BaseTexture {

    public Solid(double red, double green, double blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    @Override
    public boolean isSolid() {
        return true;
    }

    public double interpolateU(double u) {
        return 0;
    }

    public double interpolateV(double v) {
        return 0;
    }

}
