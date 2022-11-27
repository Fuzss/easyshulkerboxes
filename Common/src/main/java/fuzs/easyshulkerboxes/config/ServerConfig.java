package fuzs.easyshulkerboxes.config;

import fuzs.puzzleslib.config.ConfigCore;
import fuzs.puzzleslib.config.annotation.Config;

public class ServerConfig implements ConfigCore {
    @Config(description = "Allow using the mouse wheel to scroll between slots in an item tooltip to choose the next item to extract.")
    public boolean allowSlotCycling = true;
    @Config(description = "Allow dragging the mouse while holding a container item to insert hovered items, or to extract container contents to empty hovered slots.")
    public boolean allowMouseDragging = true;
    @Config(description = "Allow inventory interactions and contents tooltip to work on shulker boxes.")
    public boolean allowShulkerBox = true;
    @Config(description = {"Allow inventory interactions and contents tooltip to work on ender chests.", "The item stack must have just a single ender chest item."})
    public boolean allowEnderChest = true;
    @Config(description = {"Allow inventory interactions and contents tooltip from this mod to work on bundles, vanilla behavior is replaced."})
    public boolean allowBundle = true;
}
