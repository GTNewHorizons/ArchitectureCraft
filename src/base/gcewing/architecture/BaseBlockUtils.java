//------------------------------------------------------------------------------------------------
//
//   Greg's Mod Base for 1.8 - Block Utilities
//
//------------------------------------------------------------------------------------------------

package gcewing.architecture;

import java.io.*;

import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.block.state.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraftforge.common.util.*;

import static gcewing.architecture.BaseUtils.*;

public class BaseBlockUtils {

    public static String getNameForBlock(Block block) {
        return Block.blockRegistry.getNameForObject(block).toString();
    }
    
    /*
     *   Test whether a block is receiving a redstone signal from a source
     *   other than itself. For blocks that can both send and receive in
     *   any direction.
     */
    public static boolean blockIsGettingExternallyPowered(World world, BlockPos pos) {
        for (EnumFacing side : facings) {
            if (isPoweringSide(world, pos.offset(side), side))
                    return true;
        }
        return false;
    }
    
    static boolean isPoweringSide(World world, BlockPos pos, EnumFacing side) {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (block.getWeakPower(world, pos, state, side) > 0)
            return true;
        if (block.shouldCheckWeakPower(world, pos, side)) {
            for (EnumFacing side2 : facings)
                if (side2 != side.getOpposite())
                    if (world.getStrongPower(pos.offset(side2), side2) > 0)
                        return true;
        }
        return false;
    }
    
    public static IBlockState getBlockStateFromItemStack(ItemStack stack) {
        Block block = Block.getBlockFromItem(stack.getItem());
        int meta = 0;
        if (stack.getItem().getHasSubtypes())
            meta = stack.getItemDamage() & 0xf;
        return block.getStateFromMeta(meta);
    }

    // -------------------- 1.7/1.8 Compatibility --------------------

    public static IBlockState getBlockStateFromMeta(Block block, int meta) {
        return block.getStateFromMeta(meta);
    }
    
    public static int getMetaFromBlockState(IBlockState state) {
        return state.getBlock().getMetaFromState(state);
    }
    
    public static Block getWorldBlock(IBlockAccess world, BlockPos pos) {
        return world.getBlockState(pos).getBlock();
    }
    
    public static IBlockState getWorldBlockState(IBlockAccess world, BlockPos pos) {
        return world.getBlockState(pos);
    }
    
    public static void setWorldBlockState(World world, BlockPos pos, IBlockState state) {
        world.setBlockState(pos, state, 3);
    }
    
    public static void markWorldBlockForUpdate(World world, BlockPos pos) {
        world.markBlockForUpdate(pos);
    }   
    
    public static void notifyWorldNeighborsOfStateChange(World world, BlockPos pos, Block block) {
        world.notifyNeighborsOfStateChange(pos, block);
    }   
    
    public static TileEntity getWorldTileEntity(IBlockAccess world, BlockPos pos) {
        return world.getTileEntity(pos);
    }
    
    public static World getTileEntityWorld(TileEntity te) {
        return te.getWorld();
    }
    
    public static BlockPos getTileEntityPos(TileEntity te) {
        return te.getPos();
    }
    
    public static boolean blockCanRenderInLayer(Block block, EnumWorldBlockLayer layer) {
        return block.canRenderInLayer(layer);
    }
    
    public static ItemStack blockStackWithState(IBlockState state, int size) {
        Block block = state.getBlock();
        int meta = block.getMetaFromState(state);
        return new ItemStack(block, size, meta);
    }
    
    public static BlockPos readBlockPos(DataInput data) {
        try {
            int x = data.readInt();
            int y = data.readInt();
            int z = data.readInt();
            return new BlockPos(x, y, z);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void writeBlockPos(DataOutput data, BlockPos pos) {
        try {
            data.writeInt(pos.getX());
            data.writeInt(pos.getY());
            data.writeInt(pos.getZ());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
//     public static void markBlockForUpdate(World world, BlockPos pos) {
//         world.markBlockRangeForRenderUpdate(pos, pos);
//         if (!world.isRemote) {
//             IBlockState state = world.getBlockState(pos);
//             world.notifyBlockUpdate(pos, state, state, 3);
//         }
//     }

}
