package fuzs.easyshulkerboxes.world.item.container.helper;

import fuzs.easyshulkerboxes.api.world.item.container.ItemContainerProvider;
import fuzs.easyshulkerboxes.mixin.accessor.BundleItemAccessor;
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
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public class ContainerItemHelper {
    public static final String TAG_ITEMS = "Items";

    public static SimpleContainer loadItemContainer(ItemStack stack, ItemContainerProvider provider, int inventorySize, boolean allowSaving, String nbtKey) {
        return loadItemContainer(stack, provider, items -> new SimpleContainerWithSlots(inventorySize), allowSaving, nbtKey);
    }

    public static SimpleContainer loadItemContainer(ItemStack stack, ItemContainerProvider provider, IntFunction<SimpleContainer> containerFactory, boolean allowSaving, String nbtKey) {
        CompoundTag tag = provider.getItemContainerData(stack);
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
                ListTag itemsTag = ((SimpleContainer) container).createTag();
                provider.setItemContainerData(stack, itemsTag, nbtKey);
            });
        }
        return simpleContainer;
    }

    public static boolean overrideStackedOnOther(Supplier<SimpleContainer> supplier, Slot slot, ClickAction clickAction, Player player, ToIntFunction<ItemStack> acceptableItemCount, SoundEvent insertSound, SoundEvent removeSound) {
        if (clickAction != ClickAction.SECONDARY) return false;
        ItemStack stackInSlot = slot.getItem();
        boolean extractSingleItemOnly = ContainerSlotHelper.extractSingleItemOnly(player);
        if (stackInSlot.isEmpty() || extractSingleItemOnly) {
            ToIntFunction<ItemStack> amountToRemove = extractSingleItemOnly ? stack -> 1 : ItemStack::getCount;
            removeLastStack(supplier, player, removedStack -> {
                return stackInSlot.isEmpty() || ItemStack.isSameItemSameTags(stackInSlot, removedStack) && removedStack.getCount() <= stackInSlot.getMaxStackSize() - stackInSlot.getCount();
            }, amountToRemove).ifPresent(removedStack -> {
                addStack(supplier, player, slot.safeInsert(removedStack), acceptableItemCount);
                player.playSound(removeSound, 0.8F, 0.8F + player.getLevel().getRandom().nextFloat() * 0.4F);
            });
        } else {
            ItemStack hoveredStack = slot.safeTake(stackInSlot.getCount(), stackInSlot.getCount(), player);
            int transferredCount = addStack(supplier, player, hoveredStack, acceptableItemCount);
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

    public static boolean overrideOtherStackedOnMe(Supplier<SimpleContainer> supplier, ItemStack stackOnMe, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess, ToIntFunction<ItemStack> acceptableItemCount, SoundEvent insertSound, SoundEvent removeSound) {
        if (!slot.allowModification(player)) return false;
        boolean extractSingleItemOnly = ContainerSlotHelper.extractSingleItemOnly(player);
        if (clickAction == ClickAction.SECONDARY && (stackOnMe.isEmpty() || extractSingleItemOnly)) {
            ToIntFunction<ItemStack> amountToRemove = extractSingleItemOnly ? stack -> 1 : ItemStack::getCount;
            Predicate<ItemStack> itemFilter = stackInSlot -> {
                if (!stackOnMe.isEmpty()) {
                    if (ItemStack.isSameItemSameTags(stackOnMe, stackInSlot)) {
                        int remainingCapacity = stackOnMe.getMaxStackSize() - stackOnMe.getCount();
                        return amountToRemove.applyAsInt(stackInSlot) <= remainingCapacity;
                    }
                    return false;
                }
                return true;
            };
            removeLastStack(supplier, player, itemFilter, amountToRemove).ifPresent(extractedStack -> {
                if (stackOnMe.isEmpty()) {
                    slotAccess.set(extractedStack);
                } else {
                    ItemStack hoveredStack = slotAccess.get();
                    hoveredStack.grow(extractedStack.getCount());
                    slotAccess.set(hoveredStack);
                }
                player.playSound(removeSound, 0.8F, 0.8F + player.getLevel().getRandom().nextFloat() * 0.4F);
            });
            return true;
        } else if (clickAction == ClickAction.SECONDARY || extractSingleItemOnly) {
            if (clickAction == ClickAction.PRIMARY) acceptableItemCount = stack -> 1;
            int transferredCount = addStack(supplier, player, stackOnMe, acceptableItemCount);
            if (transferredCount > 0) {
                stackOnMe.shrink(transferredCount);
                player.playSound(insertSound, 0.8F, 0.8F + player.getLevel().getRandom().nextFloat() * 0.4F);
            }
            return true;
        }
        return false;
    }

    private static int addStack(Supplier<SimpleContainer> supplier, Player player, ItemStack newStack, ToIntFunction<ItemStack> acceptableItemCount) {
        if (newStack.isEmpty()) return 0;
        SimpleContainer container = supplier.get();
        ItemStack stackToAdd = newStack.copy();
        stackToAdd.setCount(Math.min(acceptableItemCount.applyAsInt(newStack), newStack.getCount()));
        if (stackToAdd.isEmpty()) return 0;
        Pair<ItemStack, Integer> result = ItemMoveHelper.addItem(container, stackToAdd, ContainerSlotHelper.getCurrentContainerSlot(player));
        ContainerSlotHelper.setCurrentContainerSlot(player, result.getRight());
        return stackToAdd.getCount() - result.getLeft().getCount();
    }

    private static Optional<ItemStack> removeLastStack(Supplier<SimpleContainer> containerSupplier, Player player, Predicate<ItemStack> itemFilter, ToIntFunction<ItemStack> amountToRemove) {
        SimpleContainer container = containerSupplier.get();
        OptionalInt slotWithContent = findSlotWithContent(container, player, itemFilter, amountToRemove);
        return slotWithContent.stream().mapToObj(index -> {
            int amount = amountToRemove.applyAsInt(container.getItem(index));
            return container.removeItem(index, amount);
        }).findAny();
    }

    private static OptionalInt findSlotWithContent(SimpleContainer container, Player player, Predicate<ItemStack> itemFilter, ToIntFunction<ItemStack> amountToRemove) {
        int currentContainerSlot = ContainerSlotHelper.getCurrentContainerSlot(player);
        if (currentContainerSlot >= 0 && currentContainerSlot < container.getContainerSize()) {
            ItemStack stackInSlot = container.getItem(currentContainerSlot);
            if (!stackInSlot.isEmpty() && itemFilter.test(stackInSlot)) {
                // did we empty the slot, so cycle to a different one
                if (stackInSlot.getCount() <= amountToRemove.applyAsInt(stackInSlot)) {
                    ContainerSlotHelper.cycleCurrentSlotBackwards(player, container);
                }
                return OptionalInt.of(currentContainerSlot);
            }
        }
        for (int i = container.getContainerSize() - 1; i >= 0; i--) {
            ItemStack stackInSlot = container.getItem(i);
            if (!stackInSlot.isEmpty() && itemFilter.test(stackInSlot)) {
                // did we empty the slot, so cycle to a different one
                if (stackInSlot.getCount() <= amountToRemove.applyAsInt(stackInSlot)) {
                    ContainerSlotHelper.resetCurrentContainerSlot(player);
                } else {
                    // otherwise if not empty make sure this is the new current slot
                    ContainerSlotHelper.setCurrentContainerSlot(player, i);
                }
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }

    public static boolean hasItemContainerTag(ItemStack stack, ItemContainerProvider provider, String nbtKey) {
        CompoundTag tag = provider.getItemContainerData(stack);
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
            return new float[]{1.0F, 1.0F, 1.0F};
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
