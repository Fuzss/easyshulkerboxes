package fuzs.easyshulkerboxes.world.item.container.helper;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;

public class ItemMoveHelper {

    public static Pair<ItemStack, Integer> addItem(Container container, ItemStack stack, int prioritizedSlot) {
        ItemStack itemStack = stack.copy();
        prioritizedSlot = moveItemToOccupiedSlotsWithSameType(container, itemStack, prioritizedSlot);
        if (itemStack.isEmpty()) {
            itemStack = ItemStack.EMPTY;
        } else {
            prioritizedSlot = moveItemToEmptySlots(container, itemStack, prioritizedSlot);
            if (itemStack.isEmpty()) {
                itemStack = ItemStack.EMPTY;
            }
        }
        return Pair.of(itemStack, prioritizedSlot);
    }

    private static int moveItemToEmptySlots(Container container, ItemStack stack, int prioritizedSlot) {
        prioritizedSlot = setItemInSlot(container, stack, prioritizedSlot);
        if (prioritizedSlot != -1) return prioritizedSlot;
        for (int i = 0; i < container.getContainerSize(); ++i) {
            prioritizedSlot = setItemInSlot(container, stack, i);
            if (prioritizedSlot != -1) return prioritizedSlot;
        }
        return -1;
    }

    private static int setItemInSlot(Container container, ItemStack stack, int slotIndex) {
        if (slotIndex != -1) {
            ItemStack itemStack = container.getItem(slotIndex);
            if (itemStack.isEmpty()) {
                container.setItem(slotIndex, stack.copy());
                stack.setCount(0);
                return slotIndex;
            }
        }
        return -1;
    }

    private static int moveItemToOccupiedSlotsWithSameType(Container container, ItemStack stack, int prioritizedSlot) {
        prioritizedSlot = addItemToSlot(container, stack, prioritizedSlot);
        if (prioritizedSlot != -1) return prioritizedSlot;
        for (int i = 0; i < container.getContainerSize(); ++i) {
            prioritizedSlot = addItemToSlot(container, stack, i);
            if (prioritizedSlot != -1) return prioritizedSlot;
        }
        return -1;
    }

    private static int addItemToSlot(Container container, ItemStack stack, int slotIndex) {
        if (slotIndex != -1) {
            ItemStack itemStack = container.getItem(slotIndex);
            if (ItemStack.isSameItemSameTags(itemStack, stack)) {
                moveItemsBetweenStacks(container, stack, itemStack);
                if (stack.isEmpty()) {
                    return slotIndex;
                }
            }
        }
        return -1;
    }

    private static void moveItemsBetweenStacks(Container container, ItemStack stack, ItemStack other) {
        int i = Math.min(container.getMaxStackSize(), other.getMaxStackSize());
        int j = Math.min(stack.getCount(), i - other.getCount());
        if (j > 0) {
            other.grow(j);
            stack.shrink(j);
            container.setChanged();
        }
    }
}
