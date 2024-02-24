package gcewing.architecture.common.tile;

import net.minecraft.inventory.IInventory;

public interface IRestrictedDroppingInventory extends IInventory {

    /**
     * Returns a list of inventory slots that are allowed to be dropped on block break
     */
    int[] getDroppingSlots();
}
