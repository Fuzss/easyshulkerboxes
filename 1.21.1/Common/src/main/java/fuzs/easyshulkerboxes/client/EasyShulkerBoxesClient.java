package fuzs.easyshulkerboxes.client;

import fuzs.easyshulkerboxes.client.gui.screens.inventory.tooltip.ClientMapContentsTooltip;
import fuzs.easyshulkerboxes.world.inventory.tooltip.MapContentsTooltip;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.ClientTooltipComponentsContext;

public class EasyShulkerBoxesClient implements ClientModConstructor {

    @Override
    public void onRegisterClientTooltipComponents(ClientTooltipComponentsContext context) {
        context.registerClientTooltipComponent(MapContentsTooltip.class, ClientMapContentsTooltip::new);
    }
}
