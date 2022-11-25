package fuzs.easyshulkerboxes.api.client.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.Unit;

import java.util.Optional;

public final class MouseDragEvents {
    public static final Event<Before> BEFORE = EventFactory.createArrayBacked(Before.class, listeners -> (Screen screen, double mouseX, double mouseY, int button, double dragX, double dragY) -> {
        for (Before event : listeners) {
            if (event.beforeMouseDrag(screen, mouseX, mouseY, button, dragX, dragY).isPresent()) {
                return Optional.of(Unit.INSTANCE);
            }
        }
        return Optional.empty();
    });
    public static final Event<After> AFTER = EventFactory.createArrayBacked(After.class, listeners -> (Screen screen, double mouseX, double mouseY, int button, double dragX, double dragY) -> {
        for (After event : listeners) {
            event.afterMouseDrag(screen, mouseX, mouseY, button, dragX, dragY);
        }
    });

    private MouseDragEvents() {

    }

    @FunctionalInterface
    public interface Before {

        /**
         * runs just before vanilla processing is done for dragging the mouse cursor (moving the cursor while a mouse button is held down)
         * <p>allows for cancelling vanilla
         *
         * @param screen the currently displayed screen
         * @param mouseX mouse x position
         * @param mouseY mouse y position
         * @param button mouse button that was clicked
         * @param dragX  how far the cursor has been dragged since last calling this on x
         * @param dragY  how far the cursor has been dragged since last calling this on y
         * @return       cancel vanilla behavior
         */
        Optional<Unit> beforeMouseDrag(Screen screen, double mouseX, double mouseY, int button, double dragX, double dragY);
    }

    @FunctionalInterface
    public interface After {

        /**
         * runs just after vanilla processing is done for dragging the mouse cursor (moving the cursor while a mouse button is held down)
         *
         * @param screen the currently displayed screen
         * @param mouseX mouse x position
         * @param mouseY mouse y position
         * @param button mouse button that was clicked
         * @param dragX  how far the cursor has been dragged since last calling this on x
         * @param dragY  how far the cursor has been dragged since last calling this on y
         */
        void afterMouseDrag(Screen screen, double mouseX, double mouseY, int button, double dragX, double dragY);
    }
}
