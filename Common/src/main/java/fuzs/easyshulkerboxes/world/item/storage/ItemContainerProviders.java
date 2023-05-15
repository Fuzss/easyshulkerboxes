package fuzs.easyshulkerboxes.world.item.storage;

import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.api.world.item.container.ItemContainerProviderSerializers;
import fuzs.easyshulkerboxes.integration.backpacked.BackpackedIntegration;
import fuzs.easyshulkerboxes.integration.bagofholding.BagOfHoldingIntegration;
import fuzs.easyshulkerboxes.integration.inmis.InmisIntegration;
import fuzs.easyshulkerboxes.integration.reinforcedshulkerboxes.ReinforcedShulkerBoxesIntegration;
import fuzs.easyshulkerboxes.integration.simplebackpack.SimpleBackpackIntegration;
import fuzs.easyshulkerboxes.world.item.container.*;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ItemContainerProviders {

    public static void registerAllBuiltInProviders() {
        registerBuiltInProviders();
        registerIntegrationProviders();
        registerItemContainerProviderSerializers();
    }

    private static void registerBuiltInProviders() {
        registerShulkerBoxProviders();
        ItemContainerProvidersListener.registerBuiltInProvider(Items.ENDER_CHEST, new EnderChestProvider());
        ItemContainerProvidersListener.registerBuiltInProvider(Items.BUNDLE, new BundleProvider(64));
        ItemContainerProvidersListener.registerBuiltInProvider(Items.FILLED_MAP, new MapProvider());
        ItemContainerProvidersListener.registerBuiltInProvider(Items.DROPPER, new BlockEntityProvider(BlockEntityType.DROPPER, 3, 3));
        ItemContainerProvidersListener.registerBuiltInProvider(Items.DISPENSER, new BlockEntityProvider(BlockEntityType.DISPENSER, 3, 3));
        ItemContainerProvidersListener.registerBuiltInProvider(Items.CHEST, new BlockEntityProvider(BlockEntityType.CHEST, 9, 3));
        ItemContainerProvidersListener.registerBuiltInProvider(Items.TRAPPED_CHEST, new BlockEntityProvider(BlockEntityType.TRAPPED_CHEST, 9, 3));
        ItemContainerProvidersListener.registerBuiltInProvider(Items.BARREL, new BlockEntityProvider(BlockEntityType.BARREL, 9, 3));
        ItemContainerProvidersListener.registerBuiltInProvider(Items.HOPPER, new BlockEntityProvider(BlockEntityType.HOPPER, 5, 1));
        ItemContainerProvidersListener.registerBuiltInProvider(Items.FURNACE, new BlockEntityViewProvider(BlockEntityType.FURNACE, 3, 1));
        ItemContainerProvidersListener.registerBuiltInProvider(Items.BLAST_FURNACE, new BlockEntityViewProvider(BlockEntityType.BLAST_FURNACE, 3, 1));
        ItemContainerProvidersListener.registerBuiltInProvider(Items.SMOKER, new BlockEntityViewProvider(BlockEntityType.SMOKER, 3, 1));
        ItemContainerProvidersListener.registerBuiltInProvider(Items.BREWING_STAND, new BlockEntityViewProvider(BlockEntityType.BREWING_STAND, 5, 1));
        ItemContainerProvidersListener.registerBuiltInProvider(Items.CAMPFIRE, new BlockEntityViewProvider(BlockEntityType.CAMPFIRE, 4, 1));
        ItemContainerProvidersListener.registerBuiltInProvider(Items.SOUL_CAMPFIRE, new BlockEntityViewProvider(BlockEntityType.CAMPFIRE, 4, 1));
    }

    private static void registerShulkerBoxProviders() {
        ItemContainerProvidersListener.registerBuiltInProvider(Items.SHULKER_BOX, BlockEntityProvider.shulkerBoxProvider(BlockEntityType.SHULKER_BOX, 9, 3, null));
        for (DyeColor dyeColor : DyeColor.values()) {
            // only affects vanilla shulker boxes, other mods might add shulker boxes with a different inventory size
            Item item = ShulkerBoxBlock.getBlockByColor(dyeColor).asItem();
            ItemContainerProvidersListener.registerBuiltInProvider(item, BlockEntityProvider.shulkerBoxProvider(BlockEntityType.SHULKER_BOX, 9, 3, dyeColor));
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
        ItemContainerProviderSerializers.register(BlockEntityProvider.class, EasyShulkerBoxes.id("block_entity"), ItemContainerProviderBuilder.fromJson(ItemContainerProviderBuilder::toBlockEntityProvider));
        ItemContainerProviderSerializers.register(BlockEntityViewProvider.class, EasyShulkerBoxes.id("block_entity_view"), ItemContainerProviderBuilder.fromJson(ItemContainerProviderBuilder::toBlockEntityViewProvider));
        ItemContainerProviderSerializers.register(BundleProvider.class, EasyShulkerBoxes.id("bundle"), ItemContainerProviderBuilder.fromJson(ItemContainerProviderBuilder::toBundleProvider));
        ItemContainerProviderSerializers.register(EnderChestProvider.class, EasyShulkerBoxes.id("ender_chest"), ItemContainerProviderBuilder.fromJson(ItemContainerProviderBuilder::toEnderChestProvider));
        ItemContainerProviderSerializers.register(SimpleItemProvider.class, EasyShulkerBoxes.id("item"), ItemContainerProviderBuilder.fromJson(ItemContainerProviderBuilder::toSimpleItemContainerProvider));
        ItemContainerProviderSerializers.register(MapProvider.class, EasyShulkerBoxes.id("map"), jsonElement -> new MapProvider());
    }
}
