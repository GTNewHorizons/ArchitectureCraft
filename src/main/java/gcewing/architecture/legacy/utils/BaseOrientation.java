// ------------------------------------------------------------------------------------------------
//
// Greg's Mod Base for 1.7 Version B - Block orientation handlers
//
// ------------------------------------------------------------------------------------------------

package gcewing.architecture.legacy.utils;

import gcewing.architecture.legacy.blocks.IOrientationHandler;

public class BaseOrientation {

    public static final boolean debugPlacement = false;
    public static final boolean debugOrientation = false;

    public static final IOrientationHandler orient4WaysByState = new Orient4WaysByState();
    public static final IOrientationHandler orient24WaysByTE = new Orient24WaysByTE();

    // ------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------

}
