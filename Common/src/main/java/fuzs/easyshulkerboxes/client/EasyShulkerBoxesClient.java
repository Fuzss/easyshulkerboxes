package fuzs.easyshulkerboxes.client;

import fuzs.easyshulkerboxes.client.gui.screens.inventory.tooltip.ClientContainerItemTooltip;
import fuzs.easyshulkerboxes.world.inventory.tooltip.ContainerItemTooltip;
import fuzs.puzzleslib.client.core.ClientCoreServices;

public class EasyShulkerBoxesClient {

    public static void onConstructMod() {
        ClientCoreServices.CLIENT_REGISTRATION.registerClientTooltipComponent(ContainerItemTooltip.class, ClientContainerItemTooltip::new);
    }
}
