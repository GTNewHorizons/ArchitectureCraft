// ------------------------------------------------------------------------------
//
// ArchitectureCraft - Sawbench Block
//
// ------------------------------------------------------------------------------

package gcewing.architecture.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import gcewing.architecture.ArchitectureCraft;
import gcewing.architecture.ArchitectureGuiHandler;
import gcewing.architecture.common.tile.TileSawbench;
import gcewing.architecture.compat.BlockPos;
import gcewing.architecture.legacy.blocks.blocks.IBlockState;
import gcewing.architecture.legacy.blocks.blocks.IOrientationHandler;
import gcewing.architecture.legacy.rendering.ModelSpec;
import gcewing.architecture.legacy.utils.BaseOrientation;

public class BlockSawbench extends BlockArchitecture<TileSawbench> {

    static final String model = "block/sawbench.smeg";
    static final String[] textures = { "sawbench-wood", "sawbench-metal" };
    static final ModelSpec modelSpec = new ModelSpec(model, textures);

    public BlockSawbench() {
        super(Material.wood, TileSawbench.class);
        // renderID = -1;
    }

    @Override
    public IOrientationHandler getOrientationHandler() {
        return BaseOrientation.orient4WaysByState;
    }

    @Override
    public String[] getTextureNames() {
        return textures;
    }

    @Override
    public ModelSpec getModelSpec(IBlockState state) {
        return modelSpec;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side,
            float hitX, float hitY, float hitZ) {
        // System.out.printf("SawbenchBlock.onBlockActivated\n");
        if (!player.isSneaking()) {
            if (!world.isRemote) {
                // System.out.printf("SawbenchBlock.onBlockActivated: opening gui\n");
                int x = pos.getX(), y = pos.getY(), z = pos.getZ();
                player.openGui(ArchitectureCraft.mod, ArchitectureGuiHandler.guiSawbench, world, x, y, z);
            }
            return true;
        } else return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileSawbench();
    }

}
