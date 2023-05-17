package fuzs.easyshulkerboxes;

import fuzs.easyshulkerboxes.api.container.v1.ItemContainerProviderSerializers;
import fuzs.easyshulkerboxes.impl.config.ClientConfig;
import fuzs.easyshulkerboxes.impl.config.ServerConfig;
import fuzs.easyshulkerboxes.impl.init.ModRegistry;
import fuzs.easyshulkerboxes.impl.network.S2CSyncItemContainerProvider;
import fuzs.easyshulkerboxes.impl.network.client.C2SContainerClientInputMessage;
import fuzs.easyshulkerboxes.network.S2CEnderChestSetContentMessage;
import fuzs.easyshulkerboxes.network.S2CEnderChestSetSlotMessage;
import fuzs.easyshulkerboxes.network.client.C2SEnderChestMenuMessage;
import fuzs.easyshulkerboxes.network.client.C2SEnderChestSetSlotMessage;
import fuzs.easyshulkerboxes.world.item.container.EnderChestProvider;
import fuzs.easyshulkerboxes.api.container.v1.ItemContainerProviderBuilder;
import fuzs.easyshulkerboxes.world.item.container.MapProvider;
import fuzs.puzzleslib.config.ConfigHolder;
import fuzs.puzzleslib.core.CommonFactories;
import fuzs.puzzleslib.core.ModConstructor;
import fuzs.puzzleslib.network.MessageDirection;
import fuzs.puzzleslib.network.NetworkHandler;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EasyShulkerBoxes implements ModConstructor {
    public static final String MOD_ID = "easyshulkerboxes";
    public static final String MOD_NAME = "Easy Shulker Boxes";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static final NetworkHandler NETWORK = CommonFactories.INSTANCE.network(MOD_ID);
    @SuppressWarnings("Convert2MethodRef")
    public static final ConfigHolder CONFIG = CommonFactories.INSTANCE
            .clientConfig(ClientConfig.class, () -> new ClientConfig())
            .serverConfig(ServerConfig.class, () -> new ServerConfig());

    @Override
    public void onConstructMod() {
        CONFIG.bakeConfigs(MOD_ID);
        ModRegistry.touch();
        registerMessages();
        ItemContainerProviderSerializers.register(EnderChestProvider.class, EasyShulkerBoxes.id("ender_chest"), ItemContainerProviderBuilder.fromJson(ItemContainerProviderBuilder::toEnderChestProvider));
        ItemContainerProviderSerializers.register(MapProvider.class, EasyShulkerBoxes.id("map"), jsonElement -> new MapProvider());
    }

    private static void registerMessages() {
        NETWORK.register(C2SContainerClientInputMessage.class, C2SContainerClientInputMessage::new, MessageDirection.TO_SERVER);
        NETWORK.register(S2CEnderChestSetContentMessage.class, S2CEnderChestSetContentMessage::new, MessageDirection.TO_CLIENT);
        NETWORK.register(S2CEnderChestSetSlotMessage.class, S2CEnderChestSetSlotMessage::new, MessageDirection.TO_CLIENT);
        NETWORK.register(C2SEnderChestSetSlotMessage.class, C2SEnderChestSetSlotMessage::new, MessageDirection.TO_SERVER);
        NETWORK.register(C2SEnderChestMenuMessage.class, C2SEnderChestMenuMessage::new, MessageDirection.TO_SERVER);
        NETWORK.register(S2CSyncItemContainerProvider.class, S2CSyncItemContainerProvider::new, MessageDirection.TO_CLIENT);
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
