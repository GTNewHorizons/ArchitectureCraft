package gcewing.architecture.compat.integration;

import net.minecraft.world.World;

import com.gtnewhorizons.liquidlogged.api.LiquidLoggedAPI;

import cpw.mods.fml.common.Loader;
import gcewing.architecture.common.tile.TileShape;

public final class LiquidLoggedIntegration {

    private LiquidLoggedIntegration() {}

    public static void revalidate(TileShape tile) {
        World world = tile.getWorldObj();
        if (Loader.isModLoaded("liquidlogged") && world != null && !world.isRemote) {
            LiquidLoggedAPI.revalidate(world, tile.xCoord, tile.yCoord, tile.zCoord);
        }
    }
}
