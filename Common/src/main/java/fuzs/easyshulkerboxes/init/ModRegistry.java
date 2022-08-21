package fuzs.easyshulkerboxes.init;

import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.capability.EnderChestMenuCapability;
import fuzs.easyshulkerboxes.capability.EnderChestMenuCapabilityImpl;
import fuzs.puzzleslib.capability.CapabilityController;
import fuzs.puzzleslib.capability.data.CapabilityKey;
import fuzs.puzzleslib.capability.data.PlayerRespawnStrategy;
import fuzs.puzzleslib.core.CoreServices;

public class ModRegistry {
    private static final CapabilityController CAPABILITIES = CoreServices.FACTORIES.capabilities(EasyShulkerBoxes.MOD_ID);
    public static final CapabilityKey<EnderChestMenuCapability> ENDER_CHEST_MENU_CAPABILITY = CAPABILITIES.registerPlayerCapability("ender_chest_menu", EnderChestMenuCapability.class, player -> new EnderChestMenuCapabilityImpl(), PlayerRespawnStrategy.NEVER);

    public static void touch() {

    }
}
