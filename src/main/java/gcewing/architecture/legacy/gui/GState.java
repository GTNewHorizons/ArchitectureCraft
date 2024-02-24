package gcewing.architecture.legacy.gui;

import net.minecraft.util.ResourceLocation;

public class GState {

    public GState previous;
    public double uscale, vscale;
    public float red, green, blue;
    public int textColor;
    public boolean textShadow;
    public ResourceLocation texture;

    public GState() {
        uscale = 1;
        vscale = 1;
        red = green = blue = 1;
        textColor = GuiText.FontColor.getColor();
        textShadow = false;
    }

    public GState(GState previous) {
        this.previous = previous;
        this.uscale = previous.uscale;
        this.vscale = previous.vscale;
        this.red = previous.red;
        this.green = previous.green;
        this.blue = previous.blue;
        this.textColor = previous.textColor;
        this.textShadow = previous.textShadow;
        this.texture = previous.texture;
    }

}
