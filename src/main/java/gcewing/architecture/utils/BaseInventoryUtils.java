// ------------------------------------------------------------------------------------------------
//
// Greg's Mod Base for 1.8 - Inventory Utilities
//
// ------------------------------------------------------------------------------------------------

package gcewing.architecture.utils;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;

public class BaseInventoryUtils {

    public static InventorySide inventorySide(IInventory base, int side) {
        if (base instanceof ISidedInventory) return new SidedInventorySide((ISidedInventory) base, side);
        else return new UnsidedInventorySide(base);
    }

    public static void clearInventory(IInventory inv) {
        int n = inv.getSizeInventory();
        for (int i = 0; i < n; i++) inv.setInventorySlotContents(i, null);
    }

}
