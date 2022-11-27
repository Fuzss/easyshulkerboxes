package fuzs.easyshulkerboxes.capability;

import net.minecraft.nbt.CompoundTag;

public class ContainerSlotCapabilityImpl implements ContainerSlotCapability {
    private int currentSlot = -1;

    @Override
    public int getCurrentSlot() {
        return this.currentSlot;
    }

    @Override
    public void setCurrentSlot(int currentSlot) {
        this.currentSlot = currentSlot;
    }

    @Override
    public void write(CompoundTag tag) {

    }

    @Override
    public void read(CompoundTag tag) {

    }
}
