package fuzs.easyshulkerboxes.client;

import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.api.client.gui.screens.inventory.tooltip.ClientContainerItemTooltip;
import fuzs.easyshulkerboxes.api.client.helper.ItemDecorationHelper;
import fuzs.easyshulkerboxes.api.world.inventory.tooltip.ContainerItemTooltip;
import fuzs.easyshulkerboxes.api.world.item.ContainerItemHelper;
import fuzs.easyshulkerboxes.config.ClientConfig;
import fuzs.easyshulkerboxes.mixin.client.accessor.BundleItemAccessor;
import fuzs.puzzleslib.client.core.ClientModConstructor;
import fuzs.puzzleslib.proxy.Proxy;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EnderChestBlock;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class EasyShulkerBoxesClient implements ClientModConstructor {

    @Override
    public void onRegisterClientTooltipComponents(ClientTooltipComponentsContext context) {
        context.registerClientTooltipComponent(ContainerItemTooltip.class, tooltip -> new ClientContainerItemTooltip(tooltip, EasyShulkerBoxes.CONFIG.get(ClientConfig.class)));
    }

    @Override
    public void onRegisterItemDecorations(ItemDecorationContext context) {
        for (Block block : Registry.BLOCK) {
            if (block instanceof ShulkerBoxBlock) {
                context.register(block, ItemDecorationHelper.getDynamicItemDecorator((AbstractContainerScreen<?> screen, ItemStack containerStack, ItemStack carriedStack) -> {
                    return ContainerItemHelper.loadItemContainer(containerStack, BlockEntityType.SHULKER_BOX, 3, false).canAddItem(carriedStack);
                }, () -> EasyShulkerBoxes.CONFIG.get(ClientConfig.class).containerItemIndicator));
            } else if (block instanceof EnderChestBlock) {
                context.register(block, ItemDecorationHelper.getDynamicItemDecorator((AbstractContainerScreen<?> screen, ItemStack containerStack, ItemStack carriedStack) -> {
                    return Proxy.INSTANCE.getClientPlayer().getEnderChestInventory().canAddItem(carriedStack);
                }, () -> EasyShulkerBoxes.CONFIG.get(ClientConfig.class).containerItemIndicator));
            }
        }
        for (Item item : Registry.ITEM) {
            if (item instanceof BundleItem) {
                context.register(item, ItemDecorationHelper.getDynamicItemDecorator((AbstractContainerScreen<?> screen, ItemStack containerStack, ItemStack carriedStack) -> {
                    int weight = BundleItemAccessor.callGetWeight(carriedStack);
                    // fix java.lang.ArithmeticException: / by zero from Numismatic Overhaul as their coins stack to 99 instead of 64
                    if (weight > 0) {
                        int remainingWeight = (64 - BundleItemAccessor.callGetContentWeight(containerStack)) / weight;
                        return remainingWeight > 0;
                    }
                    return false;
                }, () -> EasyShulkerBoxes.CONFIG.get(ClientConfig.class).containerItemIndicator));
            }
        }
    }
}
