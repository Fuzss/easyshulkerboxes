package fuzs.easyshulkerboxes.api.world.inventory;

import fuzs.easyshulkerboxes.api.capability.ContainerSlotCapability;
import fuzs.easyshulkerboxes.api.init.ModRegistry;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;

public class ContainerSlotHelper {

    public static int findClosestSlotWithContent(SimpleContainer container, int currentContainerSlot, boolean forwards) {
        int size = container.getContainerSize();
        if (currentContainerSlot == -1) currentContainerSlot = size - 1;
        for (int i = 1; i <= size; i++) {
            int currentIndex = ((currentContainerSlot + (forwards ? i : -i)) % size + size) % size;
            if (!container.getItem(currentIndex).isEmpty()) {
                return currentIndex;
            }
        }
        return -1;
    }

    public static void cycleCurrentSlotBackwards(Player player, SimpleContainer container) {
        int currentContainerSlot = getCurrentContainerSlot(player);
        currentContainerSlot = findClosestSlotWithContent(container, currentContainerSlot, false);
        setCurrentContainerSlot(player, currentContainerSlot);
    }

    public static void resetCurrentContainerSlot(Player player) {
        setCurrentContainerSlot(player, -1);
    }

    public static void setCurrentContainerSlot(Player player, int slot) {
        ModRegistry.CONTAINER_SLOT_CAPABILITY.maybeGet(player).ifPresent(capability -> {
            capability.setCurrentSlot(slot);
        });
    }

    public static int getCurrentContainerSlot(Player player) {
        return ModRegistry.CONTAINER_SLOT_CAPABILITY.maybeGet(player).map(ContainerSlotCapability::getCurrentSlot).orElse(-1);
    }
}
