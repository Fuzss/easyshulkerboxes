package fuzs.easyshulkerboxes.world.item.container;

import fuzs.easyshulkerboxes.mixin.client.accessor.BundleItemAccessor;
import fuzs.easyshulkerboxes.world.inventory.tooltip.ModBundleTooltip;
import fuzs.easyshulkerboxes.world.item.container.helper.ContainerItemHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public class BundleProvider extends ItemContainerProviderImpl {

    @Override
    public SimpleContainer getItemContainer(ItemStack stack, Player player, boolean allowSaving) {
        return ContainerItemHelper.loadBundleItemContainer(stack, allowSaving);
    }

    @Override
    public boolean isItemAllowedInContainer(ItemStack containerStack, ItemStack stackToAdd) {
        return stackToAdd.getItem().canFitInsideContainerItems();
    }

    @Override
    protected boolean internal$canAddItem(SimpleContainer container, ItemStack containerStack, ItemStack stack) {
        return ContainerItemHelper.getAvailableBundleItemSpace(containerStack, stack, 64) > 0;
    }

    @Override
    protected int internal$getAcceptableItemCount(SimpleContainer container, ItemStack containerStack, ItemStack stack) {
        return Math.min(ContainerItemHelper.getAvailableBundleItemSpace(containerStack, stack, 64), super.internal$getAcceptableItemCount(container, containerStack, stack));
    }

    @Override
    protected TooltipComponent internal$getTooltipImage(ItemStack stack, NonNullList<ItemStack> items) {
        return new ModBundleTooltip(items, BundleItemAccessor.easyshulkerboxes$getContentWeight(stack) >= 64);
    }
}
