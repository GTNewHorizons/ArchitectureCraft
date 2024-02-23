package gcewing.architecture.utils;

import net.minecraft.item.ItemStack;

public abstract class InventorySide {

    public int size;

    public abstract ItemStack get(int slot);

    public abstract boolean set(int slot, ItemStack stack);

    public abstract ItemStack extract(int slot);
}
