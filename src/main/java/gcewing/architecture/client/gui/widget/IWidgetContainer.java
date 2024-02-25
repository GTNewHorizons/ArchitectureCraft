package gcewing.architecture.client.gui.widget;

public interface IWidgetContainer extends IWidget {

    // void add(int left, int top, IWidget widget);
    IWidget getFocus();

    void setFocus(IWidget widget);
    // void onAction(IWidget sender, String action);
}
