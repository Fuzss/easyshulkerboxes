package fuzs.easyshulkerboxes.config;

import fuzs.puzzleslib.config.ConfigCore;
import fuzs.puzzleslib.config.annotation.Config;

public class CommonConfig implements ConfigCore {
    @Config(description = "Enables integration for the Backpacked mod.", category = "integration")
    public boolean backpacked = true;
}
