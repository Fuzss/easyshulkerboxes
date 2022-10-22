package fuzs.easyshulkerboxes.api;

import fuzs.easyshulkerboxes.api.init.ModRegistry;
import fuzs.easyshulkerboxes.api.network.C2SCurrentSlotMessage;
import fuzs.puzzleslib.core.CommonFactories;
import fuzs.puzzleslib.core.ModConstructor;
import fuzs.puzzleslib.network.MessageDirection;
import fuzs.puzzleslib.network.NetworkHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleInventoryContainersApi implements ModConstructor {
    public static final String MOD_ID = "simpleinventorycontainers";
    public static final String MOD_NAME = "Simple Inventory Containers";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static final NetworkHandler NETWORK = CommonFactories.INSTANCE.network(MOD_ID);

    @Override
    public void onConstructMod() {
        ModRegistry.touch();
        registerMessages();
    }

    private static void registerMessages() {
        NETWORK.register(C2SCurrentSlotMessage.class, C2SCurrentSlotMessage::new, MessageDirection.TO_SERVER);
    }
}
