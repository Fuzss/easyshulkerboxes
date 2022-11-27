package fuzs.easyshulkerboxes.init;

import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.capability.ContainerSlotCapability;
import fuzs.easyshulkerboxes.capability.ContainerSlotCapabilityImpl;
import fuzs.easyshulkerboxes.capability.EnderChestMenuCapability;
import fuzs.easyshulkerboxes.capability.EnderChestMenuCapabilityImpl;
import fuzs.puzzleslib.capability.CapabilityController;
import fuzs.puzzleslib.capability.data.CapabilityKey;
import fuzs.puzzleslib.capability.data.PlayerRespawnStrategy;
import fuzs.puzzleslib.core.CommonFactories;

public class ModRegistry {
    private static final CapabilityController CAPABILITIES = CommonFactories.INSTANCE.capabilities(EasyShulkerBoxes.MOD_ID);
    public static final CapabilityKey<ContainerSlotCapability> CONTAINER_SLOT_CAPABILITY = CAPABILITIES.registerPlayerCapability("container_slot", ContainerSlotCapability.class, player -> new ContainerSlotCapabilityImpl(), PlayerRespawnStrategy.ALWAYS_COPY);
    public static final CapabilityKey<EnderChestMenuCapability> ENDER_CHEST_MENU_CAPABILITY = CAPABILITIES.registerPlayerCapability("ender_chest_menu", EnderChestMenuCapability.class, player -> new EnderChestMenuCapabilityImpl(), PlayerRespawnStrategy.NEVER);

    public static void touch() {

    }
}
