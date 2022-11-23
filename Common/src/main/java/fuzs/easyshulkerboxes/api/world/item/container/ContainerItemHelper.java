package fuzs.easyshulkerboxes.api.world.item.container;

import fuzs.easyshulkerboxes.api.world.inventory.ContainerSlotHelper;
import fuzs.easyshulkerboxes.api.world.inventory.SimpleContainerWithSlots;
import fuzs.easyshulkerboxes.api.world.inventory.tooltip.ContainerItemTooltip;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ContainerItemHelper {

    public static SimpleContainer loadItemContainer(ItemStack stack, @Nullable BlockEntityType<?> blockEntityType, int containerRows) {
        return loadItemContainer(stack, blockEntityType, containerRows, true);
    }

    public static SimpleContainer loadItemContainer(ItemStack stack, @Nullable BlockEntityType<?> blockEntityType, int containerRows, boolean allowSaving) {
        CompoundTag compoundtag = getDataTagFromItem(stack, blockEntityType);
        SimpleContainer simpleContainer = new SimpleContainerWithSlots(containerRows);
        if (compoundtag != null && compoundtag.contains("Items")) {
            simpleContainer.fromTag(compoundtag.getList("Items", 10));
        }
        if (allowSaving) {
            simpleContainer.addListener(container -> {
                saveItemContainer(stack, blockEntityType, (SimpleContainer) container);
            });
        }
        return simpleContainer;
    }

    private static void saveItemContainer(ItemStack stack, @Nullable BlockEntityType<?> blockEntityType, SimpleContainer container) {
        ListTag listTag = container.createTag();
        if (blockEntityType == null) {
            if (listTag.isEmpty()) {
                stack.removeTagKey("Items");
            } else {
                stack.addTagElement("Items", listTag);
            }
        } else {
            CompoundTag tag = BlockItem.getBlockEntityData(stack);
            if (tag == null) tag = new CompoundTag();
            if (!listTag.isEmpty()) {
                tag.remove("Items");
                tag.put("Items", listTag);
            }
            BlockItem.setBlockEntityData(stack, blockEntityType, tag);
        }
    }

    public static boolean overrideStackedOnOther(ItemStack stack, @Nullable BlockEntityType<?> blockEntityType, int containerRows, Slot slot, ClickAction clickAction, Player player, Predicate<ItemStack> itemFilter, SoundEvent insertSound, SoundEvent removeSound) {
        return overrideStackedOnOther(() -> loadItemContainer(stack, blockEntityType, containerRows), slot, clickAction, player, itemFilter, insertSound, removeSound);
    }

    public static boolean overrideStackedOnOther(Supplier<SimpleContainer> supplier, Slot slot, ClickAction clickAction, Player player, Predicate<ItemStack> itemFilter, SoundEvent insertSound, SoundEvent removeSound) {
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

    public static boolean overrideOtherStackedOnMe(ItemStack stack, @Nullable BlockEntityType<?> blockEntityType, int containerRows, ItemStack stackOnMe, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess, Predicate<ItemStack> itemFilter, SoundEvent insertSound, SoundEvent removeSound) {
        return overrideOtherStackedOnMe(() -> loadItemContainer(stack, blockEntityType, containerRows), stackOnMe, slot, clickAction, player, slotAccess, itemFilter, insertSound, removeSound);
    }

    public static boolean overrideOtherStackedOnMe(Supplier<SimpleContainer> supplier, ItemStack stackOnMe, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess, Predicate<ItemStack> itemFilter, SoundEvent insertSound, SoundEvent removeSound) {
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

    private static int addStack(Supplier<SimpleContainer> supplier, Player player, ItemStack newStack, Predicate<ItemStack> itemFilter) {
        if (newStack.isEmpty() || !itemFilter.test(newStack)) return 0;
        SimpleContainer container = supplier.get();
        ItemStack remainingStack = container.addItem(newStack);
        ContainerSlotHelper.resetCurrentContainerSlot(player);
        return newStack.getCount() - remainingStack.getCount();
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

    public static Optional<TooltipComponent> getTooltipImage(ItemStack stack, @Nullable BlockEntityType<?> blockEntityType, int containerRows, @Nullable DyeColor backgroundColor) {
        CompoundTag compoundtag = getDataTagFromItem(stack, blockEntityType);
        if (compoundtag == null || !compoundtag.contains("Items")) {
            return Optional.empty();
        }
        SimpleContainer container = loadItemContainer(stack, blockEntityType, containerRows, false);
        return getTooltipImage(container, containerRows, backgroundColor);
    }

    public static Optional<TooltipComponent> getTooltipImage(SimpleContainer container, int containerRows, @Nullable DyeColor backgroundColor) {
        NonNullList<ItemStack> items = NonNullList.create();
        for (int i = 0; i < container.getContainerSize(); i++) {
            items.add(container.getItem(i));
        }
        return Optional.of(new ContainerItemTooltip(items, 9, containerRows, backgroundColor));
    }

    @Nullable
    private static CompoundTag getDataTagFromItem(ItemStack stack, @Nullable BlockEntityType<?> blockEntityType) {
        return blockEntityType != null ? BlockItem.getBlockEntityData(stack) : stack.getTag();
    }
}
