// ------------------------------------------------------------------------------------------------
//
// Greg's Mod Base for 1.8 - Texture
//
// ------------------------------------------------------------------------------------------------

package gcewing.architecture.rendering;

import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import gcewing.architecture.BaseModClient.ITexture;
import gcewing.architecture.BaseModClient.ITiledTexture;

public abstract class BaseTexture implements ITexture {

    public ResourceLocation location;
    public int tintIndex;
    public double red = 1, green = 1, blue = 1;
    public boolean isEmissive;
    public boolean isProjected;

    public int tintIndex() {
        return tintIndex;
    }

    public double red() {
        return red;
    }

    public double green() {
        return green;
    }

    public double blue() {
        return blue;
    }

    public boolean isEmissive() {
        return isEmissive;
    }

    public boolean isProjected() {
        return isProjected;
    }

    public boolean isSolid() {
        return false;
    }

    public static Sprite fromSprite(IIcon icon) {
        return new Sprite(icon);
    }

    public static Image fromImage(ResourceLocation location) {
        return new Image(location);
    }

    public ResourceLocation location() {
        return location;
    }

    public ITexture tinted(int index) {
        BaseTexture result = new Proxy(this);
        result.tintIndex = index;
        return result;
    }

    public ITexture colored(double red, double green, double blue) {
        BaseTexture result = new Proxy(this);
        result.red = red;
        result.green = green;
        result.blue = blue;
        return result;
    }

    public ITexture emissive() {
        BaseTexture result = new Proxy(this);
        result.isEmissive = true;
        return result;
    }

    public ITexture projected() {
        BaseTexture result = new Proxy(this);
        result.isProjected = true;
        return result;
    }

    public ITiledTexture tiled(int numRows, int numCols) {
        return new TileSet(this, numRows, numCols);
    }

}
