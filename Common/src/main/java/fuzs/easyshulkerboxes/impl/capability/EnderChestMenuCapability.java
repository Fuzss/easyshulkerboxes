package fuzs.easyshulkerboxes.impl.capability;

import fuzs.puzzleslib.capability.data.CapabilityComponent;
import net.minecraft.world.inventory.AbstractContainerMenu;

public interface EnderChestMenuCapability extends CapabilityComponent {

    void setEnderChestMenu(AbstractContainerMenu menu);

    AbstractContainerMenu getEnderChestMenu();
}
