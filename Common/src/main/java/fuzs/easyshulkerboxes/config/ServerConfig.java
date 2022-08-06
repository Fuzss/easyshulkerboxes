package fuzs.easyshulkerboxes.config;

import fuzs.puzzleslib.config.ConfigCore;
import fuzs.puzzleslib.config.annotation.Config;

public class ServerConfig implements ConfigCore {
    @Config(description = "Allow inventory interactions and contents tooltip to work on shulker boxes.")
    public boolean allowShulkerBox = true;
    @Config(description = {"Allow inventory interactions and contents tooltip to work on ender chests.", "The item stack must have just a single ender chest item."})
    public boolean allowEnderChest = true;
}
