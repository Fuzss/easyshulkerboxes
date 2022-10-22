package fuzs.easyshulkerboxes.config;

import fuzs.easyshulkerboxes.api.config.ServerConfigCore;
import fuzs.puzzleslib.config.ConfigCore;
import fuzs.puzzleslib.config.annotation.Config;

public class ServerConfig implements ConfigCore, ServerConfigCore {
    @Config(description = "Allow using the mouse wheel to scroll between slots to choose the next item to extract when hovering over a container item.")
    public boolean allowSlotCycling = true;
    @Config(description = "Allow inventory interactions and contents tooltip to work on shulker boxes.")
    public boolean allowShulkerBox = true;
    @Config(description = {"Allow inventory interactions and contents tooltip to work on ender chests.", "The item stack must have just a single ender chest item."})
    public boolean allowEnderChest = true;

    @Override
    public boolean allowSlotCycling() {
        return this.allowSlotCycling;
    }
}
