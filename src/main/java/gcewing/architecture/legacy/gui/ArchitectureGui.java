// ------------------------------------------------------------------------------------------------
//
// Greg's Mod Base - Generic GUI Screen
//
// ------------------------------------------------------------------------------------------------

package gcewing.architecture.legacy.gui;

// ------------------------------------------------------------------------------------------------

public class ArchitectureGui {

    static boolean isFocused(IWidget widget) {
        if (widget == null) return false;
        else if (widget instanceof Root) return true;
        else {
            IWidgetContainer parent = widget.parent();
            return (parent != null && parent.getFocus() == widget && isFocused(parent));
        }
    }

    static void tellFocusChanged(IWidget widget, boolean state) {
        if (widget != null) {
            widget.focusChanged(state);
            if (widget instanceof IWidgetContainer) tellFocusChanged(((IWidgetContainer) widget).getFocus(), state);
        }
    }

}
