package fuzs.easyshulkerboxes.world.item.container;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fuzs.easyshulkerboxes.api.world.item.container.ItemContainerProvider;
import fuzs.easyshulkerboxes.mixin.client.accessor.BundleItemAccessor;
import fuzs.easyshulkerboxes.world.inventory.tooltip.ModBundleTooltip;
import fuzs.easyshulkerboxes.world.item.container.helper.ContainerItemHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class BundleProvider extends ItemContainerProviderImpl {
    private final int capacity;

    public BundleProvider() {
        this(64);
    }

    public BundleProvider(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public SimpleContainer getItemContainer(ItemStack containerStack, Player player, boolean allowSaving) {
        // add one additional slot, so we can add items in the inventory
        return ContainerItemHelper.loadItemContainer(containerStack, this, items -> new SimpleContainer(items + 1), allowSaving, ContainerItemHelper.TAG_ITEMS);
    }

    @Override
    public boolean isItemAllowedInContainer(ItemStack containerStack, ItemStack stackToAdd) {
        return stackToAdd.getItem().canFitInsideContainerItems();
    }

    @Override
    protected boolean internal$canAddItem(SimpleContainer container, ItemStack containerStack, ItemStack stack) {
        return ContainerItemHelper.getAvailableBundleItemSpace(containerStack, stack, this.capacity) > 0;
    }

    @Override
    protected int internal$getAcceptableItemCount(SimpleContainer container, ItemStack containerStack, ItemStack stack) {
        return Math.min(ContainerItemHelper.getAvailableBundleItemSpace(containerStack, stack, this.capacity), super.internal$getAcceptableItemCount(container, containerStack, stack));
    }

    @Override
    public boolean canProvideTooltipImage(ItemStack containerStack, Player player) {
        return true;
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack containerStack, Player player) {
        if (this.hasItemContainerTag(containerStack)) {
            return super.getTooltipImage(containerStack, player);
        }
        // make sure to always override bundle tooltip, as otherwise vanilla tooltip would show for empty bundles
        return Optional.empty();
    }

    @Override
    protected TooltipComponent internal$getTooltipImage(ItemStack stack, NonNullList<ItemStack> items) {
        return new ModBundleTooltip(items, BundleItemAccessor.easyshulkerboxes$getContentWeight(stack) >= this.capacity);
    }

    @Override
    public void toJson(JsonObject jsonObject) {
        jsonObject.addProperty("capacity", this.capacity);
    }

    public static ItemContainerProvider fromJson(JsonElement jsonElement) {
        return new BundleProvider(GsonHelper.getAsInt(jsonElement.getAsJsonObject(), "capacity", 64));
    }
}
