package fuzs.easyshulkerboxes.client;

import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.api.client.gui.screens.inventory.tooltip.ClientBundleItemTooltipImpl;
import fuzs.easyshulkerboxes.api.client.gui.screens.inventory.tooltip.ClientContainerItemTooltipImpl;
import fuzs.easyshulkerboxes.api.client.helper.ItemDecorationHelper;
import fuzs.easyshulkerboxes.api.world.inventory.tooltip.BundleItemTooltip;
import fuzs.easyshulkerboxes.api.world.inventory.tooltip.ContainerItemTooltip;
import fuzs.easyshulkerboxes.api.world.item.container.ContainerItemHelper;
import fuzs.easyshulkerboxes.config.ClientConfig;
import fuzs.easyshulkerboxes.world.inventory.EnderChestProvider;
import fuzs.easyshulkerboxes.world.inventory.ShulkerBoxProvider;
import fuzs.puzzleslib.client.core.ClientModConstructor;
import fuzs.puzzleslib.proxy.Proxy;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;

import java.util.Map;

public class EasyShulkerBoxesClient implements ClientModConstructor {

    @Override
    public void onRegisterClientTooltipComponents(ClientTooltipComponentsContext context) {
        context.registerClientTooltipComponent(ContainerItemTooltip.class, tooltip -> new ClientContainerItemTooltipImpl(tooltip, EasyShulkerBoxes.CONFIG.get(ClientConfig.class)));
        context.registerClientTooltipComponent(BundleItemTooltip.class, tooltip -> new ClientBundleItemTooltipImpl(tooltip, EasyShulkerBoxes.CONFIG.get(ClientConfig.class)));
    }

    @Override
    public void onRegisterItemDecorations(ItemDecorationContext context) {
        for (Map.Entry<ResourceKey<Block>, Block> entry : Registry.BLOCK.entrySet()) {
            // only affect vanilla shulker boxes, other mods might add shulker boxes with a different inventory size
            if (entry.getValue() instanceof ShulkerBoxBlock && entry.getKey().location().getNamespace().equals("minecraft")) {
                context.register(entry.getValue(), ItemDecorationHelper.getDynamicItemDecorator((AbstractContainerScreen<?> screen, ItemStack containerStack, ItemStack carriedStack) -> {
                    return ShulkerBoxProvider.INSTANCE.getItemContainer(Proxy.INSTANCE.getClientPlayer(), containerStack, false).canAddItem(carriedStack);
                }, () -> EasyShulkerBoxes.CONFIG.get(ClientConfig.class).containerItemIndicator));
            }
        }
        context.register(Items.ENDER_CHEST, ItemDecorationHelper.getDynamicItemDecorator((AbstractContainerScreen<?> screen, ItemStack containerStack, ItemStack carriedStack) -> {
            return EnderChestProvider.INSTANCE.getItemContainer(Proxy.INSTANCE.getClientPlayer(), containerStack).canAddItem(carriedStack);
        }, () -> EasyShulkerBoxes.CONFIG.get(ClientConfig.class).containerItemIndicator));
        context.register(Items.BUNDLE, ItemDecorationHelper.getDynamicItemDecorator((AbstractContainerScreen<?> screen, ItemStack containerStack, ItemStack carriedStack) -> {
            return ContainerItemHelper.getAvailableBundleItemSpace(containerStack, carriedStack, 64) > 0;
        }, () -> EasyShulkerBoxes.CONFIG.get(ClientConfig.class).containerItemIndicator));
    }
}
