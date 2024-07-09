package fuzs.easyshulkerboxes.client;

import fuzs.easyshulkerboxes.client.gui.screens.inventory.tooltip.ClientMapTooltip;
import fuzs.easyshulkerboxes.world.inventory.tooltip.MapTooltip;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.ClientTooltipComponentsContext;

public class EasyShulkerBoxesClient implements ClientModConstructor {

    @Override
    public void onRegisterClientTooltipComponents(ClientTooltipComponentsContext context) {
        context.registerClientTooltipComponent(MapTooltip.class, ClientMapTooltip::new);
    }
}
