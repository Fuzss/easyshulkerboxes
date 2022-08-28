package fuzs.easyshulkerboxes.mixin.client.accessor;

import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BundleItem.class)
public interface BundleItemAccessor {

    @Invoker
    static int callGetWeight(ItemStack stack) {
        throw new IllegalStateException();
    }

    @Invoker
    static int callGetContentWeight(ItemStack stack) {
        throw new IllegalStateException();
    }
}
