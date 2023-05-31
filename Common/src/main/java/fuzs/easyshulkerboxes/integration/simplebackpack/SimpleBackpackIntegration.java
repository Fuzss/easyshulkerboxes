package fuzs.easyshulkerboxes.integration.simplebackpack;

import fuzs.iteminteractionscore.api.container.v1.provider.BundleProvider;
import fuzs.iteminteractionscore.api.container.v1.provider.EnderChestProvider;
import fuzs.iteminteractionscore.api.container.v1.provider.ItemContainerProvider;
import fuzs.iteminteractionscore.api.container.v1.provider.SimpleItemProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

import java.util.function.BiConsumer;

public class SimpleBackpackIntegration {

    public static void registerProviders(BiConsumer<ResourceLocation, ItemContainerProvider> consumer) {
        consumer.accept(id("ender_pack"), new EnderChestProvider());
        consumer.accept(id("backpack"), new SimpleItemProvider(9, 6, DyeColor.BROWN, "backpack/Items").filterContainerItems());
        consumer.accept(id("simple_bundle"), new BundleProvider(3456, DyeColor.BROWN));
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation("simple_backpack", path);
    }
}
