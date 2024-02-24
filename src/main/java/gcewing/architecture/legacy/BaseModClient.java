// ------------------------------------------------------------------------------------------------
//
// Greg's Mod Base for 1.7 Version B - Generic Client Proxy
//
// ------------------------------------------------------------------------------------------------

package gcewing.architecture.legacy;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gcewing.architecture.ArchitectureCraft;
import gcewing.architecture.common.block.BlockArchitecture;
import gcewing.architecture.common.item.IHasModel;
import gcewing.architecture.compat.BlockPos;
import gcewing.architecture.compat.Trans3;
import gcewing.architecture.legacy.blocks.blocks.BaseBlockUtils;
import gcewing.architecture.legacy.blocks.blocks.EnumWorldBlockLayer;
import gcewing.architecture.legacy.blocks.blocks.IBlock;
import gcewing.architecture.legacy.blocks.blocks.IBlockState;
import gcewing.architecture.legacy.blocks.blocks.ITextureConsumer;
import gcewing.architecture.legacy.rendering.BaseGLRenderTarget;
import gcewing.architecture.legacy.rendering.BaseModelRenderer;
import gcewing.architecture.legacy.rendering.BaseTexture;
import gcewing.architecture.legacy.rendering.BlockRenderDispatcher;
import gcewing.architecture.legacy.rendering.ICustomRenderer;
import gcewing.architecture.legacy.rendering.IModel;
import gcewing.architecture.legacy.rendering.IRenderTarget;
import gcewing.architecture.legacy.rendering.ITexture;
import gcewing.architecture.legacy.rendering.ItemRenderDispatcher;
import gcewing.architecture.legacy.rendering.ModelSpec;

public class BaseModClient<MOD extends BaseMod<? extends BaseModClient>> {

    MOD base;

    public BaseModClient(MOD mod) {
        base = mod;
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
    }

    public void preInit(FMLPreInitializationEvent e) {
        registerBlockRenderers();
        registerItemRenderers();
        registerDefaultRenderers();
        removeUnusedDefaultTextureNames();
    }

    public void init(FMLInitializationEvent e) {}

    public void postInit(FMLPostInitializationEvent e) {
        registerTileEntityRenderers();
        registerEntityRenderers();
    }

    // -------------- Renderer registration --------------------------------------------------------

    protected void registerBlockRenderers() {}

    protected void registerItemRenderers() {}

    protected void registerEntityRenderers() {}

    protected void registerTileEntityRenderers() {}

