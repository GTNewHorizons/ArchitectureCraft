package gcewing.architecture.rendering;

import gcewing.architecture.BaseModClient;

public class TileSet extends Proxy implements BaseModClient.ITiledTexture {

    public final double tileSizeU;
    public final double tileSizeV;

    public TileSet(BaseModClient.ITexture base, int numRows, int numCols) {
        super(base);
        tileSizeU = 1.0 / numCols;
        tileSizeV = 1.0 / numRows;
    }

    public BaseModClient.ITexture tile(int row, int col) {
        return new Tile(this, row, col);
    }

}
