package fuzs.easyshulkerboxes.client;

import fuzs.easyshulkerboxes.impl.client.core.HeldActivationType;
import fuzs.easyshulkerboxes.impl.client.core.KeyMappingProvider;
import fuzs.easyshulkerboxes.api.client.container.v1.tooltip.ClientContainerItemTooltip;
import fuzs.easyshulkerboxes.client.gui.screens.inventory.tooltip.ClientMapTooltip;
import fuzs.easyshulkerboxes.api.client.container.v1.tooltip.ModClientBundleTooltip;
import fuzs.easyshulkerboxes.api.container.v1.tooltip.ContainerItemTooltip;
import fuzs.easyshulkerboxes.world.inventory.tooltip.MapTooltip;
import fuzs.easyshulkerboxes.api.container.v1.tooltip.ModBundleTooltip;
import fuzs.puzzleslib.client.core.ClientModConstructor;

public class EasyShulkerBoxesClient implements ClientModConstructor {

    @Override
    public void onRegisterKeyMappings(KeyMappingsContext context) {
        HeldActivationType.getKeyMappingProviders().map(KeyMappingProvider::getKeyMapping).forEach(context::registerKeyMapping);
    }

    @Override
    public void onRegisterClientTooltipComponents(ClientTooltipComponentsContext context) {
        context.registerClientTooltipComponent(ContainerItemTooltip.class, ClientContainerItemTooltip::new);
        context.registerClientTooltipComponent(ModBundleTooltip.class, ModClientBundleTooltip::new);
        context.registerClientTooltipComponent(MapTooltip.class, ClientMapTooltip::new);
    }
}
