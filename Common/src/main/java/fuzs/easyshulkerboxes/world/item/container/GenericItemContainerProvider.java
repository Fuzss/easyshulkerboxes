package fuzs.easyshulkerboxes.world.item.container;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fuzs.easyshulkerboxes.api.world.item.container.ItemContainerProvider;
import fuzs.easyshulkerboxes.world.inventory.tooltip.ContainerItemTooltip;
import fuzs.easyshulkerboxes.world.item.container.helper.ContainerItemHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GenericItemContainerProvider extends ItemContainerProviderImpl {
    protected final int inventoryWidth;
    protected final int inventoryHeight;
    protected final String nbtKey;
    @Nullable
    private DyeColor dyeColor;
    private float[] backgroundColor;

    public GenericItemContainerProvider(int inventoryWidth, int inventoryHeight) {
        this(inventoryWidth, inventoryHeight, (DyeColor) null, ContainerItemHelper.TAG_ITEMS);
    }

    public GenericItemContainerProvider(int inventoryWidth, int inventoryHeight, @Nullable DyeColor backgroundColor, String nbtKey) {
        this(inventoryWidth, inventoryHeight, nbtKey);
        this.dyeColor = backgroundColor;
        this.backgroundColor = ContainerItemHelper.getBackgroundColor(backgroundColor);
    }

    public GenericItemContainerProvider(int inventoryWidth, int inventoryHeight, @NotNull float[] backgroundColor, String nbtKey) {
        this(inventoryWidth, inventoryHeight, nbtKey);
        this.dyeColor = null;
        this.backgroundColor = backgroundColor;
    }

    public GenericItemContainerProvider(int inventoryWidth, int inventoryHeight, String nbtKey) {
        this.inventoryWidth = inventoryWidth;
        this.inventoryHeight = inventoryHeight;
        this.nbtKey = nbtKey;
    }

    public int getInventorySize() {
        return this.getInventoryWidth() * this.getInventoryHeight();
    }

    public int getInventoryWidth() {
        return this.inventoryWidth;
    }

    public int getInventoryHeight() {
        return this.inventoryHeight;
    }

    public DyeColor getDyeColor() {
        return this.dyeColor;
    }

    @Override
    public SimpleContainer getItemContainer(ItemStack stack, Player player, boolean allowSaving) {
        return ContainerItemHelper.loadGenericItemContainer(stack, null, this.getInventorySize(), allowSaving, this.nbtKey);
    }

    @Override
    protected TooltipComponent internal$getTooltipImage(ItemStack stack, NonNullList<ItemStack> items) {
        return new ContainerItemTooltip(items, this.getInventoryWidth(), this.getInventoryHeight(), this.backgroundColor);
    }

    @Override
    public void toJson(JsonObject jsonObject) {
        super.toJson(jsonObject);
        jsonObject.addProperty("inventory_width", this.getInventoryWidth());
        jsonObject.addProperty("inventory_height", this.getInventoryHeight());
        if (this.dyeColor != null) {
            jsonObject.addProperty("background_color", this.dyeColor.getName());
        }
        if (!this.nbtKey.equals(ContainerItemHelper.TAG_ITEMS)) {
            jsonObject.addProperty("nbt_key", this.nbtKey);
        }
    }

    public static ItemContainerProvider fromJson(JsonElement jsonElement) {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        int inventoryWidth = GsonHelper.getAsInt(jsonObject, "inventory_width");
        int inventoryHeight = GsonHelper.getAsInt(jsonObject, "inventory_height");
        DyeColor dyeColor = null;
        if (jsonObject.has("background_color")) {
            dyeColor = DyeColor.byName(GsonHelper.getAsString(jsonObject, "background_color"), null);
        }
        String nbtKey = GsonHelper.getAsString(jsonObject, "nbt_key", ContainerItemHelper.TAG_ITEMS);
        return new GenericItemContainerProvider(inventoryWidth, inventoryHeight, dyeColor, nbtKey);
    }
}
