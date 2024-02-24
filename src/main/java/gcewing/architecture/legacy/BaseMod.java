// ------------------------------------------------------------------------------------------------
//
// Greg's Mod Base for 1.7 Version B - Generic Mod
//
// ------------------------------------------------------------------------------------------------

package gcewing.architecture.legacy;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.server.management.PlayerManager;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import gcewing.architecture.ArchitectureGuiHandler;
import gcewing.architecture.common.config.ArchitectConfiguration;
import gcewing.architecture.legacy.rendering.BaseModel;
import gcewing.architecture.legacy.rendering.IModel;

public class BaseMod<CLIENT extends BaseModClient<? extends BaseMod>> extends BaseSubsystem {

    protected final Map<ResourceLocation, IModel> modelCache = new HashMap<>();

    public final String modID;
    public ArchitectConfiguration config;
    public final String modPackage;

    public final String assetKey;
    public final String resourceDir; // path to resources directory with leading and trailing slashes
    public final URL resourceURL; // URL to the resources directory
    public CLIENT client;
    public boolean clientSide;
    public File cfgFile;

    public String resourcePath(String fileName) {
        return resourceDir + fileName;
    }

    public BaseMod() {
        Class<? extends BaseMod> modClass = getClass();
        modPackage = modClass.getPackage().getName();
        // assetKey = modPackage.replace(".", "_");
        modID = getModID(modClass);
        assetKey = modID.toLowerCase();
        String resourceRelDir = "assets/" + assetKey + "/";
        resourceDir = "/" + resourceRelDir;
        resourceURL = getClass().getClassLoader().getResource(resourceRelDir);
        // creativeTab = CreativeTabs.tabMisc;
    }

    static String getModID(Class cls) {
        Annotation ann = cls.getAnnotation(Mod.class);
        if (ann instanceof Mod) return ((Mod) ann).modid();
        else {
            System.out.print("BaseMod: Mod annotation not found\n");
            return "<unknown>";
        }
    }

    public void preInit(FMLPreInitializationEvent e) {
        clientSide = e.getSide().isClient();
        if (clientSide) {
            client = initClient();
        }
        cfgFile = e.getSuggestedConfigurationFile();
        loadConfig();
        configure();
        if (client != null) client.preInit(e);
    }

    public void init(FMLInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
        if (client != null) client.init(e);
    }

    public void postInit(FMLPostInitializationEvent e) {
        if (client != null) client.postInit(e);
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new ArchitectureGuiHandler());
        saveConfig();
    }

    void loadConfig() {
        config = new ArchitectConfiguration(cfgFile);
    }

    void saveConfig() {
        if (config.extended) config.save();
    }

    String qualifiedName(String name) {
        return modPackage + "." + name;
    }

    // -------------------- Configuration ---------------------------------------------------------

    void configure() {}

    // ----------------- Client Proxy -------------------------------------------------------------

    public CLIENT initClient() {
        return (CLIENT) (new BaseModClient(this));
    }

    // --------------- Resources ----------------------------------------------------------

    public ResourceLocation resourceLocation(String path) {
        if (path.contains(":")) return new ResourceLocation(path);
        else return new ResourceLocation(assetKey, path);
    }

    public ResourceLocation modelLocation(String path) {
        return resourceLocation("models/" + path);
    }

    public IModel getModel(String name) {
        ResourceLocation loc = modelLocation(name);
        IModel model = modelCache.get(loc);
        if (model == null) {
            model = BaseModel.fromResource(loc);
            modelCache.put(loc, model);
        }
        return model;
    }

    // ------------------------- Network --------------------------------------------------

    public static void sendTileEntityUpdate(TileEntity te) {
        Packet packet = te.getDescriptionPacket();
        if (packet != null) {
            int x = te.xCoord >> 4;
            int z = te.zCoord >> 4;
            // System.out.printf("BaseMod.sendTileEntityUpdate: for chunk coords (%s, %s)\n", x, z);
            WorldServer world = (WorldServer) te.getWorldObj();
            ServerConfigurationManager cm = FMLCommonHandler.instance().getMinecraftServerInstance()
                    .getConfigurationManager();
            PlayerManager pm = world.getPlayerManager();
            for (EntityPlayerMP player : cm.playerEntityList) if (pm.isPlayerWatchingChunk(player, x, z)) {
                // System.out.printf("BaseMod.sendTileEntityUpdate: to %s\n", player);
                player.playerNetServerHandler.sendPacket(packet);
            }
        }
    }

}
