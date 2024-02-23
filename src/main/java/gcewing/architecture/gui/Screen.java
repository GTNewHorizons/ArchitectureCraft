package gcewing.architecture.gui;

import static gcewing.architecture.utils.BaseUtils.packedColor;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2d;
import static org.lwjgl.opengl.GL11.glVertex3d;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import gcewing.architecture.BaseMod;
import gcewing.architecture.interfaces.ISetMod;

public class Screen extends GuiContainer implements ISetMod {

    final static int defaultTextColor = GuiText.FontColor.getColor();

    protected BaseMod mod;
    // double uscale, vscale;
    // float red = 1.0F, green = 1.0F, blue = 1.0F;
    // public int textColor = defaultTextColor;
    // public boolean textShadow = false;
    protected final Root root;
    protected String title;
    protected IWidget mouseWidget;
    protected GState gstate;

    public Screen(Container container, int width, int height) {
        super(container);
        xSize = width;
        ySize = height;
        root = new Root(this);
        initGraphics();
    }

    public Screen(BaseContainer container) {
        this(container, container.xSize, container.ySize);
    }

    public int getWidth() {
        return xSize;
    }

    public int getHeight() {
        return ySize;
    }

    @Override
    public void setMod(BaseMod mod) {
        this.mod = mod;
    }

    @Override
    public void initGui() {
        super.initGui();
        root.layout();
    }

    protected void initGraphics() {
        gstate = new GState();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
        GL11.glPushMatrix();
        GL11.glTranslatef(guiLeft, guiTop, 0.0F);
        initGraphics();
        drawBackgroundLayer();
        if (title != null) drawTitle(title);
        root.draw(this, mouseX - guiLeft, mouseY - guiTop);
        GL11.glPopMatrix();
    }

