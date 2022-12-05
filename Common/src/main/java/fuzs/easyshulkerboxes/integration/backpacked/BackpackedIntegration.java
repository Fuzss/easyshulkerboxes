package fuzs.easyshulkerboxes.integration.backpacked;

import fuzs.easyshulkerboxes.world.item.container.GenericItemContainerProvider;
import fuzs.easyshulkerboxes.world.item.storage.ItemContainerProviders;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

public class BackpackedIntegration {

    public static void registerProviders() {
        ItemContainerProviders.registerBuiltInProvider(id("backpack"), new GenericItemContainerProvider(9, 1, DyeColor.BROWN));
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation("backpacked", path);
    }
}
