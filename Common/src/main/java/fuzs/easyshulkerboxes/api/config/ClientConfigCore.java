package fuzs.easyshulkerboxes.api.config;

import fuzs.puzzleslib.proxy.Proxy;

public interface ClientConfigCore {

    boolean colorfulTooltips();

    TooltipContentsActivation tooltipContentsActivation();

    SlotOverlay slotOverlay();

    SelectedItemTooltipActivation selectedItemTooltipActivation();

    enum SlotOverlay {
        NONE, HOTBAR, HOVER
    }

    enum TooltipContentsActivation {
        ALWAYS, SHIFT, CONTROL, ALT;

        public boolean isActive() {
            return switch (this) {
                case ALWAYS -> true;
                case SHIFT -> Proxy.INSTANCE.hasShiftDown();
                case ALT -> Proxy.INSTANCE.hasAltDown();
                case CONTROL -> Proxy.INSTANCE.hasControlDown();
            };
        }
    }

    enum SelectedItemTooltipActivation {
        NEVER, ALWAYS, SHIFT, CONTROL, ALT;

        public boolean isActive() {
            return switch (this) {
                case NEVER -> false;
                case ALWAYS -> true;
                case SHIFT -> Proxy.INSTANCE.hasShiftDown();
                case ALT -> Proxy.INSTANCE.hasAltDown();
                case CONTROL -> Proxy.INSTANCE.hasControlDown();
            };
        }
    }
}
