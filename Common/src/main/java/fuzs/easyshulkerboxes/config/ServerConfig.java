package fuzs.easyshulkerboxes.config;

import fuzs.puzzleslib.config.ConfigCore;
import fuzs.puzzleslib.config.annotation.Config;

public class ServerConfig implements ConfigCore {
    public static final String PRECISION_MODE_MESSAGE = "In precision mode left-clicking inserts an item, and right-clicking extracts a single item, overriding vanilla mouse interactions. The scroll wheel can also be used for quickly moving items.";

    @Config(description = "Allow using the mouse wheel to scroll between slots in an item tooltip to choose the next item to extract.")
    public boolean allowSlotCycling = true;
    @Config(description = "Allow dragging the mouse while holding a container item to insert hovered items, or to extract container contents to empty hovered slots.")
    public boolean allowMouseDragging = true;
    @Config(description = {"Allow extracting / inserting only a single item from a container item instead of all items from the selected slot while a modifier key is held.", PRECISION_MODE_MESSAGE})
    public boolean allowPrecisionMode = true;
}
