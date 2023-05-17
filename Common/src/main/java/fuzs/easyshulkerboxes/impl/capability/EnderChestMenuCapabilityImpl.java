package fuzs.easyshulkerboxes.impl.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class EnderChestMenuCapabilityImpl implements EnderChestMenuCapability {
    private AbstractContainerMenu menu;

    @Override
    public void setEnderChestMenu(AbstractContainerMenu menu) {
        this.menu = menu;
    }

    @Override
    public AbstractContainerMenu getEnderChestMenu() {
        return this.menu;
    }

    @Override
    public void write(CompoundTag tag) {

    }

    @Override
    public void read(CompoundTag tag) {

    }
}
