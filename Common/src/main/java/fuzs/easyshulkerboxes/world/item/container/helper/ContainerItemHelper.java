package fuzs.easyshulkerboxes.world.item.container.helper;

import fuzs.easyshulkerboxes.api.world.item.container.ItemContainerProvider;
import fuzs.easyshulkerboxes.mixin.client.accessor.BundleItemAccessor;
import fuzs.easyshulkerboxes.world.inventory.SimpleContainerWithSlots;
import fuzs.easyshulkerboxes.world.inventory.helper.ContainerSlotHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public class ContainerItemHelper {
    public static final String TAG_ITEMS = "Items";
    public static final float[] DEFAULT_BACKGROUND_COLOR = {1.0F, 1.0F, 1.0F};

    public static SimpleContainer loadBundleItemContainer(ItemStack stack, ItemContainerProvider provider, boolean allowSaving) {
        // add one additional slot, so we can add items in the inventory
        return loadItemContainer(stack, null, provider, items -> new SimpleContainer(items + 1), allowSaving, TAG_ITEMS);
    }

    public static SimpleContainer loadGenericItemContainer(ItemStack stack, @Nullable BlockEntityType<?> blockEntityType, ItemContainerProvider provider, int containerSize, boolean allowSaving) {
        return loadGenericItemContainer(stack, blockEntityType, provider, containerSize, allowSaving, TAG_ITEMS);
    }

    public static SimpleContainer loadGenericItemContainer(ItemStack stack, @Nullable BlockEntityType<?> blockEntityType, ItemContainerProvider provider, int containerSize, boolean allowSaving, String nbtKey) {
        return loadItemContainer(stack, blockEntityType, provider, items -> new SimpleContainerWithSlots(containerSize), allowSaving, nbtKey);
    }

    private static SimpleContainer loadItemContainer(ItemStack stack, @Nullable BlockEntityType<?> blockEntityType, ItemContainerProvider provider, IntFunction<SimpleContainer> containerFactory, boolean allowSaving, String nbtKey) {
        CompoundTag tag = provider.getItemTag(stack);
        ListTag items = null;
        if (tag != null && tag.contains(nbtKey)) {
            items = tag.getList(nbtKey, 10);
        }
        SimpleContainer simpleContainer = containerFactory.apply(items != null ? items.size() : 0);
        if (items != null) {
            simpleContainer.fromTag(items);
        }
        if (allowSaving) {
            simpleContainer.addListener(container -> {
                saveItemContainer(stack, blockEntityType, (SimpleContainer) container, nbtKey);
            });
        }
        return simpleContainer;
    }

    private static void saveItemContainer(ItemStack stack, @Nullable BlockEntityType<?> blockEntityType, SimpleContainer container, String nbtKey) {
        ListTag listTag = container.createTag();
        if (blockEntityType == null) {
            if (listTag.isEmpty()) {
                stack.removeTagKey(nbtKey);
            } else {
                stack.addTagElement(nbtKey, listTag);
            }
        } else {
            CompoundTag tag = BlockItem.getBlockEntityData(stack);
            if (tag == null) {
                tag = new CompoundTag();
            } else {
                tag.remove(nbtKey);
            }
            if (!listTag.isEmpty()) {
                tag.put(nbtKey, listTag);
            }
            BlockItem.setBlockEntityData(stack, blockEntityType, tag);
        }
    }

    public static boolean overrideStackedOnOther(Supplier<SimpleContainer> supplier, Slot slot, ClickAction clickAction, Player player, ToIntFunction<ItemStack> itemFilter, SoundEvent insertSound, SoundEvent removeSound) {
        if (clickAction != ClickAction.SECONDARY) return false;
        ItemStack hoveredStack = slot.getItem();
        if (hoveredStack.isEmpty()) {
            removeLastStack(supplier, player).ifPresent(stack1 -> {
                player.playSound(removeSound, 0.8F, 0.8F + player.getLevel().getRandom().nextFloat() * 0.4F);
                addStack(supplier, player, slot.safeInsert(stack1), itemFilter);
            });
        } else {
            hoveredStack = slot.safeTake(hoveredStack.getCount(), hoveredStack.getCount(), player);
            int transferredCount = addStack(supplier, player, hoveredStack, itemFilter);
            hoveredStack.shrink(transferredCount);
            if (!hoveredStack.isEmpty()) {
                slot.safeInsert(hoveredStack);
            }
            if (transferredCount > 0) {
                player.playSound(insertSound, 0.8F, 0.8F + player.getLevel().getRandom().nextFloat() * 0.4F);
            }
        }
        return true;
    }

    public static boolean overrideOtherStackedOnMe(Supplier<SimpleContainer> supplier, ItemStack stackOnMe, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess, ToIntFunction<ItemStack> itemFilter, SoundEvent insertSound, SoundEvent removeSound) {
        if (clickAction != ClickAction.SECONDARY || !slot.allowModification(player)) return false;
        if (stackOnMe.isEmpty()) {
            removeLastStack(supplier, player).ifPresent((p_186347_) -> {
                player.playSound(removeSound, 0.8F, 0.8F + player.getLevel().getRandom().nextFloat() * 0.4F);
                slotAccess.set(p_186347_);
            });
        } else {
            int transferredCount = addStack(supplier, player, stackOnMe, itemFilter);
            if (transferredCount > 0) {
                player.playSound(insertSound, 0.8F, 0.8F + player.getLevel().getRandom().nextFloat() * 0.4F);
                stackOnMe.shrink(transferredCount);
            }
        }
        return true;
    }

    private static int addStack(Supplier<SimpleContainer> supplier, Player player, ItemStack newStack, ToIntFunction<ItemStack> itemFilter) {
        if (newStack.isEmpty()) return 0;
        SimpleContainer container = supplier.get();
        ItemStack stackToAdd = newStack.copy();
        stackToAdd.setCount(Math.min(itemFilter.applyAsInt(newStack), newStack.getCount()));
        if (stackToAdd.isEmpty()) return 0;
        ItemStack remainingStack = container.addItem(stackToAdd);
        ContainerSlotHelper.resetCurrentContainerSlot(player);
        return stackToAdd.getCount() - remainingStack.getCount();
    }

    private static Optional<ItemStack> removeLastStack(Supplier<SimpleContainer> supplier, Player player) {
        SimpleContainer container = supplier.get();
        return findSlotWithContent(container, player).stream().mapToObj(index -> container.removeItem(index, container.getItem(index).getCount())).findAny();
    }

    private static OptionalInt findSlotWithContent(SimpleContainer container, Player player) {
        int currentContainerSlot = ContainerSlotHelper.getCurrentContainerSlot(player);
        if (currentContainerSlot >= 0 && currentContainerSlot < container.getContainerSize()) {
            if (!container.getItem(currentContainerSlot).isEmpty()) {
                ContainerSlotHelper.cycleCurrentSlotBackwards(player, container);
                return OptionalInt.of(currentContainerSlot);
            }
        }
        for (int i = container.getContainerSize() - 1; i >= 0; i--) {
            if (!container.getItem(i).isEmpty()) {
                ContainerSlotHelper.resetCurrentContainerSlot(player);
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }

    public static boolean hasItemContainerTag(ItemStack stack, ItemContainerProvider provider) {
        return hasItemContainerTag(stack, provider, TAG_ITEMS);
    }

    public static boolean hasItemContainerTag(ItemStack stack, ItemContainerProvider provider, String nbtKey) {
        CompoundTag tag = provider.getItemTag(stack);
        return tag != null && tag.contains(nbtKey);
    }

    public static NonNullList<ItemStack> containerToList(SimpleContainer container) {
        NonNullList<ItemStack> items = NonNullList.create();
        for (int i = 0; i < container.getContainerSize(); i++) {
            items.add(container.getItem(i));
        }
        return items;
    }

    public static float[] getBackgroundColor(@Nullable DyeColor backgroundColor) {
        if (backgroundColor == null) {
            return DEFAULT_BACKGROUND_COLOR;
        } else if (backgroundColor == DyeColor.WHITE) {
            return new float[]{0.9019608F, 0.9019608F, 0.9019608F};
        } else {
            return backgroundColor.getTextureDiffuseColors();
        }
    }

    public static int getAvailableBundleItemSpace(ItemStack containerStack, ItemStack stackToAdd, int maxWeight) {
        int weight = BundleItemAccessor.easyshulkerboxes$getWeight(stackToAdd);
        // fix java.lang.ArithmeticException: / by zero from Numismatic Overhaul as their coins stack to 99 instead of 64
        return weight > 0 ? (maxWeight - BundleItemAccessor.easyshulkerboxes$getContentWeight(containerStack)) / weight : 0;
    }
}
