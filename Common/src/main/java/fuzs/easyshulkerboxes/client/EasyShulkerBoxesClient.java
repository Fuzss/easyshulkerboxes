package fuzs.easyshulkerboxes.client;

import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.api.client.gui.screens.inventory.tooltip.ClientBundleItemTooltipImpl;
import fuzs.easyshulkerboxes.api.client.gui.screens.inventory.tooltip.ClientContainerItemTooltipImpl;
import fuzs.easyshulkerboxes.api.client.helper.ItemDecorationHelper;
import fuzs.easyshulkerboxes.api.world.inventory.ContainerItemProvider;
import fuzs.easyshulkerboxes.api.world.inventory.tooltip.BundleItemTooltip;
import fuzs.easyshulkerboxes.api.world.inventory.tooltip.ContainerItemTooltip;
import fuzs.easyshulkerboxes.config.ClientConfig;
import fuzs.puzzleslib.client.core.ClientModConstructor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class EasyShulkerBoxesClient implements ClientModConstructor {

    @Override
    public void onRegisterClientTooltipComponents(ClientTooltipComponentsContext context) {
        context.registerClientTooltipComponent(ContainerItemTooltip.class, tooltip -> new ClientContainerItemTooltipImpl(tooltip, EasyShulkerBoxes.CONFIG.get(ClientConfig.class)));
        context.registerClientTooltipComponent(BundleItemTooltip.class, tooltip -> new ClientBundleItemTooltipImpl(tooltip, EasyShulkerBoxes.CONFIG.get(ClientConfig.class)));
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
