package fuzs.easyshulkerboxes.client;

import fuzs.easyshulkerboxes.client.gui.screens.inventory.tooltip.ClientContainerItemTooltip;
import fuzs.easyshulkerboxes.client.gui.screens.inventory.tooltip.ClientMapTooltip;
import fuzs.easyshulkerboxes.client.gui.screens.inventory.tooltip.ModClientBundleTooltip;
import fuzs.easyshulkerboxes.client.handler.KeyBindingHandler;
import fuzs.easyshulkerboxes.world.inventory.tooltip.ContainerItemTooltip;
import fuzs.easyshulkerboxes.world.inventory.tooltip.MapTooltip;
import fuzs.easyshulkerboxes.world.inventory.tooltip.ModBundleTooltip;
import fuzs.puzzleslib.client.core.ClientModConstructor;

public class EasyShulkerBoxesClient implements ClientModConstructor {

    @Override
    public void onRegisterKeyMappings(KeyMappingsContext context) {
        context.registerKeyMappings(KeyBindingHandler.TOGGLE_VISUAL_CONTENTS_KEY_MAPPING);
        context.registerKeyMappings(KeyBindingHandler.TOGGLE_SELECTED_TOOLTIPS_KEY_MAPPING);
    }

    @Override
    public void onRegisterClientTooltipComponents(ClientTooltipComponentsContext context) {
        context.registerClientTooltipComponent(ContainerItemTooltip.class, ClientContainerItemTooltip::new);
        context.registerClientTooltipComponent(ModBundleTooltip.class, ModClientBundleTooltip::new);
        context.registerClientTooltipComponent(MapTooltip.class, ClientMapTooltip::new);
    }
}
