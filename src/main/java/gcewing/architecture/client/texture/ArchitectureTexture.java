// ------------------------------------------------------------------------------------------------
//
// Greg's Mod Base for 1.8 - Texture
//
// ------------------------------------------------------------------------------------------------

package gcewing.architecture.client.texture;

import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import gcewing.architecture.client.render.ITexture;

public abstract class ArchitectureTexture implements ITexture {

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

    public static TextureSprite fromSprite(IIcon icon) {
        return new TextureSprite(icon);
    }

    public static Image fromImage(ResourceLocation location) {
        return new Image(location);
    }

    public ResourceLocation location() {
        return location;
    }

    public ITexture tinted(int index) {
        ArchitectureTexture result = new TextureProxy(this);
        result.tintIndex = index;
        return result;
    }

    public ITexture colored(double red, double green, double blue) {
        ArchitectureTexture result = new TextureProxy(this);
        result.red = red;
        result.green = green;
        result.blue = blue;
        return result;
    }

    public ITexture emissive() {
        ArchitectureTexture result = new TextureProxy(this);
        result.isEmissive = true;
        return result;
    }

    public ITexture projected() {
        ArchitectureTexture result = new TextureProxy(this);
        result.isProjected = true;
        return result;
    }

    public ITiledTexture tiled(int numRows, int numCols) {
        return new TextureTileSet(this, numRows, numCols);
    }

}
