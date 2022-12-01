package fuzs.easyshulkerboxes.integration;

import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.api.world.item.container.SerializableItemContainerProvider;
import fuzs.easyshulkerboxes.world.item.container.BundleProvider;
import fuzs.easyshulkerboxes.world.item.container.EnderChestProvider;
import fuzs.easyshulkerboxes.world.item.container.helper.ContainerItemHelper;
import fuzs.easyshulkerboxes.world.item.storage.ItemContainerProviders;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

public class SimpleBackpackIntegration {

    public static void registerContents() {
        SerializableItemContainerProvider.register(SimpleBackpackProvider.class, new ResourceLocation(EasyShulkerBoxes.MOD_ID, "simplebackpack"), SimpleBackpackProvider::fromJson);
        ItemContainerProviders.registerBuiltIn(new ResourceLocation("simple_backpack:ender_pack"), new EnderChestProvider());
        ItemContainerProviders.registerBuiltIn(new ResourceLocation("simple_backpack:backpack"), new SimpleBackpackProvider(9, 6, DyeColor.BROWN, ContainerItemHelper.TAG_ITEMS));
        ItemContainerProviders.registerBuiltIn(new ResourceLocation("simple_backpack:simple_bundle"), new BundleProvider(3456));
    }
}
