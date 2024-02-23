package gcewing.architecture.gui;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslated;

import java.util.ArrayList;
import java.util.List;

public class Group extends Widget implements IWidgetContainer {

    protected final List<IWidget> widgets = new ArrayList<>();
    protected IWidget focus;

    public IWidget getFocus() {
        return focus;
    }

    public void setFocus(IWidget widget) {
        focus = widget;
    }

    public void add(int left, int top, IWidget widget) {
        widget.setLeft(left);
        widget.setTop(top);
        widget.setParent(this);
        widgets.add(widget);
    }

    public void remove(IWidget widget) {
        widgets.remove(widget);
        if (getFocus() == widget) {
            if (BaseGui.isFocused(this)) BaseGui.tellFocusChanged(widget, false);
            setFocus(null);
        }
    }

    @Override
    public void draw(Screen scr, int mouseX, int mouseY) {
        super.draw(scr, mouseX, mouseY);
        for (IWidget w : widgets) {
            int dx = w.left(), dy = w.top();
            glPushMatrix();
            glTranslated(dx, dy, 0);
            w.draw(scr, mouseX - dx, mouseY - dy);
            glPopMatrix();
        }
    }

    @Override
    public IWidget dispatchMousePress(int x, int y, int button) {
        IWidget target = findWidget(x, y);
        if (target != null) return target.dispatchMousePress(x - target.left(), y - target.top(), button);
        else return this;
    }

    @Override
    public boolean dispatchKeyPress(char c, int key) {
        IWidget focus = getFocus();
        if (focus != null && focus.dispatchKeyPress(c, key)) return true;
        else return super.dispatchKeyPress(c, key);
    }

    public IWidget findWidget(int x, int y) {
        for (int i = widgets.size() - 1; i >= 0; i--) {
            IWidget w = widgets.get(i);
            int l = w.left(), t = w.top();
            if (x >= l && y >= t && x < l + w.width() && y < t + w.height()) return w;
        }
        return null;
    }

    @Override
    public void layout() {
        for (IWidget w : widgets) w.layout();
    }

}
