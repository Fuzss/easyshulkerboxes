package fuzs.easyshulkerboxes.api.init;

import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.api.capability.ContainerSlotCapability;
import fuzs.easyshulkerboxes.api.capability.ContainerSlotCapabilityImpl;
import fuzs.puzzleslib.capability.CapabilityController;
import fuzs.puzzleslib.capability.data.CapabilityKey;
import fuzs.puzzleslib.capability.data.PlayerRespawnStrategy;
import fuzs.puzzleslib.core.CommonFactories;

public class ModRegistry {
    private static final CapabilityController CAPABILITIES = CommonFactories.INSTANCE.capabilities(EasyShulkerBoxes.MOD_ID);
    public static final CapabilityKey<ContainerSlotCapability> CONTAINER_SLOT_CAPABILITY = CAPABILITIES.registerPlayerCapability("container_slot", ContainerSlotCapability.class, player -> new ContainerSlotCapabilityImpl(), PlayerRespawnStrategy.ALWAYS_COPY);

    public static void touch() {

    }
}
