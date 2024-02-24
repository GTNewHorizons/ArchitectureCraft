package gcewing.architecture.client.gui;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import gcewing.architecture.common.tile.TileSawbench;

class SlotSawbench extends Slot {

    final TileSawbench te;
    final int index;

    public SlotSawbench(TileSawbench te, int index, int x, int y) {
        super(te, index, x, y);
        this.te = te;
        this.index = index;
    }

    void updateFromServer(ItemStack stack) {
        te.inventory.setInventorySlotContents(index, stack);
    }

}
