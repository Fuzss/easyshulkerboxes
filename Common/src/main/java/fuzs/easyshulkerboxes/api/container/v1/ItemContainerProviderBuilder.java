package fuzs.easyshulkerboxes.api.container.v1;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fuzs.easyshulkerboxes.world.item.container.EnderChestProvider;
import fuzs.easyshulkerboxes.impl.world.item.container.ContainerItemHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.StreamSupport;

public class ItemContainerProviderBuilder {
    private int inventoryWidth;
    private int inventoryHeight;
    @Nullable
    private DyeColor dyeColor;
    private String[] nbtKey;
    private boolean filterContainerItems;
    private BlockEntityType<?> blockEntityType;
    private int capacity;
    private List<String> disallowedItems;
    private boolean anyGameMode;

    private ItemContainerProviderBuilder() {

    }

    public static Function<JsonElement, ItemContainerProvider> fromJson(Function<ItemContainerProviderBuilder, ItemContainerProvider> factory) {
        return jsonElement -> {
            ItemContainerProviderBuilder builder = new ItemContainerProviderBuilder();
            builder.fromJson(jsonElement);
            ItemContainerProvider provider = factory.apply(builder);
            if (provider instanceof SimpleItemProvider itemProvider && builder.filterContainerItems) {
                itemProvider.filterContainerItems();
            }
            if (provider instanceof NestedTagItemProvider nestedTagProvider) {
                nestedTagProvider.disallowValues(builder.disallowedItems);
            }
            return provider;
        };
    }

    private void fromJson(JsonElement jsonElement) {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        this.inventoryWidth = GsonHelper.getAsInt(jsonObject, "inventory_width", -1);
        this.inventoryHeight = GsonHelper.getAsInt(jsonObject, "inventory_height", -1);
        this.dyeColor = DyeColor.byName(GsonHelper.getAsString(jsonObject, "background_color", ""), null);
        this.nbtKey = GsonHelper.getAsString(jsonObject, "nbt_key", ContainerItemHelper.TAG_ITEMS).split("/");
        this.filterContainerItems = GsonHelper.getAsBoolean(jsonObject, "filter_container_items", false);
        if (jsonObject.has("block_entity_type")) {
            ResourceLocation blockEntityTypeKey = new ResourceLocation(GsonHelper.getAsString(jsonObject, "block_entity_type"));
            this.blockEntityType = Registry.BLOCK_ENTITY_TYPE.get(blockEntityTypeKey);
        }
        this.capacity = GsonHelper.getAsInt(jsonObject, "capacity", -1);
        JsonArray disallowedItemsData = GsonHelper.getAsJsonArray(jsonObject, "disallowed_items", new JsonArray());
        this.disallowedItems = StreamSupport.stream(disallowedItemsData.spliterator(), false).map(JsonElement::getAsString).toList();
        this.anyGameMode = GsonHelper.getAsBoolean(jsonObject, "any_game_mode", false);
    }

    public ItemContainerProvider toSimpleItemContainerProvider() {
        this.checkInventorySize("item");
        return new SimpleItemProvider(this.inventoryWidth, this.inventoryHeight, this.dyeColor, this.nbtKey);
    }

    public ItemContainerProvider toBlockEntityProvider() {
        this.checkInventorySize("block_entity");
        Objects.requireNonNull(this.blockEntityType, getErrorMessage("block_entity_type", "block_entity"));
        BlockEntityProvider provider = new BlockEntityProvider(this.blockEntityType, this.inventoryWidth, this.inventoryHeight, this.dyeColor, this.nbtKey);
        if (this.anyGameMode) provider.anyGameMode();
        return provider;
    }

    public ItemContainerProvider toBlockEntityViewProvider() {
        this.checkInventorySize("block_entity_view");
        Objects.requireNonNull(this.blockEntityType, getErrorMessage("block_entity_type", "block_entity_view"));
        return new BlockEntityViewProvider(this.blockEntityType, this.inventoryWidth, this.inventoryHeight, this.dyeColor, this.nbtKey);
    }

    public ItemContainerProvider toBundleProvider() {
        if (this.capacity == -1)
            throw new IllegalStateException(getErrorMessage("capacity", "bundle"));
        return new BundleProvider(this.capacity, this.dyeColor, this.nbtKey);
    }

    public ItemContainerProvider toEnderChestProvider() {
        return new EnderChestProvider();
    }

    private void checkInventorySize(String type) {
        if (this.inventoryWidth == -1)
            throw new IllegalStateException(getErrorMessage("inventory_width", type));
        if (this.inventoryHeight == -1)
            throw new IllegalStateException(getErrorMessage("inventory_height", type));
    }

    private static String getErrorMessage(String jsonKey, String providerType) {
        return "'%s' not set for provider of type '%s'".formatted(jsonKey, providerType);
    }
}
