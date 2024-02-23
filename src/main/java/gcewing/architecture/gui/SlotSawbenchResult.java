package gcewing.architecture.gui;

import gcewing.architecture.tile.SawbenchTE;
import net.minecraft.item.ItemStack;

class SlotSawbenchResult extends SlotSawbench {

    public SlotSawbenchResult(SawbenchTE te, int index, int x, int y) {
        super(te, index, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack newstack) {
        return false;
    }

}
