package fuzs.easyshulkerboxes.mixin.client;

import fuzs.easyshulkerboxes.api.client.event.MouseDragEvents;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
abstract class MouseHandlerMixin$Fabric {
    @Shadow
    private int activeButton;

    @Inject(method = "method_1602(Lnet/minecraft/client/gui/screens/Screen;DDDD)V", at = @At("HEAD"), cancellable = true)
    private void simpleinventorycontainers$onMove(Screen screen, double mouseX, double mouseY, double dragX, double dragY, CallbackInfo callback) {
        // we need to take over everything to be able to capture the result from Screen::mouseDragged
        callback.cancel();
        if (MouseDragEvents.BEFORE.invoker().beforeMouseDrag(screen, mouseX, mouseY, this.activeButton, dragX, dragY).isPresent()) return;
        if (screen.mouseDragged(mouseX, mouseY, this.activeButton, dragX, dragY)) return;
        MouseDragEvents.AFTER.invoker().afterMouseDrag(screen, mouseX, mouseY, this.activeButton, dragX, dragY);
    }
}
