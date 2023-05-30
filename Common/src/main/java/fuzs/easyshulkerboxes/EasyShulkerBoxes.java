package fuzs.easyshulkerboxes;

import fuzs.easyshulkerboxes.world.item.container.MapProvider;
import fuzs.iteminteractionscore.api.container.v1.ItemContainerProviderSerializers;
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
        ItemContainerProviderSerializers.register(MapProvider.class, id("map"), jsonElement -> new MapProvider());
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
