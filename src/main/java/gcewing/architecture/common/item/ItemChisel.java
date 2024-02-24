// ------------------------------------------------------------------------------
//
// ArchitectureCraft - Chisel
//
// ------------------------------------------------------------------------------

package gcewing.architecture.common.item;

import static gcewing.architecture.legacy.blocks.BaseBlockUtils.blockStackWithState;
import static gcewing.architecture.legacy.blocks.BaseBlockUtils.getDefaultBlockState;
import static gcewing.architecture.legacy.blocks.BaseBlockUtils.getWorldBlockState;
import static gcewing.architecture.legacy.blocks.BaseBlockUtils.getWorldTileEntity;
import static gcewing.architecture.legacy.blocks.BaseBlockUtils.playWorldAuxSFX;
import static gcewing.architecture.legacy.blocks.BaseBlockUtils.setWorldBlockState;
import static gcewing.architecture.legacy.blocks.BaseBlockUtils.spawnBlockStackAsEntity;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import gcewing.architecture.common.tile.TileShape;
import gcewing.architecture.compat.BlockPos;
import gcewing.architecture.legacy.blocks.IBlockState;

public class ItemChisel extends ItemArchitecture {

    public ItemChisel() {
        setMaxStackSize(1);
    }

    @Override
    public CreativeTabs getCreativeTab() {
        return CreativeTabs.tabTools;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side,
            float hitX, float hitY, float hitZ) {
        TileEntity te = getWorldTileEntity(world, pos);
        if (te instanceof TileShape) {
            if (!world.isRemote) {
                TileShape ste = (TileShape) te;
                ste.onChiselUse(player, side, hitX, hitY, hitZ);
            }
            return true;
        }
        IBlockState state = getWorldBlockState(world, pos);
        Block block = state.getBlock();
        if (block == Blocks.glass || block == Blocks.glass_pane || block == Blocks.glowstone || block == Blocks.ice) {
            setWorldBlockState(world, pos, getDefaultBlockState(Blocks.air), 3);
            if (!world.isRemote) {
                dropBlockAsItem(world, pos, state);
                playWorldAuxSFX(world, 2001, pos, getDefaultBlockState(Blocks.stone)); // block breaking sound and
                                                                                       // particles
            }
            return true;
        }
        return false;
    }

    void dropBlockAsItem(World world, BlockPos pos, IBlockState state) {
        ItemStack stack = blockStackWithState(state, 1);
        spawnBlockStackAsEntity(world, pos, stack);
    }

}
