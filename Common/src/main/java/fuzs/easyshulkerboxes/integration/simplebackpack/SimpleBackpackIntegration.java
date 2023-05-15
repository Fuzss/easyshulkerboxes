package fuzs.easyshulkerboxes.integration.simplebackpack;

import fuzs.easyshulkerboxes.world.item.container.BundleProvider;
import fuzs.easyshulkerboxes.world.item.container.EnderChestProvider;
import fuzs.easyshulkerboxes.world.item.container.SimpleItemProvider;
import fuzs.easyshulkerboxes.world.item.storage.ItemContainerProvidersListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

public class SimpleBackpackIntegration {

    public static void registerProviders() {
        ItemContainerProvidersListener.registerBuiltInProvider(id("ender_pack"), new EnderChestProvider());
        ItemContainerProvidersListener.registerBuiltInProvider(id("backpack"), new SimpleItemProvider(9, 6, DyeColor.BROWN, "backpack/Items").filterContainerItems());
        ItemContainerProvidersListener.registerBuiltInProvider(id("simple_bundle"), new BundleProvider(3456));
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation("simple_backpack", path);
    }
}
