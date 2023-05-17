package fuzs.easyshulkerboxes.impl.capability;

import net.minecraft.nbt.CompoundTag;

public class ContainerClientInputCapabilityImpl implements ContainerClientInputCapability {
    private int currentSlot = -1;
    private boolean modifierActive;

    @Override
    public int getCurrentSlot() {
        return this.currentSlot;
    }

    @Override
    public void setCurrentSlot(int currentSlot) {
        this.currentSlot = currentSlot;
    }

    @Override
    public boolean extractSingleItemOnly() {
        return this.modifierActive;
    }

    @Override
    public void extractSingleItem(boolean singleItemOnly) {
        this.modifierActive = singleItemOnly;
    }

    @Override
    public void write(CompoundTag tag) {

    }

    @Override
    public void read(CompoundTag tag) {

    }
}
