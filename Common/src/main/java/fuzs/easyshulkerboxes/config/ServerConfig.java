package fuzs.easyshulkerboxes.config;

import fuzs.puzzleslib.config.ConfigCore;
import fuzs.puzzleslib.config.annotation.Config;

public class ServerConfig implements ConfigCore {
    @Config(description = "Allow using the mouse wheel to scroll between slots in an item tooltip to choose the next item to extract.")
    public boolean allowSlotCycling = true;
    @Config(description = "Allow dragging the mouse while holding a container item to insert hovered items, or to extract container contents to empty hovered slots.")
    public boolean allowMouseDragging = true;
}
