package fuzs.easyshulkerboxes.world.item.container;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fuzs.easyshulkerboxes.api.world.item.container.ItemContainerProvider;
import fuzs.easyshulkerboxes.world.item.container.helper.ContainerItemHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Function;

public class ItemContainerProviderBuilder {
    private int inventoryWidth;
    private int inventoryHeight;
    @Nullable
    private DyeColor dyeColor;
    private String[] nbtKey;
    private boolean filterContainerItems;
    private BlockEntityType<?> blockEntityType;
    private int capacity;

    private ItemContainerProviderBuilder() {

    }

    public static Function<JsonElement, ItemContainerProvider> fromJson(Function<ItemContainerProviderBuilder, ItemContainerProvider> factory) {
        return jsonElement -> {
            ItemContainerProviderBuilder builder = new ItemContainerProviderBuilder();
            builder.fromJson(jsonElement);
            return factory.apply(builder);
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
        this.capacity = GsonHelper.getAsInt(jsonElement.getAsJsonObject(), "capacity", -1);
    }

    private void checkInventorySize(String type) {
        if (this.inventoryWidth == -1)
            throw new IllegalStateException(this.getMessage("inventory_width", type));
        if (this.inventoryHeight == -1)
            throw new IllegalStateException(this.getMessage("inventory_height", type));
    }

    private String getMessage(String jsonKey, String providerType) {
        return "'%s' not set for provider of type '%s'".formatted(jsonKey, providerType);
    }

    public ItemContainerProvider toSimpleItemContainerProvider() {
        this.checkInventorySize("item");
        SimpleItemProvider provider = new SimpleItemProvider(this.inventoryWidth, this.inventoryHeight, this.dyeColor, this.nbtKey);
        if (this.filterContainerItems) provider.filterContainerItems();
        return provider;
    }

    public ItemContainerProvider toBlockEntityProvider() {
        this.checkInventorySize("block_entity");
        Objects.requireNonNull(this.blockEntityType, this.getMessage("block_entity_type", "block_entity"));
        SimpleItemProvider provider = new BlockEntityProvider(this.blockEntityType, this.inventoryWidth, this.inventoryHeight, this.dyeColor, this.nbtKey);
        if (this.filterContainerItems) provider.filterContainerItems();
        return provider;
    }

    public ItemContainerProvider toBlockEntityViewProvider() {
        this.checkInventorySize("block_entity_view");
        Objects.requireNonNull(this.blockEntityType, this.getMessage("block_entity_type", "block_entity_view"));
        SimpleItemProvider provider = new BlockEntityViewProvider(this.blockEntityType, this.inventoryWidth, this.inventoryHeight, this.dyeColor, this.nbtKey);
        if (this.filterContainerItems) provider.filterContainerItems();
        return provider;
    }

    public ItemContainerProvider toShulkerBoxProvider() {
        this.checkInventorySize("shulker_box");
        Objects.requireNonNull(this.blockEntityType, this.getMessage("block_entity_type", "shulker_box"));
        SimpleItemProvider provider = new ShulkerBoxProvider(this.blockEntityType, this.inventoryWidth, this.inventoryHeight, this.dyeColor, this.nbtKey);
        if (this.filterContainerItems) provider.filterContainerItems();
        return provider;
    }

    public ItemContainerProvider toBundleProvider() {
        if (this.capacity == -1)
            throw new IllegalStateException(this.getMessage("capacity", "bundle"));
        return new BundleProvider(this.capacity, this.dyeColor, this.nbtKey);
    }

    public ItemContainerProvider toEnderChestProvider() {
        return new EnderChestProvider();
    }
}