    protected void drawBackgroundLayer() {
        initGraphics();
        drawGuiBackground(0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        drawForegroundLayer();
    }

    protected void drawForegroundLayer() {}

    public void close() {
        dispatchClosure(root);
        onClose();
        mc.thePlayer.closeScreen();
    }

    protected void onClose() {}

    public void bindTexture(String path) {
        bindTexture(path, 1, 1);
    }

    public void bindTexture(String path, int usize, int vsize) {
        bindTexture(mod.client.textureLocation(path), usize, vsize);
    }

    public void bindTexture(ResourceLocation rsrc) {
        bindTexture(rsrc, 1, 1);
    }

    public void bindTexture(ResourceLocation rsrc, int usize, int vsize) {
        gstate.texture = rsrc;
        mc.getTextureManager().bindTexture(rsrc);
        gstate.uscale = 1.0 / usize;
        gstate.vscale = 1.0 / vsize;
    }

    public void gSave() {
        gstate = new GState(gstate);
    }

    public void gRestore() {
        if (gstate.previous != null) {
            gstate = gstate.previous;
            mc.getTextureManager().bindTexture(gstate.texture);
        } else System.out.print("BaseGui: Warning: Graphics state stack underflow\n");
    }

    public void drawRect(double x, double y, double w, double h) {
        glDisable(GL_TEXTURE_2D);
        glColor3d(gstate.red, gstate.green, gstate.blue);
        glBegin(GL_QUADS);
        glVertex3d(x, y + h, zLevel);
        glVertex3d(x + w, y + h, zLevel);
        glVertex3d(x + w, y, zLevel);
        glVertex3d(x, y, zLevel);
        glEnd();
        glEnable(GL_TEXTURE_2D);
    }

    public void drawBorderedRect(double x, double y, double w, double h, double u, double v, double uSize, double vSize,
            double cornerWidth, double cornerHeight) {
        double cw = cornerWidth, ch = cornerHeight;
        double sw = w - 2 * cornerWidth; // side width
        double sh = h - 2 * cornerHeight; // side height
        double usw = uSize - 2 * cornerWidth; // u side width
        double ush = vSize - 2 * cornerHeight; // v side height
        double x1 = x + cw, x2 = w - cw;
        double y1 = y + ch, y2 = h - ch;
        double u1 = u + cw, u2 = uSize - cw;
        double v1 = v + ch, v2 = vSize - cw;
        drawTexturedRect(x, y, cw, ch, u, v); // top left corner
        drawTexturedRect(x2, y, cw, ch, u2, v); // top right corner
        drawTexturedRect(x, y2, cw, ch, u, v2); // bottom left corner
        drawTexturedRect(x2, y2, cw, ch, u2, v2); // bottom right corner
        drawTexturedRect(x1, y, sw, ch, u1, v, usw, ch); // top side
        drawTexturedRect(x1, y2, sw, ch, u1, v2, usw, ch); // bottom side
        drawTexturedRect(x, y1, cw, sh, u, v1, cw, ush); // left side
        drawTexturedRect(x2, y1, cw, sh, u2, v1, cw, ush); // right side
        drawTexturedRect(x1, y1, sw, sh, u1, v1, usw, ush); // centre
    }

    public void drawGuiBackground(double x, double y, double w, double h) {
        bindTexture("gui/gui_background.png", 16, 16);
        setColor(0xffffff);
        drawBorderedRect(x, y, w, h, 0, 0, 16, 16, 4, 4);
    }

    public void drawTexturedRect(double x, double y, double w, double h) {
        drawTexturedRectUV(x, y, w, h, 0, 0, 1, 1);
    }

    public void drawTexturedRect(double x, double y, double w, double h, double u, double v) {
        drawTexturedRect(x, y, w, h, u, v, w, h);
    }

    public void drawTexturedRect(double x, double y, double w, double h, double u, double v, double us, double vs) {
        drawTexturedRectUV(x, y, w, h, u * gstate.uscale, v * gstate.vscale, us * gstate.uscale, vs * gstate.vscale);
    }

    public void drawTexturedRectUV(double x, double y, double w, double h, double u, double v, double us, double vs) {
        glBegin(GL_QUADS);
        glColor3f(gstate.red, gstate.green, gstate.blue);
        glTexCoord2d(u, v + vs);
        glVertex3d(x, y + h, zLevel);
        glTexCoord2d(u + us, v + vs);
        glVertex3d(x + w, y + h, zLevel);
        glTexCoord2d(u + us, v);
        glVertex3d(x + w, y, zLevel);
        glTexCoord2d(u, v);
        glVertex3d(x, y, zLevel);
        glEnd();
    }

    public void setColor(int hex) {
        setColor((hex >> 16) / 255.0, ((hex >> 8) & 0xff) / 255.0, (hex & 0xff) / 255.0);
    }

    public void setColor(double r, double g, double b) {
        gstate.red = (float) r;
        gstate.green = (float) g;
        gstate.blue = (float) b;
    }

    public void resetColor() {
        setColor(1, 1, 1);
    }

    public void setTextColor(int hex) {
        gstate.textColor = hex;
    }

    public void setTextColor(double red, double green, double blue) {
        setTextColor(packedColor(red, green, blue));
    }

    public void setTextShadow(boolean state) {
        gstate.textShadow = state;
    }

    public void drawString(String s, int x, int y) {
        fontRendererObj.drawString(s, x, y, gstate.textColor, gstate.textShadow);
    }

    public void drawCenteredString(String s, int x, int y) {
        fontRendererObj
                .drawString(s, x - fontRendererObj.getStringWidth(s) / 2, y, gstate.textColor, gstate.textShadow);
    }

    public void drawRightAlignedString(String s, int x, int y) {
        fontRendererObj.drawString(s, x - fontRendererObj.getStringWidth(s), y, gstate.textColor, gstate.textShadow);
    }

    public void drawTitle(String s) {
        drawCenteredString(s, xSize / 2, 4);
    }

    public void drawInventoryName(IInventory inv, int x, int y) {
        drawString(inventoryName(inv), x, y);
    }

    public void drawPlayerInventoryName() {
        drawString(playerInventoryName(), 8, ySize - 96 + 2);
    }

    public static String inventoryName(IInventory inv) {
        String name = inv.getInventoryName();
        if (!inv.hasCustomInventoryName()) name = StatCollector.translateToLocal(name);
        return name;
    }

    public static String playerInventoryName() {
        return StatCollector.translateToLocal("container.inventory");
    }

    @Override
    protected void mouseMovedOrUp(int x, int y, int button) {
        super.mouseMovedOrUp(x, y, button);
        if (mouseWidget != null) {
            MouseCoords m = new MouseCoords(mouseWidget, x, y);
            if (button == -1) mouseWidget.mouseMoved(m);
            else {
                mouseWidget.mouseReleased(m, button);
                mouseWidget = null;
            }
        }
    }

    @Override
    public void mouseClicked(int x, int y, int button) {
        super.mouseClicked(x, y, button);
        mousePressed(x - guiLeft, y - guiTop, button);
    }

    protected void mousePressed(int x, int y, int button) {
        mouseWidget = root.dispatchMousePress(x, y, button);
        if (mouseWidget != null) {
            closeOldFocus(mouseWidget);
            focusOn(mouseWidget);
            mouseWidget.mousePressed(new MouseCoords(mouseWidget, x, y), button);
        }
    }

    void closeOldFocus(IWidget clickedWidget) {
        if (!BaseGui.isFocused(clickedWidget)) {
            IWidgetContainer parent = clickedWidget.parent();
            while (!BaseGui.isFocused(parent)) parent = parent.parent();
            dispatchClosure(parent.getFocus());
        }
    }

    void dispatchClosure(IWidget target) {
        while (target != null) {
            target.close();
            target = getFocusOf(target);
        }
    }

    IWidget getFocusOf(IWidget widget) {
        if (widget instanceof IWidgetContainer) return ((IWidgetContainer) widget).getFocus();
        else return null;
    }

    @Override
    public void keyTyped(char c, int key) {
        if (!root.dispatchKeyPress(c, key)) {
            if (key == 1 || key == mc.gameSettings.keyBindInventory.getKeyCode()) close();
            else super.keyTyped(c, key);
        }
    }

    public void focusOn(IWidget newFocus) {
        // System.out.printf("BaseGui.Screen.focusOn: %s\n", name(newFocus));
        IWidgetContainer parent = newFocus.parent();
        if (parent != null) {
            IWidget oldFocus = parent.getFocus();
            // System.out.printf("BaseGui.Screen.focusOn: Old parent focus = %s\n", name(oldFocus));
            if (BaseGui.isFocused(parent)) {
                // System.out.printf("BaseGui.Screen.focusOn: Parent is focused\n");
                if (oldFocus != newFocus) {
                    BaseGui.tellFocusChanged(oldFocus, false);
                    parent.setFocus(newFocus);
                    BaseGui.tellFocusChanged(newFocus, true);
                }
            } else {
                // System.out.printf("BaseGui.Screen.focusOn: Parent is not focused\n");
                parent.setFocus(newFocus);
                focusOn(parent);
            }
        }
    }

    public void focusChanged(boolean state) {}

}
