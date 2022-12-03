package fuzs.easyshulkerboxes.mixin.client;

import fuzs.easyshulkerboxes.api.client.event.MouseDragEvents;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// run before Mouse Tweaks mod
@Mixin(value = MouseHandler.class, priority = 900)
abstract class MouseHandlerMixin$Fabric {
    @Shadow
    private int activeButton;

    @Inject(method = "method_1602(Lnet/minecraft/client/gui/screens/Screen;DDDD)V", at = @At("HEAD"), cancellable = true)
    private void easyshulkerboxes$onMove$0(Screen screen, double mouseX, double mouseY, double dragX, double dragY, CallbackInfo callback) {
        if (MouseDragEvents.BEFORE.invoker().beforeMouseDrag(screen, mouseX, mouseY, this.activeButton, dragX, dragY).isPresent()) {
            callback.cancel();
        }
    }

    @Inject(method = "method_1602(Lnet/minecraft/client/gui/screens/Screen;DDDD)V", at = @At("TAIL"))
    private void easyshulkerboxes$onMove$1(Screen screen, double mouseX, double mouseY, double dragX, double dragY, CallbackInfo callback) {
        // on Forge this only runs when Screen::mouseDragged returns false, but vanilla does not capture the result from that method invocation
        // we can't just call the method ourselves, as that would require replacing the vanilla invocation, which messed with other mods placing their own hook here (namely Mouse Tweaks)
        // so there is no way of knowing if vanilla was successful on Fabric right now
        MouseDragEvents.AFTER.invoker().afterMouseDrag(screen, mouseX, mouseY, this.activeButton, dragX, dragY);
    }
}
