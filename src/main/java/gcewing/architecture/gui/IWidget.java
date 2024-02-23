package gcewing.architecture.gui;

public interface IWidget {

    IWidgetContainer parent();

    void setParent(IWidgetContainer widget);

    int left();

    int top();

    int width();

    int height();

    void setLeft(int x);

    void setTop(int y);

    void draw(Screen scr, int mouseX, int mouseY);

    IWidget dispatchMousePress(int x, int y, int button);

    boolean dispatchKeyPress(char c, int key);

    void mousePressed(MouseCoords m, int button);

    void mouseMoved(MouseCoords m);

    void mouseReleased(MouseCoords m, int button);

    boolean keyPressed(char c, int key);

    void focusChanged(boolean state);

    void close();

    void layout();
}
