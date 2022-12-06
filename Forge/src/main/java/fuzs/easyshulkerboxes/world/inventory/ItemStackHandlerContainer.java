package fuzs.easyshulkerboxes.world.inventory;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * a wrapper for Forge's {@link ItemStackHandler} to make it compatible with vanilla's {@link net.minecraft.world.Container} implementation
 * untested right now
 */
public class ItemStackHandlerContainer extends SimpleContainer {
    private final ItemStackHandler handler;

    public ItemStackHandlerContainer(ItemStackHandler handler) {
        super(handler.getSlots());
        this.handler = handler;
    }

    @Override
    public int getContainerSize() {
        return this.handler.getSlots();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < this.getContainerSize(); i++) {
            if (!this.getItem(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int pSlot) {
        return this.handler.getStackInSlot(pSlot);
    }

    @Override
    public ItemStack removeItem(int pSlot, int pAmount) {
        ItemStack stack = this.handler.extractItem(pSlot, pAmount, false);
        if (!stack.isEmpty()) this.setChanged();
        return stack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack itemStack = this.getItem(slot);
        if (itemStack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            this.setItemNoUpdate(slot, ItemStack.EMPTY);
            return itemStack;
        }
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.setItemNoUpdate(slot, stack);
        this.setChanged();
    }

    private void setItemNoUpdate(int slot, ItemStack stack) {
        this.handler.setStackInSlot(slot, stack);
        if (!stack.isEmpty() && stack.getCount() > this.handler.getSlotLimit(slot)) {
            stack.setCount(this.handler.getSlotLimit(slot));
        }
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < this.getContainerSize(); i++) {
            this.setItemNoUpdate(i, ItemStack.EMPTY);
        }
        this.setChanged();
    }

    @Override
    public boolean canPlaceItem(int pIndex, ItemStack pStack) {
        return this.handler.isItemValid(pIndex, pStack);
    }

    private List<ItemStack> items() {
        return IntStream.range(0, this.handler.getSlots()).mapToObj(this.handler::getStackInSlot).toList();
    }

    @Override
    public List<ItemStack> removeAllItems() {
        List<ItemStack> list = this.items().stream().filter((p_19197_) -> {
            return !p_19197_.isEmpty();
        }).collect(Collectors.toList());
        this.clearContent();
        return list;
    }

    @Override
    public boolean canAddItem(ItemStack pStack) {
        boolean flag = false;

        for (ItemStack itemstack : this.items()) {
            if (itemstack.isEmpty() || ItemStack.isSameItemSameTags(itemstack, pStack) && itemstack.getCount() < itemstack.getMaxStackSize()) {
                flag = true;
                break;
            }
        }

        return flag;
    }

    @Override
    public void fillStackedContents(StackedContents pHelper) {
        for(ItemStack itemstack : this.items()) {
            pHelper.accountStack(itemstack);
        }

    }

    @Override
    public String toString() {
        return this.items().stream().filter((p_19194_) -> {
            return !p_19194_.isEmpty();
        }).toList().toString();
    }

    @Override
    public void fromTag(ListTag listTag) {
        CompoundTag tag = new CompoundTag();
        tag.put("Items", listTag);
        tag.putInt("Size", listTag.size());
        this.handler.deserializeNBT(tag);
    }

    @Override
    public ListTag createTag() {
        return this.handler.serializeNBT().getList("Items", Tag.TAG_COMPOUND);
    }
}
