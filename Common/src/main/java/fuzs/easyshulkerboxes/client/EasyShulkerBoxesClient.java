package fuzs.easyshulkerboxes.client;

import fuzs.easyshulkerboxes.client.core.HeldActivationType;
import fuzs.easyshulkerboxes.client.core.KeyMappingProvider;
import fuzs.easyshulkerboxes.client.gui.screens.inventory.tooltip.ClientContainerItemTooltip;
import fuzs.easyshulkerboxes.client.gui.screens.inventory.tooltip.ClientMapTooltip;
import fuzs.easyshulkerboxes.client.gui.screens.inventory.tooltip.ModClientBundleTooltip;
import fuzs.easyshulkerboxes.world.inventory.tooltip.ContainerItemTooltip;
import fuzs.easyshulkerboxes.world.inventory.tooltip.MapTooltip;
import fuzs.easyshulkerboxes.world.inventory.tooltip.ModBundleTooltip;
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
