package fuzs.easyshulkerboxes.client;

import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.client.gui.screens.inventory.tooltip.ModClientBundleTooltip;
import fuzs.easyshulkerboxes.client.gui.screens.inventory.tooltip.ClientContainerItemTooltip;
import fuzs.easyshulkerboxes.client.gui.screens.inventory.tooltip.ClientMapTooltip;
import fuzs.easyshulkerboxes.client.helper.ItemDecorationHelper;
import fuzs.easyshulkerboxes.client.init.ClientModRegistry;
import fuzs.easyshulkerboxes.config.ClientConfig;
import fuzs.easyshulkerboxes.world.inventory.provider.ContainerItemProvider;
import fuzs.easyshulkerboxes.world.inventory.tooltip.ContainerItemTooltip;
import fuzs.easyshulkerboxes.world.inventory.tooltip.MapTooltip;
import fuzs.easyshulkerboxes.world.inventory.tooltip.ModBundleTooltip;
import fuzs.puzzleslib.client.core.ClientModConstructor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class EasyShulkerBoxesClient implements ClientModConstructor {

    @Override
    public void onRegisterKeyMappings(KeyMappingsContext context) {
        context.registerKeyMappings(ClientModRegistry.TOGGLE_VISUAL_CONTENTS_KEY_MAPPING);
        context.registerKeyMappings(ClientModRegistry.TOGGLE_SELECTED_TOOLTIPS_KEY_MAPPING);
    }

    @Override
    public void onRegisterClientTooltipComponents(ClientTooltipComponentsContext context) {
        context.registerClientTooltipComponent(ContainerItemTooltip.class, ClientContainerItemTooltip::new);
        context.registerClientTooltipComponent(ModBundleTooltip.class, ModClientBundleTooltip::new);
        context.registerClientTooltipComponent(MapTooltip.class, ClientMapTooltip::new);
    }

    @Override
    public void onRegisterItemDecorations(ItemDecorationContext context) {
        for (Map.Entry<Item, ContainerItemProvider> entry : ContainerItemProvider.REGISTRY.entrySet()) {
            context.register(entry.getKey(), ItemDecorationHelper.getDynamicItemDecorator((AbstractContainerScreen<?> screen, ItemStack containerStack, ItemStack carriedStack) -> {
                return entry.getValue().canAcceptItem(containerStack, carriedStack);
            }, () -> EasyShulkerBoxes.CONFIG.get(ClientConfig.class).containerItemIndicator));
        }
    }
}
