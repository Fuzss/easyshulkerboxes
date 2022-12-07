package fuzs.easyshulkerboxes.world.item.container;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fuzs.easyshulkerboxes.world.item.container.helper.ContainerItemHelper;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class NestedTagItemProvider extends AbstractItemContainerProvider {
    @Nullable
    private final DyeColor dyeColor;
    private final float[] backgroundColor;
    private final String[] nbtKey;
    private final List<Value> values = Lists.newArrayList();
    @Nullable
    private Set<Item> disallowedItems;

    public NestedTagItemProvider(@Nullable DyeColor dyeColor, String... nbtKey) {
        this.dyeColor = dyeColor;
        this.backgroundColor = ContainerItemHelper.getBackgroundColor(dyeColor);
        this.nbtKey = nbtKey.length == 0 ? new String[]{ContainerItemHelper.TAG_ITEMS} : nbtKey;
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

    public NestedTagItemProvider disallowValues(Collection<String> value) {
        for (String s : value) {
            this.disallowValue(s);
        }
        return this;
    }

    public NestedTagItemProvider disallowValue(String value) {
        boolean tag = value.startsWith("#");
        if (tag) value = value.substring(1);
        ResourceLocation id = ResourceLocation.tryParse(value);
        Objects.requireNonNull(id, "invalid resource location '%s'".formatted(value));
        Value valueObj = tag ? new TagValue(id) : new ItemValue(id);
        this.values.add(valueObj);
        return this;
    }

    @Override
    public boolean isItemAllowedInContainer(ItemStack containerStack, ItemStack stackToAdd) {
        if (this.disallowedItems == null) {
            this.disallowedItems = this.values.stream()
                    .flatMap(value -> value.getItems().stream())
                    .collect(ImmutableSet.toImmutableSet());
        }
        return !this.disallowedItems.contains(stackToAdd.getItem());
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
        if (this.dyeColor != null) {
            jsonObject.addProperty("background_color", this.dyeColor.getName());
        }
        if (this.nbtKey.length != 1 || !this.nbtKey[0].equals(ContainerItemHelper.TAG_ITEMS)) {
            jsonObject.addProperty("nbt_key", String.join("/", this.nbtKey));
        }
        if (!this.values.isEmpty()) {
            JsonArray jsonArray = new JsonArray();
            for (Value value : this.values) {
                jsonArray.add(value.getValue());
            }
            jsonObject.add("disallowed_items", jsonArray);
        }
    }

    private interface Value {

        Collection<Item> getItems();

        String getValue();
    }

    private static class ItemValue implements Value {
        private final ResourceLocation value;
        @Nullable
        private final Item item;

        public ItemValue(ResourceLocation value) {
            this.value = value;
            this.item = Registry.ITEM.containsKey(this.value) ? Registry.ITEM.get(this.value) : null;
        }

        @Override
        public Collection<Item> getItems() {
            if (this.item == null) return Collections.emptySet();
            return Collections.singleton(this.item);
        }

        @Override
        public String getValue() {
            return this.value.toString();
        }
    }

    private static class TagValue implements Value {
        private final ResourceLocation value;
        private final TagKey<Item> tag;

        public TagValue(ResourceLocation value) {
            this.value = value;
            this.tag = TagKey.create(Registry.ITEM_REGISTRY, this.value);
        }

        @Override
        public Collection<Item> getItems() {
            List<Item> list = Lists.newArrayList();
            for (Holder<Item> holder : Registry.ITEM.getTagOrEmpty(this.tag)) {
                list.add(holder.value());
            }
            return list;
        }

        @Override
        public String getValue() {
            return "#" + this.value.toString();
        }
    }
}
