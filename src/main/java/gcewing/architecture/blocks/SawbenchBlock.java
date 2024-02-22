// ------------------------------------------------------------------------------
//
// ArchitectureCraft - Sawbench Block
//
// ------------------------------------------------------------------------------

package gcewing.architecture.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import gcewing.architecture.ArchitectureCraft;
import gcewing.architecture.compat.BlockPos;
import gcewing.architecture.interfaces.IBlockState;
import gcewing.architecture.rendering.ModelSpec;
import gcewing.architecture.tile.SawbenchTE;
import gcewing.architecture.utils.BaseOrientation;

public class SawbenchBlock extends BaseBlock<SawbenchTE> {

    static final String model = "block/sawbench.smeg";
    static final String[] textures = { "sawbench-wood", "sawbench-metal" };
    static final ModelSpec modelSpec = new ModelSpec(model, textures);

    public SawbenchBlock() {
        super(Material.wood, SawbenchTE.class);
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

    // @Override
    // public String getModelNameForState(IBlockState state) {
    // return model;
    // }

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
                ArchitectureCraft.mod.openGuiSawbench(world, pos, player);
            }
            return true;
        } else return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new SawbenchTE();
    }

    // @Override
    // public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int data) {
    // Utils.dumpInventoryIntoWorld(world, x, y, z);
    // super.onBlockDestroyedByPlayer(world, x, y, z, data);
    // }

}
