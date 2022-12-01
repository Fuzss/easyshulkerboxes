package fuzs.easyshulkerboxes.integration;

import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.api.world.item.container.SerializableItemContainerProvider;
import fuzs.easyshulkerboxes.world.item.storage.ItemContainerProviders;
import net.minecraft.resources.ResourceLocation;

public class BackpackedIntegration {

    public static void registerContents() {
        // this is required to support Backpacked's customizable backpack size
        // for other backpack mods this should ideally not be necessary if their backpacks have a fixed size
        SerializableItemContainerProvider.register(BackpackedProvider.class, new ResourceLocation(EasyShulkerBoxes.MOD_ID, "backpacked"), BackpackedProvider::fromJson);
        ItemContainerProviders.registerBuiltIn(new ResourceLocation("backpacked:backpack"), new BackpackedProvider());
    }
}
