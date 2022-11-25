package fuzs.easyshulkerboxes.api.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

/**
 * callbacks injected in the render method of all container screens to allow for rendering on background and foreground layers
 */
public class ContainerScreenEvents {
    public static final Event<Background> BACKGROUND = EventFactory.createArrayBacked(Background.class, listeners -> (AbstractContainerScreen<?> screen, PoseStack poseStack, int mouseX, int mouseY) -> {
        for (Background event : listeners) {
            event.onDrawBackground(screen, poseStack, mouseX, mouseY);
        }
    });
    public static final Event<Foreground> FOREGROUND = EventFactory.createArrayBacked(Foreground.class, listeners -> (AbstractContainerScreen<?> screen, PoseStack poseStack, int mouseX, int mouseY) -> {
        for (Foreground event : listeners) {
            event.onDrawForeground(screen, poseStack, mouseX, mouseY);
        }
    });

    @FunctionalInterface
    public interface Background {

        /**
         * called after the screen background is drawn (like menu texture)
         * this event is simply for notifying the background has been drawn, nothing can be cancelled
         *
         * @param screen    the screen being drawn
         * @param poseStack pose stack
         * @param mouseX    mouse x position
         * @param mouseY    mouse y position
         */
        void onDrawBackground(AbstractContainerScreen<?> screen, PoseStack poseStack, int mouseX, int mouseY);
    }

    @FunctionalInterface
    public interface Foreground {

        /**
         * called after the screen foreground is drawn (like text labels)
         * this event is simply for notifying the foreground has been drawn, nothing can be cancelled
         *
         * @param screen    the screen being drawn
         * @param poseStack pose stack
         * @param mouseX    mouse x position
         * @param mouseY    mouse y position
         */
        void onDrawForeground(AbstractContainerScreen<?> screen, PoseStack poseStack, int mouseX, int mouseY);
    }
}
