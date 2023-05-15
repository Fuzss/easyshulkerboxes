package fuzs.easyshulkerboxes.integration.bagofholding;

import fuzs.easyshulkerboxes.world.item.container.SimpleItemProvider;
import fuzs.easyshulkerboxes.world.item.storage.ItemContainerProvidersListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

public class BagOfHoldingIntegration {

    public static void registerProviders() {
        ItemContainerProvidersListener.registerBuiltInProvider(id("leather_bag_of_holding"), new SimpleItemProvider(9, 1, DyeColor.BROWN).filterContainerItems());
        ItemContainerProvidersListener.registerBuiltInProvider(id("iron_bag_of_holding"), new SimpleItemProvider(9, 3, DyeColor.WHITE).filterContainerItems());
        ItemContainerProvidersListener.registerBuiltInProvider(id("golden_bag_of_holding"), new SimpleItemProvider(9, 6, DyeColor.YELLOW).filterContainerItems());
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation("bagofholding", path);
    }
}
