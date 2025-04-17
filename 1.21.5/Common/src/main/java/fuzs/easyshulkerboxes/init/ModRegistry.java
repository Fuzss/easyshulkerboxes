package fuzs.easyshulkerboxes.init;

import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.world.item.container.MapProvider;
import fuzs.iteminteractions.api.v1.provider.ItemContentsProvider;
import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import net.minecraft.core.Holder;

public class ModRegistry {
    static final RegistryManager REGISTRIES = RegistryManager.from(EasyShulkerBoxes.MOD_ID);
    public static final Holder.Reference<ItemContentsProvider.Type> MAP_ITEM_CONTENTS_PROVIDER_TYPE = REGISTRIES.register(
            ItemContentsProvider.REGISTRY_KEY,
            "map",
            () -> new ItemContentsProvider.Type(MapProvider.CODEC));

    public static void bootstrap() {
        // NO-OP
    }
}
