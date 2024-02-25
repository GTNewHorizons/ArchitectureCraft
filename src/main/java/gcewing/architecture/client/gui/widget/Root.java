package gcewing.architecture.client.gui.widget;

import java.util.ArrayList;
import java.util.List;

public class Root extends Group {

    public final Screen screen;
    public final List<IWidget> popupStack;

    public Root(Screen screen) {
        this.screen = screen;
        popupStack = new ArrayList<>();
    }

    @Override
    public int width() {
        return screen.getWidth();
    }

    @Override
    public int height() {
        return screen.getHeight();
    }

    @Override
    public IWidget dispatchMousePress(int x, int y, int button) {
        IWidget w = topPopup();
        if (w == null) w = super.dispatchMousePress(x, y, button);
        return w;
    }

    @Override
    public void addPopup(int x, int y, IWidget widget) {
        add(x, y, widget);
        popupStack.add(widget);
        screen.focusOn(widget);
    }

    @Override
    public void remove(IWidget widget) {
        super.remove(widget);
        popupStack.remove(widget);
        focusTopPopup();
    }

    public IWidget topPopup() {
        int n = popupStack.size();
        if (n > 0) return popupStack.get(n - 1);
        else return null;
    }

    void focusTopPopup() {
        IWidget w = topPopup();
        if (w != null) screen.focusOn(w);
    }

}
