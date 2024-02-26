// ------------------------------------------------------------------------------------------------
//
// Greg's Mod Base for 1.7 Version B - Generic Tile Entity
//
// ------------------------------------------------------------------------------------------------

package gcewing.architecture.common.tile;

import static gcewing.architecture.compat.BlockCompatUtils.getWorldBlockState;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.management.PlayerManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

import gcewing.architecture.common.block.IBlockArchitecture;
import gcewing.architecture.compat.BlockPos;
import gcewing.architecture.compat.IBlockState;
import gcewing.architecture.compat.Trans3;
import gcewing.architecture.compat.Vector3;

public class TileArchitecture extends TileEntity {

    public byte side, turn;
    public Ticket chunkTicket;

    public BlockPos getPos() {
        return new BlockPos(xCoord, yCoord, zCoord);
    }

    public int getX() {
        return xCoord;
    }

    public int getY() {
        return yCoord;
    }

    public int getZ() {
        return zCoord;
    }

    public void setSide(int side) {
        this.side = (byte) side;
    }

    public void setTurn(int turn) {
        this.turn = (byte) turn;
    }

    public Trans3 localToGlobalRotation() {
        return localToGlobalTransformation(Vector3.zero);
    }

    public Trans3 localToGlobalTransformation() {
        return localToGlobalTransformation(Vector3.blockCenter(xCoord, yCoord, zCoord));
    }

    public Trans3 localToGlobalTransformation(Vector3 origin, IBlockState state) {
        BlockPos pos = getPos();
        Block block = state.getBlock();
        return ((IBlockArchitecture) block).localToGlobalTransformation(worldObj, pos, state, this, origin);
    }

    public Trans3 localToGlobalTransformation(Vector3 origin) {
        BlockPos pos = getPos();
        IBlockState state = getWorldBlockState(worldObj, pos);
        Block block = state.getBlock();
        if (block instanceof IBlockArchitecture)
            return ((IBlockArchitecture) block).localToGlobalTransformation(worldObj, pos, state, this, origin);
        else {
            return new Trans3(origin);
        }
    }

    @Override
    public Packet getDescriptionPacket() {
        if (syncWithClient()) {
            NBTTagCompound nbt = new NBTTagCompound();
            writeToNBT(nbt);
            return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, nbt);
        } else return null;
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readFromNBT(pkt.func_148857_g());
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    boolean syncWithClient() {
        return true;
    }

    public void markBlockForUpdate() {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public void markForClientUpdate() {
        PlayerManager pm = ((WorldServer) worldObj).getPlayerManager();
        PlayerManager.PlayerInstance watcher = pm.getOrCreateChunkWatcher(xCoord >> 4, zCoord >> 4, false);
        if (watcher != null) {
            int oldFlags = watcher.flagsYAreasToUpdate;
            markBlockForUpdate();
            watcher.flagsYAreasToUpdate = oldFlags;
        } else markBlockForUpdate();
    }

    public void markForUpdate() {
        markDirty();
        markForClientUpdate();
    }

    public void onAddedToWorld() {}

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        side = nbt.getByte("side");
        turn = nbt.getByte("turn");
        readContentsFromNBT(nbt);
    }

    public void readFromItemStack(ItemStack stack) {
        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt != null) readFromItemStackNBT(nbt);
    }

    public void readFromItemStackNBT(NBTTagCompound nbt) {
        readContentsFromNBT(nbt);
    }

    public void readContentsFromNBT(NBTTagCompound nbt) {}

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        if (side != 0) nbt.setByte("side", side);
        if (turn != 0) nbt.setByte("turn", turn);
        writeContentsToNBT(nbt);
    }

    public void writeToItemStackNBT(NBTTagCompound nbt) {
        writeContentsToNBT(nbt);
    }

    public void writeContentsToNBT(NBTTagCompound nbt) {}

    // Save to disk, update client and re-render block
    public void markChanged() {
        markDirty();
        markBlockForUpdate();
    }

    @Override
    public void invalidate() {
        releaseChunkTicket();
        super.invalidate();
    }

    public void releaseChunkTicket() {
        if (chunkTicket != null) {
            ForgeChunkManager.releaseTicket(chunkTicket);
            chunkTicket = null;
        }
    }

    public static ItemStack blockStackWithTileEntity(Block block, int size, TileArchitecture te) {
        return blockStackWithTileEntity(block, size, 0, te);
    }

    public static ItemStack blockStackWithTileEntity(Block block, int size, int meta, TileArchitecture te) {
        ItemStack stack = new ItemStack(block, size, meta);
        if (te != null) {
            NBTTagCompound tag = new NBTTagCompound();
            te.writeToItemStackNBT(tag);
            stack.setTagCompound(tag);
        }
        return stack;
    }

    public ItemStack newItemStack(int size) {
        return blockStackWithTileEntity(getBlockType(), size, this);
    }

    @Override
    public boolean canUpdate() {
        return this instanceof ITickable;
    }

    @Override
    public void updateEntity() {
        ((ITickable) this).update();
    }

}
