package gcewing.architecture.gui;

import net.minecraft.client.Minecraft;

public class Widget implements IWidget {

    public IWidgetContainer parent;
    // public IWidget focus;
    public int left, top, width, height;

    public Widget() {}

    public Widget(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public IWidgetContainer parent() {
        return parent;
    }

    public void setParent(IWidgetContainer widget) {
        parent = widget;
    }

    public int left() {
        return left;
    }

    public int top() {
        return top;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public void setLeft(int x) {
        left = x;
    }

    public void setTop(int y) {
        top = y;
    }

    public void draw(Screen scr, int mouseX, int mouseY) {}

    public void mousePressed(MouseCoords m, int button) {}

    public void mouseMoved(MouseCoords m) {}

    public void mouseReleased(MouseCoords m, int button) {}

    public boolean keyPressed(char c, int key) {
        return false;
    }

    public void focusChanged(boolean state) {}

    public void close() {}

    public void layout() {}

    public IWidget dispatchMousePress(int x, int y, int button) {
        return this;
    }

    public boolean dispatchKeyPress(char c, int key) {
        return this.keyPressed(c, key);
    }

    public static int stringWidth(String s) {
        return Minecraft.getMinecraft().fontRenderer.getStringWidth(s);
    }

    public void addPopup(int x, int y, IWidget widget) {
        IWidget w = this;
        while (!(w instanceof Root)) {
            x += w.left();
            y += w.top();
            w = w.parent();
        }
        ((Root) w).addPopup(x, y, widget);
    }

    public void removePopup() {
        Root root = getRoot();
        root.remove(this);
    }

    public Root getRoot() {
        IWidget w = this;
        while (w != null && !(w instanceof Root)) w = w.parent();
        return (Root) w;
    }

}
