package fuzs.easyshulkerboxes.world.item.container;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fuzs.easyshulkerboxes.api.world.item.container.ItemContainerProvider;
import fuzs.easyshulkerboxes.world.inventory.tooltip.ContainerItemTooltip;
import fuzs.easyshulkerboxes.world.item.container.helper.ContainerItemHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class GenericItemContainerProvider extends ItemContainerProviderImpl {
    private final int inventoryWidth;
    private final int inventoryHeight;
    private final String[] nbtKey;
    @Nullable
    private DyeColor dyeColor;
    private float[] backgroundColor;

    public GenericItemContainerProvider(int inventoryWidth, int inventoryHeight) {
        this(inventoryWidth, inventoryHeight, (DyeColor) null, ContainerItemHelper.TAG_ITEMS);
    }

    public GenericItemContainerProvider(int inventoryWidth, int inventoryHeight, @Nullable DyeColor backgroundColor) {
        this(inventoryWidth, inventoryHeight, backgroundColor, ContainerItemHelper.TAG_ITEMS);
    }

    public GenericItemContainerProvider(int inventoryWidth, int inventoryHeight, @Nullable DyeColor backgroundColor, String... nbtKey) {
        this(inventoryWidth, inventoryHeight, nbtKey);
        this.dyeColor = backgroundColor;
        this.backgroundColor = ContainerItemHelper.getBackgroundColor(backgroundColor);
    }

    public GenericItemContainerProvider(int inventoryWidth, int inventoryHeight, @NotNull float[] backgroundColor, String... nbtKey) {
        this(inventoryWidth, inventoryHeight, nbtKey);
        this.dyeColor = null;
        this.backgroundColor = backgroundColor;
    }

    public GenericItemContainerProvider(int inventoryWidth, int inventoryHeight, String... nbtKey) {
        this.inventoryWidth = inventoryWidth;
        this.inventoryHeight = inventoryHeight;
        if (nbtKey.length < 1) throw new IllegalArgumentException("nbtKey path must have at least one entry");
        this.nbtKey = nbtKey;
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

    public DyeColor getDyeColor() {
        return this.dyeColor;
    }

    public String getNbtKey() {
        return this.nbtKey[this.nbtKey.length - 1];
    }

    public String[] getNbtPath() {
        return Arrays.copyOf(this.nbtKey, this.nbtKey.length - 1);
    }

    @Override
    public SimpleContainer getItemContainer(ItemStack containerStack, Player player, boolean allowSaving) {
        return ContainerItemHelper.loadGenericItemContainer(containerStack, null, this, this.getInventorySize(), allowSaving, this.getNbtKey());
    }

    protected @Nullable CompoundTag getItemSourceTag(ItemStack containerStack, boolean force) {
        return super.getItemTag(containerStack, force);
    }

    @Override
    public final @Nullable CompoundTag getItemTag(ItemStack containerStack, boolean force) {
        CompoundTag tag = this.getItemSourceTag(containerStack, force);
        for (String s : this.getNbtPath()) {
            if (tag != null) {
                if (tag.contains(s, Tag.TAG_COMPOUND)) {
                    tag = tag.getCompound(s);
                } else if (force) {
                    CompoundTag compoundTag = new CompoundTag();
                    tag.put(s, compoundTag);
                    tag = compoundTag;
                } else {
                    tag = null;
                }
            }
        }
        return tag;
    }

    @Override
    public boolean canProvideTooltipImage(ItemStack containerStack, Player player) {
        return ContainerItemHelper.hasItemContainerTag(containerStack, this, this.getNbtKey());
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
        return new GenericItemContainerProvider(inventoryWidth, inventoryHeight, dyeColor, nbtKey);
    }
}
