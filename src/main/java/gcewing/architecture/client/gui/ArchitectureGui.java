// ------------------------------------------------------------------------------------------------
//
// Greg's Mod Base - Generic GUI Screen
//
// ------------------------------------------------------------------------------------------------

package gcewing.architecture.client.gui;

// ------------------------------------------------------------------------------------------------

import gcewing.architecture.client.gui.widget.IWidget;
import gcewing.architecture.client.gui.widget.IWidgetContainer;
import gcewing.architecture.client.gui.widget.Root;

public class ArchitectureGui {

    public static boolean isFocused(IWidget widget) {
        if (widget == null) return false;
        else if (widget instanceof Root) return true;
        else {
            IWidgetContainer parent = widget.parent();
            return (parent != null && parent.getFocus() == widget && isFocused(parent));
        }
    }

    public static void tellFocusChanged(IWidget widget, boolean state) {
        if (widget != null) {
            widget.focusChanged(state);
            if (widget instanceof IWidgetContainer) tellFocusChanged(((IWidgetContainer) widget).getFocus(), state);
        }
    }

}
