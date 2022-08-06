package fuzs.easyshulkerboxes.init;

import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.capability.EnderChestMenuCapability;
import fuzs.easyshulkerboxes.capability.EnderChestMenuCapabilityImpl;
import fuzs.puzzleslib.capability.FabricCapabilityController;
import fuzs.puzzleslib.capability.data.CapabilityKey;
import fuzs.puzzleslib.capability.data.PlayerRespawnStrategy;

public class FabricModRegistry {
    private static final FabricCapabilityController CAPABILITIES = FabricCapabilityController.of(EasyShulkerBoxes.MOD_ID);
    public static final CapabilityKey<EnderChestMenuCapability> ENDER_CHEST_MENU_CAPABILITY = CAPABILITIES.registerPlayerCapability("ender_chest_menu", EnderChestMenuCapability.class, player -> new EnderChestMenuCapabilityImpl(), PlayerRespawnStrategy.NEVER);

    public static void touch() {

    }
}
