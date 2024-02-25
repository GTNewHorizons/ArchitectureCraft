// ------------------------------------------------------------------------------------------------
//
// Greg's Mod Base for 1.7 Version B - Block orientation handlers
//
// ------------------------------------------------------------------------------------------------

package gcewing.architecture.compat;

public class Orientation {

    public static final IOrientationHandler orient4WaysByState = new Orient4WaysByState();
    public static final IOrientationHandler orient24WaysByTE = new Orient24WaysByTE();

}
