// ------------------------------------------------------
//
// ArchitectureCraft - Main
//
// ------------------------------------------------------

package gcewing.architecture;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import gcewing.architecture.common.network.DataChannel;
import gcewing.architecture.legacy.BaseMod;

@Mod(
        modid = ArchitectureCraft.MOD_ID,
        name = ArchitectureCraft.MOD_NAME,
        version = ArchitectureCraft.VERSION,
        acceptedMinecraftVersions = "[1.7.10]")
public class ArchitectureCraft extends BaseMod<ArchitectureCraftClient> {

    public static final String MOD_NAME = "ArchitectureCraft";
    public static final String MOD_ID = "ArchitectureCraft";
    public static final String VERSION = Tags.VERSION;
    public static final String ASSET_KEY = MOD_ID.toLowerCase();
    public static final String REGISTRY_PREFIX = MOD_ID.toLowerCase();

    public static final ArchitectureContent content = new ArchitectureContent();

    @Mod.Instance(MOD_ID)
    public static ArchitectureCraft mod;

    public static DataChannel channel;

    public ArchitectureCraft() {
        super();
        channel = new DataChannel(MOD_ID);
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        content.preInit(e);
        super.preInit(e);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        super.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        content.postInit(e);
        super.postInit(e);
    }

    @Override
    public ArchitectureCraftClient initClient() {
        return new ArchitectureCraftClient(this);
    }

}
