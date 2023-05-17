package fuzs.easyshulkerboxes.integration.simplebackpack;

import fuzs.easyshulkerboxes.api.container.v1.ItemContainerProvider;
import fuzs.easyshulkerboxes.api.container.v1.BundleProvider;
import fuzs.easyshulkerboxes.world.item.container.EnderChestProvider;
import fuzs.easyshulkerboxes.api.container.v1.SimpleItemProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

import java.util.function.BiConsumer;

public class SimpleBackpackIntegration {

    public static void registerProviders(BiConsumer<ResourceLocation, ItemContainerProvider> consumer) {
        consumer.accept(id("ender_pack"), new EnderChestProvider());
        consumer.accept(id("backpack"), new SimpleItemProvider(9, 6, DyeColor.BROWN, "backpack/Items").filterContainerItems());
        consumer.accept(id("simple_bundle"), new BundleProvider(3456));
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation("simple_backpack", path);
    }
}
