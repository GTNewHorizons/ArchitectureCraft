package gcewing.architecture.legacy.rendering;

public interface ITiledTexture extends ITexture {

    ITexture tile(int row, int col);
}
