package gcewing.architecture.client.texture;

import gcewing.architecture.client.render.ITexture;

public interface ITiledTexture extends ITexture {

    ITexture tile(int row, int col);
}
