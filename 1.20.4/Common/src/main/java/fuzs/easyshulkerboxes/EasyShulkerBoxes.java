package fuzs.easyshulkerboxes;

import fuzs.easyshulkerboxes.integration.inmis.InmisIntegration;
import fuzs.easyshulkerboxes.world.item.container.MapProvider;
import fuzs.puzzlesapi.api.iteminteractions.v1.ItemContainerProviderBuilder;
import fuzs.puzzlesapi.api.iteminteractions.v1.ItemContainerProviderSerializers;
import fuzs.puzzlesapi.api.iteminteractions.v1.provider.*;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EasyShulkerBoxes implements ModConstructor {
    public static final String MOD_ID = "easyshulkerboxes";
    public static final String MOD_NAME = "Easy Shulker Boxes";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @Override
    public void onConstructMod() {
        ItemContainerProviderSerializers.register(BlockEntityProvider.class, id("block_entity"), ItemContainerProviderBuilder.fromJson(ItemContainerProviderBuilder::toBlockEntityProvider));
        ItemContainerProviderSerializers.register(BlockEntityViewProvider.class, id("block_entity_view"), ItemContainerProviderBuilder.fromJson(ItemContainerProviderBuilder::toBlockEntityViewProvider));
        ItemContainerProviderSerializers.register(BundleProvider.class, id("bundle"), ItemContainerProviderBuilder.fromJson(ItemContainerProviderBuilder::toBundleProvider));
        ItemContainerProviderSerializers.register(SimpleItemProvider.class, id("item"), ItemContainerProviderBuilder.fromJson(ItemContainerProviderBuilder::toSimpleItemContainerProvider));
        ItemContainerProviderSerializers.register(EnderChestProvider.class, id("ender_chest"), ItemContainerProviderBuilder.fromJson(ItemContainerProviderBuilder::toEnderChestProvider));
        ItemContainerProviderSerializers.register(MapProvider.class, id("map"), jsonElement -> new MapProvider());
        InmisIntegration.registerSerializers();
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
