package fuzs.easyshulkerboxes.world.inventory.provider;

import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.world.inventory.tooltip.ModBundleTooltip;
import fuzs.easyshulkerboxes.world.inventory.ContainerItemHelper;
import fuzs.easyshulkerboxes.config.ServerConfig;
import fuzs.easyshulkerboxes.mixin.client.accessor.BundleItemAccessor;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class BundleProvider extends ContainerItemProvider {
    public static final ContainerItemProvider INSTANCE = new BundleProvider();

    private BundleProvider() {

    }

    @Override
    public SimpleContainer getItemContainer(Player player, ItemStack stack, boolean allowSaving) {
        return ContainerItemHelper.loadBundleItemContainer(stack, allowSaving);
    }

    @Override
    protected boolean canItemFitInside(ItemStack containerStack, ItemStack stack) {
        return stack.getItem().canFitInsideContainerItems();
    }

    @Override
    protected boolean internal$canAcceptItem(ItemStack containerStack, ItemStack stack) {
        return this.canItemFitInside(containerStack, stack) && ContainerItemHelper.getAvailableBundleItemSpace(containerStack, stack, 64) > 0;
    }

    @Override
    protected int internal$getAcceptableItemCount(ItemStack containerStack, ItemStack stack) {
        return Math.min(ContainerItemHelper.getAvailableBundleItemSpace(containerStack, stack, 64), stack.getCount());
    }

    @Override
    public boolean isAllowed() {
        return EasyShulkerBoxes.CONFIG.get(ServerConfig.class).allowBundle;
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        return ContainerItemHelper.getTooltipContainer(stack, null, -1)
                .map(ContainerItemHelper::getContainerItems)
                .map(items -> new ModBundleTooltip(items, BundleItemAccessor.simpleinventorycontainers$getContentWeight(stack) >= 64));
    }
}
