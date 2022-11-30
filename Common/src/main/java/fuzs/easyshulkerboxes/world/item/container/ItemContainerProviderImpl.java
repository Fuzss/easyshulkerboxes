package fuzs.easyshulkerboxes.world.item.container;

import fuzs.easyshulkerboxes.api.world.item.container.ItemContainerProvider;
import fuzs.easyshulkerboxes.world.item.container.helper.ContainerItemHelper;
import fuzs.puzzleslib.proxy.Proxy;
import net.minecraft.core.NonNullList;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public abstract class ItemContainerProviderImpl implements ItemContainerProvider {

    @Override
    public final Optional<SimpleContainer> getItemContainer(Player player, ItemStack stack, boolean allowSaving) {
        if (!this.canProvideContainer(stack, player)) return Optional.empty();
        return Optional.of(this.internal$getItemContainer(player, stack, allowSaving));
    }

    protected boolean canProvideContainer(ItemStack stack, Player player) {
        return stack.getCount() == 1;
    }

    protected abstract SimpleContainer internal$getItemContainer(Player player, ItemStack stack, boolean allowSaving);

    @Override
    public boolean canItemFitInside(ItemStack containerStack, ItemStack stack) {
        return true;
    }

    @Override
    public final boolean canAddItem(ItemStack containerStack, ItemStack stack) {
        return this.canAcceptItem(containerStack, stack) && this.internal$canAddItem(containerStack, stack);
    }

    protected boolean internal$canAddItem(ItemStack containerStack, ItemStack stack) {
        return this.getItemContainer(Proxy.INSTANCE.getClientPlayer(), containerStack, false).map(container -> container.canAddItem(stack)).orElse(false);
    }

    @Override
    public final int getAcceptableItemCount(ItemStack containerStack, ItemStack stack) {
        return this.canAcceptItem(containerStack, stack) ? this.internal$getAcceptableItemCount(containerStack, stack) : 0;
    }

    protected int internal$getAcceptableItemCount(ItemStack containerStack, ItemStack stack) {
        return stack.getCount();
    }

    private boolean canAcceptItem(ItemStack containerStack, ItemStack stack) {
        return !stack.isEmpty() && this.canItemFitInside(containerStack, stack);
    }

    @Override
    public final Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        if (!this.canProvideTooltipImage(stack)) return Optional.empty();
        SimpleContainer itemContainer = this.internal$getItemContainer(Proxy.INSTANCE.getClientPlayer(), stack, false);
        return Optional.of(this.internal$getTooltipImage(stack, ContainerItemHelper.containerToList(itemContainer)));
    }

    public boolean canProvideTooltipImage(ItemStack stack) {
        return ContainerItemHelper.hasItemContainerTag(stack, null);
    }

    protected abstract TooltipComponent internal$getTooltipImage(ItemStack stack, NonNullList<ItemStack> items);

    @Override
    public void broadcastContainerChanges(Player player) {

    }
}
