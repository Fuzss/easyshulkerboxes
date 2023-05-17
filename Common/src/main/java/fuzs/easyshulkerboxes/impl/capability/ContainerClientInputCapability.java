package fuzs.easyshulkerboxes.impl.capability;

import fuzs.puzzleslib.capability.data.CapabilityComponent;

public interface ContainerClientInputCapability extends CapabilityComponent {

    int getCurrentSlot();

    void setCurrentSlot(int currentSlot);

    boolean extractSingleItemOnly();

    void extractSingleItem(boolean singleItemOnly);
}
