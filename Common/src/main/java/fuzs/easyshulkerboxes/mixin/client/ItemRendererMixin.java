package fuzs.easyshulkerboxes.mixin.client;

import fuzs.easyshulkerboxes.client.helper.ItemDecorationHelper;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
abstract class ItemRendererMixin {
    @Shadow
    public float blitOffset;

    @Inject(method = "renderGuiItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At("TAIL"))
    public void renderGuiItemDecorations(Font fr, ItemStack stack, int xPosition, int yPosition, @Nullable String text, CallbackInfo callback) {
        if (!stack.isEmpty()) ItemDecorationHelper.render(fr, stack, xPosition, yPosition, this.blitOffset);
    }
}
