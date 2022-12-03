package fuzs.easyshulkerboxes.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(ShulkerBoxBlock.class)
abstract class ShulkerBoxBlockMixin extends BaseEntityBlock {
    protected ShulkerBoxBlockMixin(Properties p_49224_) {
        super(p_49224_);
    }

    @Inject(method = "appendHoverText", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundTag;contains(Ljava/lang/String;I)Z"), slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/Component;literal(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;")), cancellable = true)
    public void easyshulkerboxes$appendHoverText(ItemStack p_56193_, @Nullable BlockGetter p_56194_, List<Component> p_56195_, TooltipFlag p_56196_, CallbackInfo callbackInfo) {
        callbackInfo.cancel();
    }
}
