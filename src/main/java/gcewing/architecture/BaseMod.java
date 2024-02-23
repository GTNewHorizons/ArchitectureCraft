// ------------------------------------------------------------------------------------------------
//
// Greg's Mod Base for 1.7 Version B - Generic Mod
//
// ------------------------------------------------------------------------------------------------

package gcewing.architecture;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.server.management.PlayerManager;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.ShapedOreRecipe;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import gcewing.architecture.BaseModClient.IModel;
import gcewing.architecture.blocks.BaseBlock;
import gcewing.architecture.blocks.IBlock;
import gcewing.architecture.compat.BlockPos;
import gcewing.architecture.config.BaseConfiguration;
import gcewing.architecture.interfaces.ISetMod;
import gcewing.architecture.rendering.BaseModel;

public class BaseMod<CLIENT extends BaseModClient<? extends BaseMod>> extends BaseSubsystem implements IGuiHandler {

    protected final Map<ResourceLocation, IModel> modelCache = new HashMap<>();

    public void setModOf(Object obj) {
        if (obj instanceof ISetMod) ((ISetMod) obj).setMod(this);
    }

    public final String modID;
    public BaseConfiguration config;
    public final String modPackage;
    public final String assetKey;
    public final String blockDomain;
    public final String itemDomain;
    public final String resourceDir; // path to resources directory with leading and trailing slashes
    public final URL resourceURL; // URL to the resources directory
    public CLIENT client;
    public IGuiHandler proxy;
    public boolean clientSide;
    public final CreativeTabs creativeTab;
    public File cfgFile;
    public final List<Block> registeredBlocks = new ArrayList<>();
    public final List<Item> registeredItems = new ArrayList<>();
    public final List<BaseSubsystem> subsystems = new ArrayList<>();

    public final boolean debugGui = false;
    public final boolean debugBlockRegistration = false;
    public final boolean debugCreativeTabs = false;

    public String resourcePath(String fileName) {
        return resourceDir + fileName;
    }

