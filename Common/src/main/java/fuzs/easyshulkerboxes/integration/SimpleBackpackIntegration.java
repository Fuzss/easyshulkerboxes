package fuzs.easyshulkerboxes.integration;

import fuzs.easyshulkerboxes.world.item.container.BundleProvider;
import fuzs.easyshulkerboxes.world.item.container.EnderChestProvider;
import fuzs.easyshulkerboxes.world.item.container.GenericItemContainerProvider;
import fuzs.easyshulkerboxes.world.item.storage.ItemContainerProviders;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

public class SimpleBackpackIntegration {

    public static void registerProviders() {
        ItemContainerProviders.registerBuiltInProvider(id("ender_pack"), new EnderChestProvider());
        ItemContainerProviders.registerBuiltInProvider(id("backpack"), new GenericItemContainerProvider(9, 6, DyeColor.BROWN, "backpack/Items"));
        ItemContainerProviders.registerBuiltInProvider(id("simple_bundle"), new BundleProvider(3456));
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation("simple_backpack", path);
    }
}
