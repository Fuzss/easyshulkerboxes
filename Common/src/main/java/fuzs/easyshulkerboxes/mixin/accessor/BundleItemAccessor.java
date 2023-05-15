package fuzs.easyshulkerboxes.mixin.accessor;

import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BundleItem.class)
public interface BundleItemAccessor {

    @Invoker("getWeight")
    static int easyshulkerboxes$getWeight(ItemStack stack) {
        throw new IllegalStateException();
    }

    @Invoker("getContentWeight")
    static int easyshulkerboxes$getContentWeight(ItemStack stack) {
        throw new IllegalStateException();
    }
}
