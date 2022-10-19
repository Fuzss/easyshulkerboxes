package fuzs.easyshulkerboxes.config;

import fuzs.easyshulkerboxes.api.config.ContainerItemTooltipConfig;
import fuzs.puzzleslib.config.ConfigCore;
import fuzs.puzzleslib.config.annotation.Config;

public class ClientConfig implements ConfigCore, ContainerItemTooltipConfig {
    @Config(description = "Color shulker box inventories on tooltips according to the boxes color.")
    public boolean colorfulTooltips = true;
    @Config(description = "Seeing shulker box/ender chest inventory contents requires shift to be held.")
    public boolean contentsRequireShift = true;
    @Config(name = "render_slot_overlay", description = "Render a white overlay over the slot the next item will be taken out when right-clicking the shulker box/ender chest item.")
    public boolean slotOverlay = true;
    @Config(description = "Show an indicator on shulker boxes, ender chests and bundles when the stack carried by the cursor can be added to them in your inventory.")
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
    public boolean slotOverlay() {
        return this.slotOverlay;
    }
}