    public BaseMod() {
        Class<? extends BaseMod> modClass = getClass();
        modPackage = modClass.getPackage().getName();
        // assetKey = modPackage.replace(".", "_");
        modID = getModID(modClass);
        assetKey = modID.toLowerCase();
        blockDomain = assetKey;
        itemDomain = assetKey;
        String resourceRelDir = "assets/" + assetKey + "/";
        resourceDir = "/" + resourceRelDir;
        resourceURL = getClass().getClassLoader().getResource(resourceRelDir);
        subsystems.add(this);
        creativeTab = CreativeTabs.tabMisc;
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
            proxy = client;
        }
        cfgFile = e.getSuggestedConfigurationFile();
        loadConfig();
        configure();
        for (BaseSubsystem sub : subsystems) {
            if (sub != this) sub.preInit(e);
            sub.configure(config);
            sub.registerBlocks();
            sub.registerTileEntities();
            sub.registerItems();
            sub.registerOres();
            sub.registerWorldGenerators();
            sub.registerContainers();
            sub.registerEntities();
            sub.registerVillagers();
        }
        if (client != null) client.preInit(e);
    }

    public void init(FMLInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
        if (client != null) client.init(e);
        for (BaseSubsystem sub : subsystems) if (sub != this) sub.init(e);
    }

    public void postInit(FMLPostInitializationEvent e) {
        for (BaseSubsystem sub : subsystems) {
            if (sub != this) sub.postInit(e);
            sub.registerRecipes();
            sub.registerRandomItems();
            sub.registerOther();
        }
        if (client != null) client.postInit(e);
        if (proxy == null) proxy = this;
        NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
        saveConfig();
    }

    void loadConfig() {
        config = new BaseConfiguration(cfgFile);
    }

    void saveConfig() {
        if (config.extended) config.save();
    }

    String qualifiedName(String name) {
        return modPackage + "." + name;
    }

    // ----- BaseSubsystem -----

    @Override
    protected void registerScreens() {
        if (client != null) client.registerScreens();
    }

    @Override
    protected void registerBlockRenderers() {
        if (client != null) client.registerBlockRenderers();
    }

    @Override
    protected void registerItemRenderers() {
        if (client != null) client.registerItemRenderers();
    }

    @Override
    protected void registerEntityRenderers() {
        if (client != null) client.registerEntityRenderers();
    }

    @Override
    protected void registerTileEntityRenderers() {
        if (client != null) client.registerTileEntityRenderers();
    }

    @Override
    protected void registerOtherClient() {
        if (client != null) client.registerOther();
    }

    // -------------------- Configuration ---------------------------------------------------------

    void configure() {}

    // ----------------- Client Proxy -------------------------------------------------------------

    CLIENT initClient() {
        return (CLIENT) (new BaseModClient(this));
    }

    // --------------- Item registration ----------------------------------------------------------

    public Item newItem(String name) {
        return newItem(name, Item.class);
    }

    public <ITEM extends Item> ITEM newItem(String name, Class<ITEM> cls) {
        ITEM item;
        try {
            Constructor<ITEM> ctor = cls.getConstructor();
            item = ctor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return addItem(item, name);
    }

    public <ITEM extends Item> ITEM addItem(ITEM item, String name) {
        String qualName = itemDomain + ":" + name;
        item.setUnlocalizedName(qualName);
        item.setTextureName(assetKey + ":" + name);
        GameRegistry.registerItem(item, name);
        if (debugBlockRegistration) System.out.printf("BaseMod.addItem: Registered %s as %s\n", item, name);
        if (creativeTab != null) {
            if (debugCreativeTabs)
                System.out.printf("BaseMod.addItem: Setting creativeTab of %s to %s\n", name, creativeTab);
            item.setCreativeTab(creativeTab);
        }
        registeredItems.add(item);
        return item;
    }

    // --------------- Block registration ----------------------------------------------------------

    public <BLOCK extends Block> BLOCK newBlock(String name, Class<BLOCK> cls) {
        return newBlock(name, cls, null);
    }

    public <BLOCK extends Block> BLOCK newBlock(String name, Class<BLOCK> cls, Class itemClass) {
        BLOCK block;
        try {
            Constructor<BLOCK> ctor = cls.getConstructor();
            block = ctor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return addBlock(block, name, itemClass);
    }

    public <BLOCK extends Block> BLOCK addBlock(BLOCK block, String name, Class itemClass) {
        String qualName = blockDomain + ":" + name;
        block.setBlockName(qualName);
        block.setBlockTextureName(assetKey + ":" + name);
        itemClass = getItemClassForBlock(block, itemClass);
        GameRegistry.registerBlock(block, itemClass, name);
        if (creativeTab != null) {
            // System.out.printf("BaseMod.addBlock: Setting creativeTab to %s\n", creativeTab);
            block.setCreativeTab(creativeTab);
        }
        if (block instanceof BaseBlock) ((BaseBlock<?>) block).mod = this;
        registeredBlocks.add(block);
        return block;
    }

    protected Class defaultItemClassForBlock(Block block) {
        if (block instanceof IBlock) return ((IBlock) block).getDefaultItemClass();
        else return ItemBlock.class;
    }

    protected Class getItemClassForBlock(Block block, Class suppliedClass) {
        Class baseClass = defaultItemClassForBlock(block);
        if (suppliedClass == null) return baseClass;
        else {
            if (!baseClass.isAssignableFrom(suppliedClass)) throw new RuntimeException(
                    String.format(
                            "Block item class %s for %s does not extend %s\n",
                            suppliedClass.getName(),
                            block.getUnlocalizedName(),
                            baseClass.getName()));
            return suppliedClass;
        }
    }

    // --------------- Recipe construction ----------------------------------------------------------

    public void newRecipe(Item product, int qty, Object... params) {
        newRecipe(new ItemStack(product, qty), params);
    }

    public void newRecipe(Block product, int qty, Object... params) {
        newRecipe(new ItemStack(product, qty), params);
    }

    public void newRecipe(ItemStack product, Object... params) {
        GameRegistry.addRecipe(new ShapedOreRecipe(product, params));
    }

    // --------------- Resources ----------------------------------------------------------

    public ResourceLocation resourceLocation(String path) {
        if (path.contains(":")) return new ResourceLocation(path);
        else return new ResourceLocation(assetKey, path);
    }

    public ResourceLocation textureLocation(String path) {
        return resourceLocation("textures/" + path);
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

    // --------------- GUIs - Registration ------------------------------------------------

    protected void registerContainers() {
        // Make calls to addContainer() here.
        //
        // Container classes registered using these methods must implement either:
        //
        // (1) A static method create(EntityPlayer player, World world, int x, int y, int z [,int param])
        // (2) A constructor MyContainer(EntityPlayer player, World world, int x, int y, int z [, int param])
    }

    public void addContainer(int id, Class<? extends Container> cls) {
        containerClasses.put(id, cls);
    }

    // --------------- GUIs - Invoking -------------------------------------------------

    public void openGui(EntityPlayer player, Enum id, TileEntity te, int param) {
        openGui(player, id.ordinal(), te, param);
    }

    public void openGui(EntityPlayer player, int id, TileEntity te, int param) {
        openGui(player, id, te.getWorldObj(), new BlockPos(te), param);
    }

    public void openGui(EntityPlayer player, Enum id, World world, BlockPos pos, int param) {
        openGui(player, id.ordinal(), world, pos, param);
    }

    public void openGui(EntityPlayer player, int id, World world, BlockPos pos, int param) {
        openGui(player, id | (param << 16), world, pos);
    }

    public void openGui(EntityPlayer player, int id, World world, BlockPos pos) {
        int x = pos.getX(), y = pos.getY(), z = pos.getZ();
        if (debugGui)
            System.out.printf("BaseMod.openGui: for %s with id 0x%x in %s at (%s, %s, %s)\n", this, id, world, x, y, z);
        player.openGui(this, id, world, x, y, z);
    }

    // --------------- GUIs - Internal -------------------------------------------------

    final Map<Integer, Class<? extends Container>> containerClasses = new HashMap<>();

    /**
     * Returns a Container to be displayed to the user. On the client side, this needs to return a instance of GuiScreen
     * On the server side, this needs to return a instance of Container
     *
     * @param id     The Gui ID Number
     * @param player The player viewing the Gui
     * @param world  The current world
     * @param x      Position in world
     * @param y      Position in world
     * @param z      Position in world
     * @return A GuiScreen/Container to be displayed to the user, null if none.
     */

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return getServerGuiElement(id, player, world, new BlockPos(x, y, z));
    }

    public Object getServerGuiElement(int id, EntityPlayer player, World world, BlockPos pos) {
        if (debugGui) System.out.printf("BaseMod.getServerGuiElement: for id 0x%x\n", id);
        int param = id >> 16;
        id = id & 0xffff;
        Class<? extends Container> cls = containerClasses.get(id);
        Object result;
        if (cls != null) result = createGuiElement(cls, player, world, pos, param);
        else result = getGuiContainer(id, player, world, pos, param);
        if (debugGui) System.out.printf("BaseMod.getServerGuiElement: Returning %s\n", result);
        setModOf(result);
        return result;
    }

    Container getGuiContainer(int id, EntityPlayer player, World world, BlockPos pos, int param) {
        // Called when container id not found in registry
        if (debugGui)
            System.out.printf("%s: BaseMod.getGuiContainer: No Container class found for gui id %d\n", this, id);
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    Object createGuiElement(Class cls, EntityPlayer player, World world, BlockPos pos, int param) {
        try {
            if (debugGui) System.out.printf(
                    "BaseMod.createGuiElement: Looking for create method on %s for %s in %s\n",
                    cls,
                    player,
                    world);
            Method m = getMethod(cls, "create", EntityPlayer.class, World.class, BlockPos.class, int.class);
            if (m != null) return m.invoke(null, player, world, pos, param);
            m = getMethod(cls, "create", EntityPlayer.class, World.class, BlockPos.class);
            if (m != null) return m.invoke(null, player, world, pos);
            if (debugGui) System.out.printf("BaseMod.createGuiElement: Looking for constructor on %s\n", cls);
            Constructor c = getConstructor(cls, EntityPlayer.class, World.class, BlockPos.class, int.class);
            if (c != null) return c.newInstance(player, world, pos, param);
            c = getConstructor(cls, EntityPlayer.class, World.class, BlockPos.class);
            if (c != null) return c.newInstance(player, world, pos);
            throw new RuntimeException(
                    String.format("%s: No suitable gui element constructor found for %s\n", modID, cls));
        } catch (Exception e) {
            reportExceptionCause(e);
            return null;
        }
    }

    Method getMethod(Class cls, String name, Class... argTypes) {
        try {
            return cls.getMethod(name, argTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    Constructor getConstructor(Class cls, Class... argTypes) {
        try {
            return cls.getConstructor(argTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static void reportExceptionCause(Exception e) {
        Throwable cause = e.getCause();
        System.out.printf("BaseMod.createGuiElement: %s: %s\n", e, cause);
        if (cause != null) cause.printStackTrace();
        else e.printStackTrace();
    }
}
