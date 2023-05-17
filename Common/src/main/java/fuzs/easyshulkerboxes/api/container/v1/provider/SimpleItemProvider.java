package fuzs.easyshulkerboxes.api.container.v1.provider;

import com.google.gson.JsonObject;
import fuzs.easyshulkerboxes.api.container.v1.ContainerItemHelper;
import fuzs.easyshulkerboxes.api.container.v1.tooltip.ContainerItemTooltip;
import fuzs.easyshulkerboxes.impl.world.item.container.ItemInteractionHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class SimpleItemProvider extends NestedTagItemProvider {
    private final int inventoryWidth;
    private final int inventoryHeight;
    private boolean filterContainerItems;

    public SimpleItemProvider(int inventoryWidth, int inventoryHeight) {
        this(inventoryWidth, inventoryHeight, null, ItemInteractionHelper.TAG_ITEMS);
    }

    public SimpleItemProvider(int inventoryWidth, int inventoryHeight, @Nullable DyeColor dyeColor, String... nbtKey) {
        super(dyeColor, nbtKey);
        this.inventoryWidth = inventoryWidth;
        this.inventoryHeight = inventoryHeight;
    }

    public SimpleItemProvider filterContainerItems() {
        this.filterContainerItems = true;
        return this;
    }

    protected int getInventoryWidth() {
        return this.inventoryWidth;
    }

    protected int getInventoryHeight() {
        return this.inventoryHeight;
    }

    protected int getInventorySize() {
        return this.getInventoryWidth() * this.getInventoryHeight();
    }

    @Override
    public SimpleContainer getItemContainer(ItemStack containerStack, Player player, boolean allowSaving) {
        int inventorySize = this.getInventorySize();
        return ContainerItemHelper.INSTANCE.loadItemContainer(containerStack, this, inventorySize, allowSaving, this.getNbtKey());
    }

    @Override
    public boolean isItemAllowedInContainer(ItemStack containerStack, ItemStack stackToAdd) {
        return super.isItemAllowedInContainer(containerStack, stackToAdd) && (!this.filterContainerItems || stackToAdd.getItem().canFitInsideContainerItems());
    }

    @Override
    public TooltipComponent createTooltipImageComponent(ItemStack containerStack, Player player, NonNullList<ItemStack> items) {
        return new ContainerItemTooltip(items, this.getInventoryWidth(), this.getInventoryHeight(), this.getBackgroundColor());
    }

    @Override
    public void toJson(JsonObject jsonObject) {
        jsonObject.addProperty("inventory_width", this.getInventoryWidth());
        jsonObject.addProperty("inventory_height", this.getInventoryHeight());
        if (this.filterContainerItems) {
            jsonObject.addProperty("filter_container_items", true);
        }
        super.toJson(jsonObject);
    }
}
