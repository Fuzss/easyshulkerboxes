package fuzs.easyshulkerboxes;

import fuzs.easyshulkerboxes.init.ModRegistry;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.core.v1.context.PackRepositorySourcesContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EasyShulkerBoxes implements ModConstructor {
    public static final String MOD_ID = "easyshulkerboxes";
    public static final String MOD_NAME = "Easy Shulker Boxes";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static final Identifier SHULKER_BOXES_LOCATION = id("shulker_boxes");
    public static final Identifier BUNDLES_LOCATION = id("bundles");
    public static final Identifier ENDER_CHEST_LOCATION = id("ender_chest");
    public static final Identifier CONTAINERS_LOCATION = id("containers");
    public static final Identifier MAP_LOCATION = id("map");
    public static final Identifier MOD_SUPPORT_LOCATION = id("mod_support");

    @Override
    public void onConstructMod() {
        ModRegistry.bootstrap();
    }

    @Override
    public void onAddDataPackFinders(PackRepositorySourcesContext context) {
        context.registerBuiltInPack(SHULKER_BOXES_LOCATION, Component.literal("Shulker Boxes"), true);
        context.registerBuiltInPack(BUNDLES_LOCATION, Component.literal("Bundles"), true);
        context.registerBuiltInPack(ENDER_CHEST_LOCATION, Component.literal("Ender Chest"), true);
        context.registerBuiltInPack(CONTAINERS_LOCATION, Component.literal("Containers"), false);
        context.registerBuiltInPack(MAP_LOCATION, Component.literal("Map"), false);
        context.registerBuiltInPack(MOD_SUPPORT_LOCATION, Component.literal("Mod Support"), false);
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
