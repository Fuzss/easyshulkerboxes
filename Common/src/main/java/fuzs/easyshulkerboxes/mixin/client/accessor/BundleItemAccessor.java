package fuzs.easyshulkerboxes.mixin.client.accessor;

import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BundleItem.class)
public interface BundleItemAccessor {

    @Invoker("getWeight")
    static int simpleinventorycontainers$getWeight(ItemStack stack) {
        throw new IllegalStateException();
    }

    @Invoker("getContentWeight")
    static int simpleinventorycontainers$getContentWeight(ItemStack stack) {
        throw new IllegalStateException();
    }
}
