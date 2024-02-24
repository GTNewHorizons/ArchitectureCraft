// ------------------------------------------------------------------------------------------------
//
// Greg's Mod Base for 1.7 Version B - Mod Subsystem
//
// ------------------------------------------------------------------------------------------------

package gcewing.architecture.legacy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class BaseSubsystem<M extends BaseMod, C extends BaseModClient> {

    public void preInit(FMLPreInitializationEvent e) {}

    public void init(FMLInitializationEvent e) {}

    public void postInit(FMLPostInitializationEvent e) {}

}
