package gcewing.architecture.legacy.rendering;

public class TileSet extends Proxy implements ITiledTexture {

    public final double tileSizeU;
    public final double tileSizeV;

    public TileSet(ITexture base, int numRows, int numCols) {
        super(base);
        tileSizeU = 1.0 / numCols;
        tileSizeV = 1.0 / numRows;
    }

    public ITexture tile(int row, int col) {
        return new Tile(this, row, col);
    }

}
