package gcewing.architecture;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.IGuiHandler;
import gcewing.architecture.client.gui.ContainerSawbench;
import gcewing.architecture.client.gui.GuiSawbench;
import gcewing.architecture.common.tile.TileSawbench;

public class ArchitectureGuiHandler implements IGuiHandler {

    public final static int guiSawbench = 1;

    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (id == guiSawbench) {
            TileEntity te = world.getTileEntity(x, y, z);
            if (te instanceof TileSawbench tileSawbench) {
                return new ContainerSawbench(player, tileSawbench);
            }
        }
        return null;
    }

    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (id == guiSawbench) {
            TileEntity te = world.getTileEntity(x, y, z);
            if (te instanceof TileSawbench tileSawbench) {
                return new GuiSawbench(player, tileSawbench);
            }
        }
        return null;
    }
}
