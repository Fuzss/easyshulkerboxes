package fuzs.easyshulkerboxes.integration.bagofholding;

import fuzs.easyshulkerboxes.world.item.container.SimpleItemProvider;
import fuzs.easyshulkerboxes.world.item.storage.ItemContainerProviders;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

public class BagOfHoldingIntegration {

    public static void registerProviders() {
        ItemContainerProviders.registerBuiltInProvider(id("leather_bag_of_holding"), new SimpleItemProvider(9, 1, DyeColor.BROWN).filterContainerItems());
        ItemContainerProviders.registerBuiltInProvider(id("iron_bag_of_holding"), new SimpleItemProvider(9, 3, DyeColor.WHITE).filterContainerItems());
        ItemContainerProviders.registerBuiltInProvider(id("golden_bag_of_holding"), new SimpleItemProvider(9, 6, DyeColor.YELLOW).filterContainerItems());
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation("bagofholding", path);
    }
}
