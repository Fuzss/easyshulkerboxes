package fuzs.easyshulkerboxes.init;

import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.capability.EnderChestMenuCapability;
import fuzs.easyshulkerboxes.capability.EnderChestMenuCapabilityImpl;
import fuzs.puzzleslib.capability.ForgeCapabilityController;
import fuzs.puzzleslib.capability.data.CapabilityKey;
import fuzs.puzzleslib.capability.data.PlayerRespawnStrategy;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class ForgeModRegistry {
    private static final ForgeCapabilityController CAPABILITIES = ForgeCapabilityController.of(EasyShulkerBoxes.MOD_ID);
    public static final CapabilityKey<EnderChestMenuCapability> ENDER_CHEST_MENU_CAPABILITY = CAPABILITIES.registerPlayerCapability("ender_chest_menu", EnderChestMenuCapability.class, player -> new EnderChestMenuCapabilityImpl(), PlayerRespawnStrategy.NEVER, new CapabilityToken<EnderChestMenuCapability>() {});

    public static void touch() {

    }
}
