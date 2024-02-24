package gcewing.architecture.legacy.gui;

public class MouseCoords {

    final int x;
    final int y;

    public MouseCoords(IWidget widget, int x, int y) {
        while (widget != null) {
            x -= widget.left();
            y -= widget.top();
            widget = widget.parent();
        }
        this.x = x;
        this.y = y;
    }

}
