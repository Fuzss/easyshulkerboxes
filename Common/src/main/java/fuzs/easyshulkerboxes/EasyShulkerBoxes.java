package fuzs.easyshulkerboxes;

import fuzs.easyshulkerboxes.api.world.item.container.SerializableItemContainerProvider;
import fuzs.easyshulkerboxes.config.ClientConfig;
import fuzs.easyshulkerboxes.config.ServerConfig;
import fuzs.easyshulkerboxes.init.ModRegistry;
import fuzs.easyshulkerboxes.integration.InmisProvider;
import fuzs.easyshulkerboxes.network.S2CEnderChestSetContentMessage;
import fuzs.easyshulkerboxes.network.S2CEnderChestSetSlotMessage;
import fuzs.easyshulkerboxes.network.S2CSyncItemContainerProvider;
import fuzs.easyshulkerboxes.network.client.C2SCurrentSlotMessage;
import fuzs.easyshulkerboxes.network.client.C2SEnderChestMenuMessage;
import fuzs.easyshulkerboxes.network.client.C2SEnderChestSetSlotMessage;
import fuzs.easyshulkerboxes.world.item.container.*;
import fuzs.easyshulkerboxes.world.item.storage.ItemContainerProviders;
import fuzs.puzzleslib.config.ConfigHolder;
import fuzs.puzzleslib.core.CommonFactories;
import fuzs.puzzleslib.core.ModConstructor;
import fuzs.puzzleslib.core.ModLoaderEnvironment;
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
    }

    private static void registerMessages() {
        NETWORK.register(C2SCurrentSlotMessage.class, C2SCurrentSlotMessage::new, MessageDirection.TO_SERVER);
        NETWORK.register(S2CEnderChestSetContentMessage.class, S2CEnderChestSetContentMessage::new, MessageDirection.TO_CLIENT);
        NETWORK.register(S2CEnderChestSetSlotMessage.class, S2CEnderChestSetSlotMessage::new, MessageDirection.TO_CLIENT);
        NETWORK.register(C2SEnderChestSetSlotMessage.class, C2SEnderChestSetSlotMessage::new, MessageDirection.TO_SERVER);
        NETWORK.register(C2SEnderChestMenuMessage.class, C2SEnderChestMenuMessage::new, MessageDirection.TO_SERVER);
        NETWORK.register(S2CSyncItemContainerProvider.class, S2CSyncItemContainerProvider::new, MessageDirection.TO_CLIENT);
    }

    @Override
    public void onCommonSetup() {
        registerItemContainerProviderSerializers();
        if (ModLoaderEnvironment.INSTANCE.isDevelopmentEnvironment()) {
            ItemContainerProviders.serializeBuiltInProviders();
        }
    }

    private static void registerItemContainerProviderSerializers() {
        SerializableItemContainerProvider.register(BlockEntityProvider.class, new ResourceLocation(MOD_ID, "block_entity"), BlockEntityProvider::fromJson);
        SerializableItemContainerProvider.register(BlockEntityViewProvider.class, new ResourceLocation(MOD_ID, "block_entity_view"), BlockEntityViewProvider::fromJson);
        SerializableItemContainerProvider.register(BundleProvider.class, new ResourceLocation(MOD_ID, "bundle"), BundleProvider::fromJson);
        SerializableItemContainerProvider.register(EnderChestProvider.class, new ResourceLocation(MOD_ID, "ender_chest"), EnderChestProvider::fromJson);
        SerializableItemContainerProvider.register(GenericItemContainerProvider.class, new ResourceLocation(MOD_ID, "item"), GenericItemContainerProvider::fromJson);
        SerializableItemContainerProvider.register(MapProvider.class, new ResourceLocation(MOD_ID, "map"), MapProvider::fromJson);
        SerializableItemContainerProvider.register(ShulkerBoxProvider.class, new ResourceLocation(MOD_ID, "shulker_box"), ShulkerBoxProvider::fromJson);
        SerializableItemContainerProvider.register(InmisProvider.class, new ResourceLocation(MOD_ID, "inmis"), InmisProvider::fromJson);
    }
}
