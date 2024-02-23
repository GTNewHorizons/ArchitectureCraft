// ------------------------------------------------------------------------------
//
// ArchitectureCraft - SawbenchContainer
//
// ------------------------------------------------------------------------------

package gcewing.architecture.gui;

import static gcewing.architecture.blocks.BaseBlockUtils.getWorldTileEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import gcewing.architecture.compat.BlockPos;
import gcewing.architecture.network.ChannelInput;
import gcewing.architecture.network.ServerMessageHandler;
import gcewing.architecture.tile.SawbenchTE;

public class SawbenchContainer extends BaseContainer {

    public static final int guWidth = 242;
    public static final int guiHeight = 224;
    public static final int inputSlotLeft = 12;
    public static final int inputSlotTop = 19;
    public static final int outputSlotLeft = 12;
    public static final int outputSlotTop = 57;

    final SawbenchTE te;
    final SlotRange sawbenchSlotRange;
    final Slot materialSlot;
    final Slot resultSlot;

    public static Container create(EntityPlayer player, World world, BlockPos pos) {
        TileEntity te = getWorldTileEntity(world, pos);
        if (te instanceof SawbenchTE) return new SawbenchContainer(player, (SawbenchTE) te);
        else return null;
    }

    public SawbenchContainer(EntityPlayer player, SawbenchTE te) {
        super(guWidth, guiHeight);
        this.te = te;
        sawbenchSlotRange = new SlotRange(this);
        materialSlot = addSlotToContainer(new Slot(te, 0, inputSlotLeft, inputSlotTop));
        sawbenchSlotRange.end();
        resultSlot = addSlotToContainer(new SlotSawbenchResult(te, 1, outputSlotLeft, outputSlotTop));
        addPlayerSlots(player, 8, guiHeight - 81);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return this.te.isUseableByPlayer(player);
    }

    @Override
    protected SlotRange transferSlotRange(int srcSlotIndex, ItemStack stack) {
        if (playerSlotRange.contains(srcSlotIndex)) return sawbenchSlotRange;
        else return playerSlotRange;
    }

    //
    // Server
    //

    @Override
    public void detectAndSendChanges() {
        for (int i = 0; i < this.inventorySlots.size(); ++i) {
            ItemStack newstack = this.inventorySlots.get(i).getStack();
            ItemStack oldstack = this.inventoryItemStacks.get(i);
            if (!ItemStack.areItemStacksEqual(oldstack, newstack)) {
                oldstack = newstack == null ? null : newstack.copy();
                this.inventoryItemStacks.set(i, oldstack);
                for (ICrafting crafter : crafters) {
                    if (crafter instanceof EntityPlayerMP) {
                        ((EntityPlayerMP) crafter).playerNetServerHandler
                                .sendPacket(new S2FPacketSetSlot(windowId, i, newstack));
                    } else crafter.sendSlotContents(this, i, newstack);
                }
            }
        }
        te.updateResultSlot();// update the result slot to make shift clicking new material in work smoothly
    }

    @ServerMessageHandler("SelectShape")
    public void onSelectShape(EntityPlayer player, ChannelInput data) {
        int page = data.readInt();
        int slot = data.readInt();
        te.setSelectedShape(page, slot);
    }

    //
    // Client
    //

    @Override
    public void putStackInSlot(int i, ItemStack stack) {
        // Slot update packet has arrived from server. Do not trigger crafting behaviour.
        Slot slot = getSlot(i);
        if (slot instanceof SlotSawbench) {
            // System.out.printf("SawbenchContainer.putStackInSlot: %d %s on %s\n", i, stack, te.worldObj);
            ((SlotSawbench) slot).updateFromServer(stack);
        } else super.putStackInSlot(i, stack);
    }

    // Default transferStackInSlot does not invoke decrStackSize, so we need this
    // to get pending material used.
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        Slot slot = getSlot(index);
        if (slot == resultSlot) return transferStackInResultSlot(player, index);
        else return super.transferStackInSlot(player, index);
    }

    protected ItemStack transferStackInResultSlot(EntityPlayer player, int index) {
        boolean materialWasPending = te.pendingMaterialUsage;
        ItemStack origMaterialStack = te.usePendingMaterial();
        ItemStack result = super.transferStackInSlot(player, index);
        if (materialWasPending) te.returnUnusedMaterial(origMaterialStack);
        return result;
    }

}
