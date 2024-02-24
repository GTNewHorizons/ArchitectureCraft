package gcewing.architecture.legacy.rendering;

public class Tile extends Proxy {

    protected final double u0;
    protected final double v0;
    protected final double uSize;
    protected final double vSize;

    public Tile(TileSet base, int row, int col) {
        super(base);
        uSize = base.tileSizeU;
        vSize = base.tileSizeV;
        u0 = uSize * col;
        v0 = vSize * row;
    }

    @Override
    public double interpolateU(double u) {
        return super.interpolateU(u0 + u * uSize);
    }

    @Override
    public double interpolateV(double v) {
        return super.interpolateV(v0 + v * vSize);
    }

}
