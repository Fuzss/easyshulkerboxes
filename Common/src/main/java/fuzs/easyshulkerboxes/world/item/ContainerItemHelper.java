package fuzs.easyshulkerboxes.world.item;

import fuzs.easyshulkerboxes.world.inventory.SimpleContainerWithSlots;
import fuzs.easyshulkerboxes.world.inventory.tooltip.ContainerItemTooltip;
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
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ContainerItemHelper {

    public static SimpleContainer loadItemContainer(ItemStack stack, @Nullable BlockEntityType<?> blockEntityType, int containerRows) {
        CompoundTag compoundtag = getDataTagFromItem(stack, blockEntityType);
        SimpleContainer simpleContainer = new SimpleContainerWithSlots(containerRows);
        if (compoundtag != null && compoundtag.contains("Items")) {
            simpleContainer.fromTag(compoundtag.getList("Items", 10));
        }
        simpleContainer.addListener(container -> {
            saveItemContainer(stack, blockEntityType, (SimpleContainer) container);
        });
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
            CompoundTag tag = new CompoundTag();
            if (!listTag.isEmpty()) {
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
            removeLastStack(supplier).ifPresent(stack1 -> {
                player.playSound(removeSound, 0.8F, 0.8F + player.getLevel().getRandom().nextFloat() * 0.4F);
                addStack(supplier, slot.safeInsert(stack1), itemFilter);
            });
        } else {
            hoveredStack = slot.safeTake(hoveredStack.getCount(), hoveredStack.getCount(), player);
            int transferredCount = addStack(supplier, hoveredStack, itemFilter);
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
            removeLastStack(supplier).ifPresent((p_186347_) -> {
                player.playSound(removeSound, 0.8F, 0.8F + player.getLevel().getRandom().nextFloat() * 0.4F);
                slotAccess.set(p_186347_);
            });
        } else {
            int transferredCount = addStack(supplier, stackOnMe, itemFilter);
            if (transferredCount > 0) {
                player.playSound(insertSound, 0.8F, 0.8F + player.getLevel().getRandom().nextFloat() * 0.4F);
                stackOnMe.shrink(transferredCount);
            }
        }
        return true;
    }

    private static int addStack(Supplier<SimpleContainer> supplier, ItemStack newStack, Predicate<ItemStack> itemFilter) {
        if (newStack.isEmpty() || !itemFilter.test(newStack)) return 0;
        SimpleContainer container = supplier.get();
        ItemStack remainingStack = container.addItem(newStack);
        return newStack.getCount() - remainingStack.getCount();
    }

    private static Optional<ItemStack> removeLastStack(Supplier<SimpleContainer> supplier) {
        SimpleContainer container = supplier.get();
        for (int i = container.getContainerSize() - 1; i >= 0; i--) {
            ItemStack stack1 = container.getItem(i);
            if (!stack1.isEmpty()) {
                return Optional.of(container.removeItem(i, stack1.getCount()));
            }
        }
        return Optional.empty();
    }

    public static Optional<TooltipComponent> getTooltipImage(ItemStack stack, @Nullable BlockEntityType<?> blockEntityType, int containerRows, @Nullable DyeColor backgroundColor) {
        CompoundTag compoundtag = getDataTagFromItem(stack, blockEntityType);
        if (compoundtag == null || !compoundtag.contains("Items")) {
            return Optional.empty();
        }
        SimpleContainer container = loadItemContainer(stack, blockEntityType, containerRows);
        return getTooltipImage(container, containerRows, backgroundColor);
    }

    public static Optional<TooltipComponent> getTooltipImage(SimpleContainer container, int containerRows, @Nullable DyeColor backgroundColor) {
        NonNullList<ItemStack> items = NonNullList.create();
        for (int i = 0; i < container.getContainerSize(); i++) {
            items.add(container.getItem(i));
        }
        return Optional.of(new ContainerItemTooltip(items, containerRows, backgroundColor));
    }

    @Nullable
    private static CompoundTag getDataTagFromItem(ItemStack stack, @Nullable BlockEntityType<?> blockEntityType) {
        if (blockEntityType != null) return BlockItem.getBlockEntityData(stack);
        return stack.getTag();
    }
}
