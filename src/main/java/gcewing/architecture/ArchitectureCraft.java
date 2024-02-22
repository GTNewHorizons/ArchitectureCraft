// ------------------------------------------------------
//
// ArchitectureCraft - Main
//
// ------------------------------------------------------

package gcewing.architecture;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import gcewing.architecture.blocks.BaseBlock;
import gcewing.architecture.blocks.SawbenchBlock;
import gcewing.architecture.compat.BlockPos;
import gcewing.architecture.gui.SawbenchContainer;
import gcewing.architecture.items.ChiselItem;
import gcewing.architecture.items.CladdingItem;
import gcewing.architecture.items.HammerItem;
import gcewing.architecture.network.DataChannel;
import gcewing.architecture.shapes.ShapeBlock;
import gcewing.architecture.shapes.ShapeItem;
import gcewing.architecture.shapes.ShapeTE;
import gcewing.architecture.tile.SawbenchTE;

@Mod(
        modid = Info.modID,
        name = Info.modName,
        version = Info.versionNumber,
        acceptedMinecraftVersions = Info.acceptedMinecraftVersions)

public class ArchitectureCraft extends BaseMod<ArchitectureCraftClient> {

    public static ArchitectureCraft mod;
    public static DataChannel channel;

    //
    // Blocks and Items
    //

    public static SawbenchBlock blockSawbench;
    public static BaseBlock blockShape;
    public static BaseBlock blockShapeSE;
    public static Item itemSawblade;
    public static Item itemLargePulley;
    public static Item itemChisel;
    public static Item itemHammer;
    public static CladdingItem itemCladding;

    public ArchitectureCraft() {
        super();
        mod = this;
        channel = new DataChannel(modID);
        // debugCreativeTabs = true;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        super.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);
    }

    @Override
    ArchitectureCraftClient initClient() {
        return new ArchitectureCraftClient(this);
    }

    protected void registerBlocks() {
        blockSawbench = newBlock("sawbench", SawbenchBlock.class);
        blockSawbench.setHardness(2.0F);
        blockShape = newBlock("shape", ShapeBlock.class, ShapeItem.class);
        blockShapeSE = newBlock("shapeSE", ShapeBlock.class, ShapeItem.class);
    }

    protected void registerTileEntities() {
        GameRegistry.registerTileEntity(SawbenchTE.class, "gcewing.sawbench");
        GameRegistry.registerTileEntity(ShapeTE.class, "gcewing.shape");
    }

    protected void registerItems() {
        itemSawblade = newItem("sawblade");
        itemLargePulley = newItem("largePulley");
        itemChisel = newItem("chisel", ChiselItem.class);
        itemHammer = newItem("hammer", HammerItem.class);
        itemCladding = newItem("cladding", CladdingItem.class);
    }

    protected void registerRecipes() {
        if (!Loader.isModLoaded("dreamcraft")) {
            ItemStack orangeDye = new ItemStack(Items.dye, 1, 14);
            newRecipe(
                    blockSawbench,
                    1,
                    "I*I",
                    "/0/",
                    "/_/",
                    'I',
                    Items.iron_ingot,
                    '*',
                    itemSawblade,
                    '/',
                    Items.stick,
                    '_',
                    Blocks.wooden_pressure_plate,
                    '0',
                    itemLargePulley);
            newRecipe(itemSawblade, 1, " I ", "I/I", " I ", 'I', Items.iron_ingot, '/', Items.stick);
            newRecipe(itemLargePulley, 1, " W ", "W/W", " W ", 'W', Blocks.planks, '/', Items.stick);
            newRecipe(itemChisel, 1, "I ", "ds", 'I', Items.iron_ingot, 's', Items.stick, 'd', orangeDye);
            newRecipe(itemHammer, 1, "II ", "dsI", "ds ", 'I', Items.iron_ingot, 's', Items.stick, 'd', orangeDye);
        }
    }

    // --------------- GUIs ----------------------------------------------------------

    public final static int guiSawbench = 1;

    @Override
    protected void registerContainers() {
        addContainer(guiSawbench, SawbenchContainer.class);
    }

    public void openGuiSawbench(World world, BlockPos pos, EntityPlayer player) {
        openGui(player, guiSawbench, world, pos);
    }

}
