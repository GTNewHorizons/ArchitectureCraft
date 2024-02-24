package gcewing.architecture.client.texture;

import gcewing.architecture.client.render.ITexture;

public class TextureTileSet extends TextureProxy implements ITiledTexture {

    public final double tileSizeU;
    public final double tileSizeV;

    public TextureTileSet(ITexture base, int numRows, int numCols) {
        super(base);
        tileSizeU = 1.0 / numCols;
        tileSizeV = 1.0 / numRows;
    }

    public ITexture tile(int row, int col) {
        return new TextureTile(this, row, col);
    }

}
