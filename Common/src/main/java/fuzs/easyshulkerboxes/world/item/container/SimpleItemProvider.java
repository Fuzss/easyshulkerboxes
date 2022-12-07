package fuzs.easyshulkerboxes.world.item.container;

import com.google.gson.JsonObject;
import fuzs.easyshulkerboxes.world.inventory.tooltip.ContainerItemTooltip;
import fuzs.easyshulkerboxes.world.item.container.helper.ContainerItemHelper;
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
        this(inventoryWidth, inventoryHeight, null, ContainerItemHelper.TAG_ITEMS);
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

    public int getInventoryWidth() {
        return this.inventoryWidth;
    }

    public int getInventoryHeight() {
        return this.inventoryHeight;
    }

    public int getInventorySize() {
        return this.getInventoryWidth() * this.getInventoryHeight();
    }

    @Override
    public SimpleContainer getItemContainer(ItemStack containerStack, Player player, boolean allowSaving) {
        return ContainerItemHelper.loadItemContainer(containerStack, this, this.getInventorySize(), allowSaving, this.getNbtKey());
    }

    @Override
    public boolean isItemAllowedInContainer(ItemStack containerStack, ItemStack stackToAdd) {
        return !this.filterContainerItems || stackToAdd.getItem().canFitInsideContainerItems();
    }

    @Override
    protected TooltipComponent internal$getTooltipImage(ItemStack stack, NonNullList<ItemStack> items) {
        return new ContainerItemTooltip(items, this.getInventoryWidth(), this.getInventoryHeight(), this.getBackgroundColor());
    }

    @Override
    public void toJson(JsonObject jsonObject) {
        super.toJson(jsonObject);
        jsonObject.addProperty("inventory_width", this.getInventoryWidth());
        jsonObject.addProperty("inventory_height", this.getInventoryHeight());
        if (this.filterContainerItems) {
            jsonObject.addProperty("filter_container_items", true);
        }
    }
}
