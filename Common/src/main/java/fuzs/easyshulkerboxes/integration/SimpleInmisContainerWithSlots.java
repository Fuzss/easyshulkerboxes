package fuzs.easyshulkerboxes.integration;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

/**
 * Inmis mod needs a custom container implementation as it stored items in a separate <code>Stack</code> tag, usually stacks are stored in the same tag as the slot id
 */
public class SimpleInmisContainerWithSlots extends SimpleContainer {

    public SimpleInmisContainerWithSlots(int inventorySize) {
        super(inventorySize);
    }

    @Override
    public void fromTag(ListTag listTag) {
        for (int i = 0; i < this.getContainerSize(); ++i) {
            this.setItem(i, ItemStack.EMPTY);
        }
        for (int k = 0; k < listTag.size(); ++k) {
            CompoundTag compoundtag = listTag.getCompound(k);
            int slot = compoundtag.getInt("Slot");
            if (slot >= 0 && slot < this.getContainerSize()) {
                this.setItem(slot, ItemStack.of(compoundtag.getCompound("Stack")));
            }
        }
    }

    @Override
    public ListTag createTag() {
        ListTag listtag = new ListTag();
        for (int i = 0; i < this.getContainerSize(); ++i) {
            ItemStack itemstack = this.getItem(i);
            if (!itemstack.isEmpty()) {
                CompoundTag compoundtag = new CompoundTag();
                compoundtag.putInt("Slot", i);
                compoundtag.put("Stack", itemstack.save(new CompoundTag()));
                listtag.add(compoundtag);
            }
        }
        return listtag;
    }
}
