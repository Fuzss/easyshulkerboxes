package fuzs.easyshulkerboxes.world.item.container;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fuzs.easyshulkerboxes.api.world.item.container.ItemContainerProvider;
import fuzs.easyshulkerboxes.world.inventory.tooltip.ContainerItemTooltip;
import fuzs.easyshulkerboxes.world.item.container.helper.ContainerItemHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;

public class GenericItemContainerProvider extends ItemContainerProviderImpl {
    private static final float[] DEFAULT_BACKGROUND_COLOR = {1.0F, 1.0F, 1.0F};

    private final int inventoryWidth;
    private final int inventoryHeight;
    @Nullable
    private final DyeColor dyeColor;
    private final String[] nbtKey;
    private final float[] backgroundColor;
    private boolean filterContainerItems;

    public GenericItemContainerProvider(int inventoryWidth, int inventoryHeight) {
        this(inventoryWidth, inventoryHeight, null, ContainerItemHelper.TAG_ITEMS);
    }

    public GenericItemContainerProvider(int inventoryWidth, int inventoryHeight, @Nullable DyeColor dyeColor, String... nbtKey) {
        this.inventoryWidth = inventoryWidth;
        this.inventoryHeight = inventoryHeight;
        this.dyeColor = dyeColor;
        this.backgroundColor = dyeColor == null ? this.getDefaultBackgroundColor() : ContainerItemHelper.getBackgroundColor(dyeColor);
        this.nbtKey = nbtKey.length == 0 ? new String[]{ContainerItemHelper.TAG_ITEMS} : nbtKey;
    }

    public GenericItemContainerProvider filterContainerItems() {
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

    protected float[] getDefaultBackgroundColor() {
        return DEFAULT_BACKGROUND_COLOR;
    }

    public String getNbtKey() {
        return this.nbtKey[this.nbtKey.length - 1];
    }

    private String[] getNbtPath() {
        return Arrays.copyOf(this.nbtKey, this.nbtKey.length - 1);
    }

    @Override
    public boolean hasItemContainerTag(ItemStack containerStack) {
        return ContainerItemHelper.hasItemContainerTag(containerStack, this, this.getNbtKey());
    }

    @Override
    public SimpleContainer getItemContainer(ItemStack containerStack, Player player, boolean allowSaving) {
        return ContainerItemHelper.loadItemContainer(containerStack, this, this.getInventorySize(), allowSaving, this.getNbtKey());
    }

    @Override
    public boolean isItemAllowedInContainer(ItemStack containerStack, ItemStack stackToAdd) {
        return !this.filterContainerItems || stackToAdd.getItem().canFitInsideContainerItems();
    }

    @Nullable
    @Override
    public final CompoundTag getItemData(ItemStack containerStack) {
        return this.getItemDataAtPath(this.getItemDataBase(containerStack), false);
    }

    @Nullable
    protected CompoundTag getItemDataBase(ItemStack containerStack) {
        return super.getItemData(containerStack);
    }

    @Nullable
    private CompoundTag getItemDataAtPath(@Nullable CompoundTag tag, boolean computeIfAbsent) {
        for (String path : this.getNbtPath()) {
            if (tag != null) {
                if (tag.contains(path, Tag.TAG_COMPOUND)) {
                    tag = tag.getCompound(path);
                } else if (computeIfAbsent) {
                    CompoundTag compoundTag = new CompoundTag();
                    tag.put(path, compoundTag);
                    tag = compoundTag;
                } else {
                    tag = null;
                }
            } else {
                break;
            }
        }
        return tag;
    }

    @Override
    public final void setItemData(ItemStack containerStack, ListTag itemsTag, String nbtKey) {
        CompoundTag itemData = this.getItemData(containerStack);
        if (itemsTag.isEmpty()) {
            CompoundTag tag = this.getItemDataAtPath(itemData, false);
            if (tag != null) tag.remove(nbtKey);
        } else {
            if (itemData == null) itemData = new CompoundTag();
            CompoundTag tag = this.getItemDataAtPath(itemData, true);
            Objects.requireNonNull(tag, "tag at path %s was null".formatted(Arrays.toString(this.getNbtPath())));
            tag.put(nbtKey, itemsTag);
        }
        if (itemData != null && itemData.isEmpty()) itemData = null;
        this.setItemDataToStack(containerStack, itemData);
    }

    protected void setItemDataToStack(ItemStack containerStack, @Nullable CompoundTag tag) {
        containerStack.setTag(tag);
    }

    @Override
    protected TooltipComponent internal$getTooltipImage(ItemStack stack, NonNullList<ItemStack> items) {
        return new ContainerItemTooltip(items, this.getInventoryWidth(), this.getInventoryHeight(), this.backgroundColor);
    }

    @Override
    public void toJson(JsonObject jsonObject) {
        jsonObject.addProperty("inventory_width", this.getInventoryWidth());
        jsonObject.addProperty("inventory_height", this.getInventoryHeight());
        if (this.dyeColor != null) {
            jsonObject.addProperty("background_color", this.dyeColor.getName());
        }
        if (this.nbtKey.length != 1 || !this.nbtKey[0].equals(ContainerItemHelper.TAG_ITEMS)) {
            jsonObject.addProperty("nbt_key", String.join("/", this.nbtKey));
        }
        if (this.filterContainerItems) {
            jsonObject.addProperty("filter_container_items", true);
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
        String[] nbtKey = GsonHelper.getAsString(jsonObject, "nbt_key", ContainerItemHelper.TAG_ITEMS).split("/");
        GenericItemContainerProvider provider = new GenericItemContainerProvider(inventoryWidth, inventoryHeight, dyeColor, nbtKey);
        if (jsonObject.has("filter_container_items")) {
            if (GsonHelper.getAsBoolean(jsonObject, "filter_container_items")) {
                provider.filterContainerItems();
            }
        }
        return provider;
    }
}
