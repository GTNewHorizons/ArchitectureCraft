package gcewing.architecture.client.gui;

import gcewing.architecture.common.tile.ContainerArchictecture;

public class SlotRange {

    private final ContainerArchictecture baseContainer;
    public final int firstSlot;
    public int numSlots;
    public boolean reverseMerge;

    public SlotRange(ContainerArchictecture baseContainer) {
        this.baseContainer = baseContainer;
        firstSlot = baseContainer.inventorySlots.size();
    }

    public void end() {
        numSlots = baseContainer.inventorySlots.size() - firstSlot;
    }

    public boolean contains(int slot) {
        return slot >= firstSlot && slot < firstSlot + numSlots;
    }

    @Override
    public String toString() {
        return String.format("SlotRange(%s to %s)", firstSlot, firstSlot + numSlots - 1);
    }
}
