package fuzs.easyshulkerboxes.integration;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fuzs.easyshulkerboxes.api.world.item.container.ItemContainerProvider;
import fuzs.easyshulkerboxes.world.item.container.GenericItemContainerProvider;
import fuzs.easyshulkerboxes.world.item.container.helper.ContainerItemHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleBackpackProvider extends GenericItemContainerProvider {

    public SimpleBackpackProvider(int inventoryWidth, int inventoryHeight) {
        super(inventoryWidth, inventoryHeight);
    }

    public SimpleBackpackProvider(int inventoryWidth, int inventoryHeight, @Nullable DyeColor backgroundColor, String nbtKey) {
        super(inventoryWidth, inventoryHeight, backgroundColor, nbtKey);
    }

    public SimpleBackpackProvider(int inventoryWidth, int inventoryHeight, @NotNull float[] backgroundColor, String nbtKey) {
        super(inventoryWidth, inventoryHeight, backgroundColor, nbtKey);
    }

    public SimpleBackpackProvider(int inventoryWidth, int inventoryHeight, String nbtKey) {
        super(inventoryWidth, inventoryHeight, nbtKey);
    }

    @Override
    public @Nullable CompoundTag getItemTag(ItemStack containerStack) {
        return containerStack.getTagElement("backpack");
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
        return new SimpleBackpackProvider(inventoryWidth, inventoryHeight, dyeColor, nbtKey);
    }
}
