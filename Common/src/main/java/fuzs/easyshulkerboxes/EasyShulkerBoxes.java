package fuzs.easyshulkerboxes;

import fuzs.easyshulkerboxes.api.world.item.container.ItemContainerProviderSerializers;
import fuzs.easyshulkerboxes.config.ClientConfig;
import fuzs.easyshulkerboxes.config.ServerConfig;
import fuzs.easyshulkerboxes.init.ModRegistry;
import fuzs.easyshulkerboxes.integration.backpacked.BackpackedIntegration;
import fuzs.easyshulkerboxes.integration.bagofholding.BagOfHoldingIntegration;
import fuzs.easyshulkerboxes.integration.inmis.InmisIntegration;
import fuzs.easyshulkerboxes.integration.reinforcedshulkerboxes.ReinforcedShulkerBoxesIntegration;
import fuzs.easyshulkerboxes.integration.simplebackpack.SimpleBackpackIntegration;
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
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
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
    
    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
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
        registerBuiltInProviders();
        registerItemContainerProviderSerializers();
        registerIntegrationProviders();
        if (ModLoaderEnvironment.INSTANCE.isDevelopmentEnvironment()) {
            ItemContainerProviders.serializeBuiltInProviders();
        }
    }

    private static void registerBuiltInProviders() {
        registerShulkerBoxProviders();
        ItemContainerProviders.registerBuiltInProvider(Items.ENDER_CHEST, new EnderChestProvider());
        ItemContainerProviders.registerBuiltInProvider(Items.BUNDLE, new BundleProvider(64));
        ItemContainerProviders.registerBuiltInProvider(Items.FILLED_MAP, new MapProvider());
        ItemContainerProviders.registerBuiltInProvider(Items.DROPPER, new BlockEntityProvider(BlockEntityType.DROPPER, 3, 3));
        ItemContainerProviders.registerBuiltInProvider(Items.DISPENSER, new BlockEntityProvider(BlockEntityType.DISPENSER, 3, 3));
        ItemContainerProviders.registerBuiltInProvider(Items.CHEST, new BlockEntityProvider(BlockEntityType.CHEST, 9, 3));
        ItemContainerProviders.registerBuiltInProvider(Items.TRAPPED_CHEST, new BlockEntityProvider(BlockEntityType.TRAPPED_CHEST, 9, 3));
        ItemContainerProviders.registerBuiltInProvider(Items.BARREL, new BlockEntityProvider(BlockEntityType.BARREL, 9, 3));
        ItemContainerProviders.registerBuiltInProvider(Items.HOPPER, new BlockEntityProvider(BlockEntityType.HOPPER, 5, 1));
        ItemContainerProviders.registerBuiltInProvider(Items.FURNACE, new BlockEntityViewProvider(BlockEntityType.FURNACE, 3, 1));
        ItemContainerProviders.registerBuiltInProvider(Items.BLAST_FURNACE, new BlockEntityViewProvider(BlockEntityType.BLAST_FURNACE, 3, 1));
        ItemContainerProviders.registerBuiltInProvider(Items.SMOKER, new BlockEntityViewProvider(BlockEntityType.SMOKER, 3, 1));
        ItemContainerProviders.registerBuiltInProvider(Items.BREWING_STAND, new BlockEntityViewProvider(BlockEntityType.BREWING_STAND, 5, 1));
        ItemContainerProviders.registerBuiltInProvider(Items.CAMPFIRE, new BlockEntityViewProvider(BlockEntityType.CAMPFIRE, 4, 1));
        ItemContainerProviders.registerBuiltInProvider(Items.SOUL_CAMPFIRE, new BlockEntityViewProvider(BlockEntityType.CAMPFIRE, 4, 1));
    }

    private static void registerShulkerBoxProviders() {
        ItemContainerProviders.registerBuiltInProvider(Items.SHULKER_BOX, BlockEntityProvider.shulkerBoxProvider(BlockEntityType.SHULKER_BOX, 9, 3, null));
        for (DyeColor dyeColor : DyeColor.values()) {
            // only affects vanilla shulker boxes, other mods might add shulker boxes with a different inventory size
            Item item = ShulkerBoxBlock.getBlockByColor(dyeColor).asItem();
            ItemContainerProviders.registerBuiltInProvider(item, BlockEntityProvider.shulkerBoxProvider(BlockEntityType.SHULKER_BOX, 9, 3, dyeColor));
        }
    }

    private static void registerIntegrationProviders() {
        BagOfHoldingIntegration.registerProviders();
        BackpackedIntegration.registerProviders();
        SimpleBackpackIntegration.registerProviders();
        InmisIntegration.registerProviders();
        ReinforcedShulkerBoxesIntegration.registerProviders();
    }

    private static void registerItemContainerProviderSerializers() {
        ItemContainerProviderSerializers.register(BlockEntityProvider.class, id("block_entity"), ItemContainerProviderBuilder.fromJson(ItemContainerProviderBuilder::toBlockEntityProvider));
        ItemContainerProviderSerializers.register(BlockEntityViewProvider.class, id("block_entity_view"), ItemContainerProviderBuilder.fromJson(ItemContainerProviderBuilder::toBlockEntityViewProvider));
        ItemContainerProviderSerializers.register(BundleProvider.class, id("bundle"), ItemContainerProviderBuilder.fromJson(ItemContainerProviderBuilder::toBundleProvider));
        ItemContainerProviderSerializers.register(EnderChestProvider.class, id("ender_chest"), ItemContainerProviderBuilder.fromJson(ItemContainerProviderBuilder::toEnderChestProvider));
        ItemContainerProviderSerializers.register(SimpleItemProvider.class, id("item"), ItemContainerProviderBuilder.fromJson(ItemContainerProviderBuilder::toSimpleItemContainerProvider));
        ItemContainerProviderSerializers.register(MapProvider.class, id("map"), jsonElement -> new MapProvider());
    }
}
