package fuzs.easyshulkerboxes.mixin;

import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import net.minecraft.network.FriendlyByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(FriendlyByteBuf.class)
abstract class FriendlyByteBufMixin {

    @Inject(method = "readVarIntArray(I)[I", at = @At(value = "NEW", target = "io/netty/handler/codec/DecoderException"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void readVarIntArray(int maxLength, CallbackInfoReturnable<int[]> callback, int i) {
        EasyShulkerBoxes.LOGGER.error("", new RuntimeException("VarIntArray with size " + i + " is bigger than allowed " + maxLength + " (diff: %s)".formatted(i - maxLength)));
    }
}
