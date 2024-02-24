package gcewing.architecture.client.gui;

import net.minecraft.item.ItemStack;

import gcewing.architecture.common.tile.TileSawbench;

class SlotSawbenchResult extends SlotSawbench {

    public SlotSawbenchResult(TileSawbench te, int index, int x, int y) {
        super(te, index, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack newstack) {
        return false;
    }

}
