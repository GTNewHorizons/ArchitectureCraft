package gcewing.architecture.client.gui;

import gcewing.architecture.legacy.gui.ArchictectureContainer;

public class SlotRange {

    private final ArchictectureContainer baseContainer;
    public final int firstSlot;
    public int numSlots;
    public boolean reverseMerge;

    public SlotRange(ArchictectureContainer baseContainer) {
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
