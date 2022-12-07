package fuzs.easyshulkerboxes.world.item.container;

import com.google.gson.JsonObject;
import fuzs.easyshulkerboxes.world.item.container.helper.ContainerItemHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;

public abstract class NestedTagItemProvider extends AbstractItemContainerProvider {
    private static final float[] DEFAULT_BACKGROUND_COLOR = {1.0F, 1.0F, 1.0F};

    @Nullable
    private final DyeColor dyeColor;
    private final float[] backgroundColor;
    private final String[] nbtKey;

    public NestedTagItemProvider(@Nullable DyeColor dyeColor, String... nbtKey) {
        this.dyeColor = dyeColor;
        this.backgroundColor = dyeColor == null ? this.getDefaultBackgroundColor() : ContainerItemHelper.getBackgroundColor(dyeColor);
        this.nbtKey = nbtKey.length == 0 ? new String[]{ContainerItemHelper.TAG_ITEMS} : nbtKey;
    }

    protected float[] getDefaultBackgroundColor() {
        return DEFAULT_BACKGROUND_COLOR;
    }

    public float[] getBackgroundColor() {
        return this.backgroundColor;
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
    public void toJson(JsonObject jsonObject) {
        super.toJson(jsonObject);
        if (this.dyeColor != null) {
            jsonObject.addProperty("background_color", this.dyeColor.getName());
        }
        if (this.nbtKey.length != 1 || !this.nbtKey[0].equals(ContainerItemHelper.TAG_ITEMS)) {
            jsonObject.addProperty("nbt_key", String.join("/", this.nbtKey));
        }
    }
}
