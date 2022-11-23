package fuzs.easyshulkerboxes.world.inventory;

import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.api.world.inventory.ContainerItemProvider;
import fuzs.easyshulkerboxes.api.world.inventory.tooltip.BundleItemTooltip;
import fuzs.easyshulkerboxes.api.world.item.container.ContainerItemHelper;
import fuzs.easyshulkerboxes.config.ServerConfig;
import fuzs.easyshulkerboxes.mixin.client.accessor.BundleItemAccessor;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class BundleProvider implements ContainerItemProvider {
    public static final ContainerItemProvider INSTANCE = new BundleProvider();

    private BundleProvider() {

    }

    @Override
    public SimpleContainer getItemContainer(Player player, ItemStack stack, boolean allowSaving) {
        return ContainerItemHelper.loadItemContainer(stack, null, -1, allowSaving);
    }

    @Override
    public int acceptableItemCount(ItemStack containerStack, ItemStack stack) {
        return !stack.getItem().canFitInsideContainerItems() ? 0 : Math.min(ContainerItemHelper.getAvailableBundleItemSpace(containerStack, stack, 64), stack.getCount());
    }

    @Override
    public boolean isAllowed() {
        return EasyShulkerBoxes.CONFIG.get(ServerConfig.class).allowBundle;
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        return ContainerItemHelper.getTooltipContainer(stack, null, -1).map(ContainerItemHelper::getContainerItems).map(items -> new BundleItemTooltip(items, BundleItemAccessor.callGetContentWeight(stack) >= 64));
    }
}
