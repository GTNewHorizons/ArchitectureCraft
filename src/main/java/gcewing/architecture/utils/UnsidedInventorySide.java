package gcewing.architecture.utils;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class UnsidedInventorySide extends InventorySide {

    final IInventory base;

    public UnsidedInventorySide(IInventory base) {
        this.base = base;
        size = base.getSizeInventory();
    }

    public ItemStack get(int slot) {
        return base.getStackInSlot(slot);
    }

    public boolean set(int slot, ItemStack stack) {
        base.setInventorySlotContents(slot, stack);
        return true;
    }

    public ItemStack extract(int slot) {
        return get(slot);
    }

}
