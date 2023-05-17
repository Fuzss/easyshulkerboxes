package fuzs.easyshulkerboxes.api.container.v1.provider;

import com.google.gson.JsonObject;
import fuzs.easyshulkerboxes.api.container.v1.ContainerItemHelper;
import fuzs.easyshulkerboxes.api.container.v1.tooltip.ModBundleTooltip;
import fuzs.easyshulkerboxes.impl.world.item.container.ItemInteractionHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class BundleProvider extends NestedTagItemProvider {
    private final int capacity;

    public BundleProvider(int capacity) {
        this(capacity, DyeColor.BROWN, ItemInteractionHelper.TAG_ITEMS);
    }

    public BundleProvider(int capacity, @Nullable DyeColor dyeColor, String... nbtKey) {
        super(dyeColor, nbtKey);
        this.capacity = capacity;
    }

    protected int getCapacity() {
        return this.capacity;
    }

    @Override
    public SimpleContainer getItemContainer(ItemStack containerStack, Player player, boolean allowSaving) {
        // add one additional slot, so we can add items in the inventory
        String nbtKey = this.getNbtKey();
        return ContainerItemHelper.INSTANCE.loadItemContainer(containerStack, this, items -> new SimpleContainer(items + 1), allowSaving, nbtKey);
    }

    @Override
    public boolean isItemAllowedInContainer(ItemStack containerStack, ItemStack stackToAdd) {
        return super.isItemAllowedInContainer(containerStack, stackToAdd) && stackToAdd.getItem().canFitInsideContainerItems();
    }

    @Override
    public boolean canAddItem(ItemStack containerStack, ItemStack stackToAdd, Player player) {
        return this.getAvailableBundleItemSpace(containerStack, stackToAdd, player) > 0;
    }

    @Override
    public int getAcceptableItemCount(ItemStack containerStack, ItemStack stackToAdd, Player player) {
        return Math.min(this.getAvailableBundleItemSpace(containerStack, stackToAdd, player), super.getAcceptableItemCount(containerStack, stackToAdd, player));
    }

    public int getAvailableBundleItemSpace(ItemStack containerStack, ItemStack stackToAdd, Player player) {
        int itemWeight = ContainerItemHelper.INSTANCE.getItemWeight(stackToAdd);
        // fix java.lang.ArithmeticException: / by zero from Numismatic Overhaul as their coins stack to 99 instead of 64
        if (itemWeight <= 0) return 0;
        return (this.getCapacity() - this.getContentWeight(containerStack, player)) / itemWeight;
    }

    @Override
    public boolean canProvideTooltipImage(ItemStack containerStack, Player player) {
        return true;
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack containerStack, Player player) {
        // make sure to always override bundle tooltip, as otherwise vanilla tooltip would show for empty bundles
        if (!this.hasItemContainerData(containerStack)) return Optional.empty();
        return super.getTooltipImage(containerStack, player);
    }

    @Override
    public TooltipComponent createTooltipImageComponent(ItemStack containerStack, Player player, NonNullList<ItemStack> items) {
        return new ModBundleTooltip(items, this.getContentWeight(containerStack, player) >= this.getCapacity(), this.getBackgroundColor());
    }

    public int getContentWeight(ItemStack containerStack, Player player) {
        SimpleContainer container = this.getItemContainer(containerStack, player, false);
        return ContainerItemHelper.INSTANCE.convertContainerToList(container).stream().mapToInt(stack -> {
            return ContainerItemHelper.INSTANCE.getItemWeight(stack) * stack.getCount();
        }).sum();
    }

    @Override
    public void toJson(JsonObject jsonObject) {
        jsonObject.addProperty("capacity", this.getCapacity());
        super.toJson(jsonObject);
    }
}
