package fuzs.easyshulkerboxes;

import fuzs.easyshulkerboxes.config.ClientConfig;
import fuzs.puzzleslib.config.AbstractConfig;
import fuzs.puzzleslib.config.ConfigHolder;
import fuzs.puzzleslib.core.Services;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EasyShulkerBoxes {
    public static final String MOD_ID = "easyshulkerboxes";
    public static final String MOD_NAME = "Easy Shulker Boxes";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @SuppressWarnings("Convert2MethodRef")
    public static final ConfigHolder<ClientConfig, AbstractConfig> CONFIG = Services.FACTORIES.clientConfig(() -> new ClientConfig());

    public static void onConstructMod() {
        CONFIG.loadConfigs(MOD_ID);
    }
}
