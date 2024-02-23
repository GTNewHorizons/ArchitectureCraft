package gcewing.architecture.gui;

import net.minecraft.item.ItemStack;

import gcewing.architecture.tile.SawbenchTE;

class SlotSawbenchResult extends SlotSawbench {

    public SlotSawbenchResult(SawbenchTE te, int index, int x, int y) {
        super(te, index, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack newstack) {
        return false;
    }

}
