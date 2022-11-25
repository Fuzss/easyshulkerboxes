package fuzs.easyshulkerboxes.api.config;

import fuzs.puzzleslib.proxy.Proxy;
import net.minecraft.client.Minecraft;

public interface ClientConfigCore {

    boolean colorfulTooltips();

    TooltipContentsActivation tooltipContentsActivation();

    SlotOverlay slotOverlay();

    TooltipContentsActivation selectedItemTooltipActivation();

    enum SlotOverlay {
        NONE, HOTBAR, HOVER
    }

    enum TooltipContentsActivation {
        NEVER(""), ALWAYS(""), SHIFT("SHIFT"), CONTROL(Minecraft.ON_OSX ? "CMD" : "CTRL"), ALT("ALT");

        public final String text;

        TooltipContentsActivation(String text) {
            this.text = text;
        }

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
