package fuzs.easyshulkerboxes.config;

import fuzs.easyshulkerboxes.api.config.ClientConfigCore;
import fuzs.puzzleslib.config.ConfigCore;
import fuzs.puzzleslib.config.annotation.Config;

public class ClientConfig implements ConfigCore, ClientConfigCore {
    @Config(description = "Color item inventories on tooltips according to the container item's color.")
    public boolean colorfulTooltips = true;
    @Config(description = "Seeing item inventory contents requires shift to be held.")
    public boolean contentsRequireShift = false;
    @Config(name = "slot_overlay", description = "Render a white overlay or the hotbar selected item frame over the slot the next item will be taken out when right-clicking the container item.")
    public SlotOverlay slotOverlay = SlotOverlay.HOVER;
    @Config(description = "Show an indicator on container items when the stack carried by the cursor can be added in your inventory.")
    public boolean containerItemIndicator = true;

    @Override
    public boolean colorfulTooltips() {
        return this.colorfulTooltips;
    }

    @Override
    public boolean contentsRequireShift() {
        return this.contentsRequireShift;
    }

    @Override
    public SlotOverlay slotOverlay() {
        return this.slotOverlay;
    }

    @Override
    public boolean containerItemIndicator() {
        return this.containerItemIndicator;
    }
}
