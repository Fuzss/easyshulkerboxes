package fuzs.easyshulkerboxes.integration.bagofholding;

import fuzs.easyshulkerboxes.api.container.v1.ItemContainerProvider;
import fuzs.easyshulkerboxes.api.container.v1.SimpleItemProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

import java.util.function.BiConsumer;

public class BagOfHoldingIntegration {

    public static void registerProviders(BiConsumer<ResourceLocation, ItemContainerProvider> consumer) {
        consumer.accept(id("leather_bag_of_holding"), new SimpleItemProvider(9, 1, DyeColor.BROWN).filterContainerItems());
        consumer.accept(id("iron_bag_of_holding"), new SimpleItemProvider(9, 3, DyeColor.WHITE).filterContainerItems());
        consumer.accept(id("golden_bag_of_holding"), new SimpleItemProvider(9, 6, DyeColor.YELLOW).filterContainerItems());
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation("bagofholding", path);
    }
}
