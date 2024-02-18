// ------------------------------------------------------------------------------
//
// ArchitectureCraft - ShapeItem - Client
//
// ------------------------------------------------------------------------------

package gcewing.architecture;

import static gcewing.architecture.BaseBlockUtils.*;
import static gcewing.architecture.BaseUtils.*;
import static gcewing.architecture.Vector3.getDirectionVec;

import java.util.List;
import java.util.Random;

import net.minecraft.block.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

public class ShapeItem extends BaseItemBlock {

    static Random rand = new Random();

    public ShapeItem(Block block) {
        super(block);
    }

    /**
     * Converts the given ItemStack damage value into a metadata value to be placed in the world when this Item is
     * placed as a Block (mostly used with ItemBlocks).
     */
    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing face,
            float hitX, float hitY, float hitZ, IBlockState newState) {
        // if (!world.isRemote)
        // System.out.printf("ShapeItem.placeBlockAt: hit = (%.3f, %.3f, %.3f)\n", hitX, hitY, hitZ);
        if (!setWorldBlockState(world, pos, newState, 3)) return false;
        Vec3i d = getDirectionVec(face);
        Vector3 hit = new Vector3(hitX - d.getX() - 0.5, hitY - d.getY() - 0.5, hitZ - d.getZ() - 0.5);
        ShapeTE te = ShapeTE.get(world, pos);
        if (te != null) {
            te.readFromItemStack(stack);
            if (te.shape != null) {
                BlockPos npos = te.getPos().offset(oppositeFacing(face));
                IBlockState nstate = getWorldBlockState(world, npos);
                TileEntity nte = getWorldTileEntity(world, npos);
                te.shape.orientOnPlacement(player, te, npos, nstate, nte, face, hit);
            }
        }
        return true;
    }

    @Override
    public boolean getShareTag() {
        return true;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List lines, boolean par4) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null) {
            int id = tag.getInteger("Shape");
            Shape shape = Shape.forId(id);
            if (shape != null) lines.set(0, GuiText.valueOf(GuiText.class, shape.name()).getLocal());
            else lines.set(0, lines.get(0) + " (" + id + ")");
            Block baseBlock = Block.getBlockFromName(tag.getString("BaseName"));
            int baseMetadata = tag.getInteger("BaseData");
            if (baseBlock != null) lines.add(Utils.displayNameOfBlock(baseBlock, baseMetadata));
        }
    }

}
