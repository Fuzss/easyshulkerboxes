package fuzs.easyshulkerboxes.init;

import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.capability.EnderChestMenuCapability;
import fuzs.puzzleslib.capability.CapabilityController;
import fuzs.puzzleslib.capability.data.CapabilityKey;

public class ModRegistry {
    public static final CapabilityKey<EnderChestMenuCapability> ENDER_CHEST_MENU_CAPABILITY = CapabilityController.makeCapabilityKey(EasyShulkerBoxes.MOD_ID, "ender_chest_menu", EnderChestMenuCapability.class);

    public static void touch() {

    }
}
