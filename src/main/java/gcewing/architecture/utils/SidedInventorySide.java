package gcewing.architecture.utils;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;

public class SidedInventorySide extends InventorySide {

    final ISidedInventory base;
    final int side;
    final int[] slots;

    public SidedInventorySide(ISidedInventory base, int side) {
        this.base = base;
        this.side = side;
        slots = base.getAccessibleSlotsFromSide(side);
        size = slots.length;
    }

    public ItemStack get(int i) {
        return base.getStackInSlot(slots[i]);
    }

    public boolean set(int i, ItemStack stack) {
        int slot = slots[i];
        if (base.canInsertItem(slot, stack, side)) {
            base.setInventorySlotContents(slot, stack);
            return true;
        } else return false;
    }

    public ItemStack extract(int i) {
        int slot = slots[i];
        ItemStack stack = base.getStackInSlot(slot);
        if (base.canExtractItem(slot, stack, side)) return stack;
        else return null;
    }

}