    protected void registerDefaultRenderers() {
        for (Block block : ArchitectureCraft.content.registeredBlocks) {
            Item item = Item.getItemFromBlock(block);
            if (block instanceof IBlock) {
                if (!blockRenderers.containsKey(block)) {
                    String name = ((IBlock) block).getQualifiedRendererClassName();
                    if (name != null) {
                        try {
                            Class<?> cls = Class.forName(name);
                            addBlockRenderer((IBlock) block, (ICustomRenderer) cls.newInstance());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                if (blockNeedsCustomRendering(block)) {
                    installCustomBlockRenderDispatcher((IBlock) block);
                    installCustomItemRenderDispatcher(item);
                }
            }
            if (itemNeedsCustomRendering(item)) installCustomItemRenderDispatcher(item);
        }
        for (Item item : ArchitectureCraft.content.registeredItems) {
            if (itemNeedsCustomRendering(item)) installCustomItemRenderDispatcher(item);
        }
    }

    protected void installCustomBlockRenderDispatcher(IBlock block) {
        block.setRenderType(getCustomBlockRenderType());
    }

    protected void installCustomItemRenderDispatcher(Item item) {
        if (item != null) {
            MinecraftForgeClient.registerItemRenderer(item, getItemRenderDispatcher());
        }
    }

    protected void removeUnusedDefaultTextureNames() {
        for (Block block : ArchitectureCraft.content.registeredBlocks) {
            if (blockNeedsCustomRendering(block)) {
                block.setBlockTextureName("minecraft:stone");
            }
        }
        for (Item item : ArchitectureCraft.content.registeredItems) {
            if (itemNeedsCustomRendering(item)) {
                item.setTextureName("minecraft:apple");
            }
        }
    }
    // -------------- Rendering --------------------------------------------------------

    public ResourceLocation textureLocation(String path) {
        return base.resourceLocation("textures/" + path);
    }

    public static void bindTexture(ResourceLocation rsrc) {
        TextureManager tm = Minecraft.getMinecraft().getTextureManager();
        tm.bindTexture(rsrc);
    }

    // ======================================= Custom Rendering =======================================

    public static class TextureCache extends HashMap<ResourceLocation, ITexture> {
    }

    protected final Map<IBlock, ICustomRenderer> blockRenderers = new HashMap<>();
    public final Map<Item, ICustomRenderer> itemRenderers = new HashMap<>();
    protected final Map<IBlockState, ICustomRenderer> stateRendererCache = new HashMap<>();
    protected final TextureCache[] textureCaches = new TextureCache[2];
    {
        for (int i = 0; i < 2; i++) textureCaches[i] = new TextureCache();
    }

    // -------------- Renderer registration -------------------------------

    public void addBlockRenderer(IBlock block, ICustomRenderer renderer) {
        blockRenderers.put(block, renderer);
        // block.setRenderType(getCustomBlockRenderType());
        Item item = Item.getItemFromBlock((Block) block);
        if (item != null && !itemRenderers.containsKey(item)) addItemRenderer(item, renderer);
    }

    public void addItemRenderer(Item item, ICustomRenderer renderer) {
        itemRenderers.put(item, renderer);
        // MinecraftForgeClient.registerItemRenderer(item, getItemRenderDispatcher());
    }

    // --------------- Model Locations ----------------------------------------------------

    protected boolean blockNeedsCustomRendering(Block block) {
        return blockRenderers.containsKey(block) || specifiesTextures(block);
    }

    protected boolean itemNeedsCustomRendering(Item item) {
        return itemRenderers.containsKey(item) || specifiesTextures(item);
    }

    protected boolean specifiesTextures(Object obj) {
        return obj instanceof ITextureConsumer && ((ITextureConsumer) obj).getTextureNames() != null;
    }

    // ------------------------------------------------------------------------------------------------

    public static final EnumWorldBlockLayer[][] passLayers = {
            { EnumWorldBlockLayer.SOLID, EnumWorldBlockLayer.CUTOUT_MIPPED, EnumWorldBlockLayer.CUTOUT,
                    EnumWorldBlockLayer.TRANSLUCENT },
            { EnumWorldBlockLayer.SOLID, EnumWorldBlockLayer.CUTOUT_MIPPED, EnumWorldBlockLayer.CUTOUT },
            { EnumWorldBlockLayer.TRANSLUCENT } };

    protected BlockRenderDispatcher blockRenderDispatcher;

    protected int getCustomBlockRenderType() {
        return getBlockRenderDispatcher().renderID;
    }

    protected BlockRenderDispatcher getBlockRenderDispatcher() {
        if (blockRenderDispatcher == null) blockRenderDispatcher = new BlockRenderDispatcher(this);
        return blockRenderDispatcher;
    }

    // ------------------------------------------------------------------------------------------------

    protected ItemRenderDispatcher itemRenderDispatcher;

    protected ItemRenderDispatcher getItemRenderDispatcher() {
        if (itemRenderDispatcher == null) itemRenderDispatcher = new ItemRenderDispatcher(this);
        return itemRenderDispatcher;
    }

    public static final BaseGLRenderTarget glTarget = new BaseGLRenderTarget();

    public static final Trans3 entityTrans = Trans3.blockCenter;
    public static final Trans3 equippedTrans = Trans3.blockCenter;
    public static final Trans3 firstPersonTrans = Trans3.blockCenterSideTurn(0, 3);
    public static final Trans3 inventoryTrans = Trans3.blockCenter;

    // ------------------------------------------------------------------------------------------------

    public ICustomRenderer getCustomBlockRenderer(IBlockAccess world, BlockPos pos, IBlockState state) {
        BlockArchitecture block = (BlockArchitecture) state.getBlock();
        ICustomRenderer rend = blockRenderers.get(block);
        if (rend == null && block instanceof IBlock) {
            IBlockState astate = block.getActualState(state, world, pos);
            rend = getModelRendererForState(astate);
        }
        return rend;
    }

    protected ICustomRenderer getModelRendererForSpec(ModelSpec spec, int textureType) {
        IModel model = getModel(spec.modelName);
        ITexture[] textures = new ITexture[spec.textureNames.length];
        for (int i = 0; i < textures.length; i++) textures[i] = getTexture(textureType, spec.textureNames[i]);
        return new BaseModelRenderer(model, spec.origin, textures);
    }

    protected ICustomRenderer getModelRendererForState(IBlockState astate) {
        ICustomRenderer rend = stateRendererCache.get(astate);
        if (rend == null) {
            Block block = astate.getBlock();
            if (block instanceof IBlock) {
                ModelSpec spec = ((IBlock) block).getModelSpec(astate);
                if (spec != null) {
                    rend = getModelRendererForSpec(spec, 0);
                    stateRendererCache.put(astate, rend);
                }
            }
        }
        return rend;
    }

    public ICustomRenderer getModelRendererForItemStack(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof IHasModel) {
            ModelSpec spec = ((IHasModel) item).getModelSpec(stack);
            if (spec != null) return getModelRendererForSpec(spec, 1);
        }
        if (item instanceof ItemBlock) {
            Block block = ((ItemBlock) item).field_150939_a;
            if (block instanceof BlockArchitecture) {
                IBlockState state = BaseBlockUtils.getBlockStateFromItemStack(stack);
                ModelSpec spec = ((IBlock) block).getModelSpec(state);
                return getModelRendererForSpec(spec, 0);
            }
        }
        return null;
    }

    // Call this from renderBlock of an ICustomRenderer to fall back to model spec
    public void renderBlockUsingModelSpec(IBlockAccess world, BlockPos pos, IBlockState state, IRenderTarget target,
            EnumWorldBlockLayer layer, Trans3 t) {
        ICustomRenderer rend = getModelRendererForState(state);
        if (rend != null) rend.renderBlock(world, pos, state, target, layer, t);
    }

    // Call this from renderItemStack of an ICustomRenderer to fall back to model spec
    public void renderItemStackUsingModelSpec(ItemStack stack, IRenderTarget target, Trans3 t) {
        ICustomRenderer rend = getModelRendererForItemStack(stack);
        if (rend != null) rend.renderItemStack(stack, target, t);
    }

    public IModel getModel(String name) {
        return base.getModel(name);
    }

    public ITexture getTexture(int type, String name) {
        // Cache is keyed by texture name without "textures/"
        ResourceLocation loc = base.resourceLocation(name);
        return textureCaches[type].get(loc);
    }

    @SubscribeEvent
    public void onTextureStitchEventPre(TextureStitchEvent.Pre e) {
        int type = e.map.getTextureType();
        // System.out.printf("BaseModClient.onTextureStitchEventPre: %s [%s]\n", e.map, type);
        if (type >= 0 && type <= 1) {
            TextureCache cache = textureCaches[type];
            cache.clear();
            switch (type) {
                case 0:
                    for (Block block : ArchitectureCraft.content.registeredBlocks) registerSprites(e.map, cache, block);
                    break;
                case 1:
                    for (Item item : ArchitectureCraft.content.registeredItems) registerSprites(e.map, cache, item);
                    break;
            }
        }
    }

    protected void registerSprites(TextureMap reg, TextureCache cache, Object obj) {
        if (obj instanceof ITextureConsumer) {
            String[] names = ((ITextureConsumer) obj).getTextureNames();
            if (names != null) {
                for (String name : names) {
                    ResourceLocation loc = base.resourceLocation(name); // TextureMap adds "textures/"
                    if (cache.get(loc) == null) {
                        IIcon icon = reg.registerIcon(loc.toString());
                        ITexture texture = BaseTexture.fromSprite(icon);
                        cache.put(loc, texture);
                    }
                }
            }
        }
    }

}
