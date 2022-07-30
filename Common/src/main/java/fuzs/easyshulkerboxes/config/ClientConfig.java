package fuzs.easyshulkerboxes.config;

import fuzs.puzzleslib.config.ConfigCore;
import fuzs.puzzleslib.config.annotation.Config;

public class ClientConfig implements ConfigCore {
    @Config(description = "Color shulker box inventories on tooltips according to the boxes color.")
    public boolean colorfulTooltips = true;
    @Config(description = "Seeing shulker box inventory contents requires shift to be held.")
    public boolean contentsRequireShift = true;
    @Config(name = "render_slot_overlay", description = "Render a white overlay over the slot the next item will be taken out when right-clicking the shulker box item.")
    public boolean slotOverlay = true;
}
