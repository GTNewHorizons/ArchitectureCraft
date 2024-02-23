package gcewing.architecture.gui;

import gcewing.architecture.tile.SawbenchTE;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

class SlotSawbench extends Slot {

    final SawbenchTE te;
    final int index;

    public SlotSawbench(SawbenchTE te, int index, int x, int y) {
        super(te, index, x, y);
        this.te = te;
        this.index = index;
    }

    void updateFromServer(ItemStack stack) {
        te.inventory.setInventorySlotContents(index, stack);
    }

}
