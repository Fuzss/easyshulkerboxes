package fuzs.easyshulkerboxes.api.world.inventory;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

/**
 * copied from ender chest, as over there only difference from simple container is that slot ids are saved along with items
 */
public class SimpleContainerWithSlots extends SimpleContainer {

    public SimpleContainerWithSlots(int size) {
        super(size);
    }

    @Override
    public void fromTag(ListTag listTag) {
        for (int i = 0; i < this.getContainerSize(); ++i) {
            this.setItem(i, ItemStack.EMPTY);
        }
        for (int k = 0; k < listTag.size(); ++k) {
            CompoundTag compoundtag = listTag.getCompound(k);
            int j = compoundtag.getByte("Slot") & 255;
            if (j >= 0 && j < this.getContainerSize()) {
                this.setItem(j, ItemStack.of(compoundtag));
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
                compoundtag.putByte("Slot", (byte) i);
                itemstack.save(compoundtag);
                listtag.add(compoundtag);
            }
        }
        return listtag;
    }
}
