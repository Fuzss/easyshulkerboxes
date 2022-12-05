package fuzs.easyshulkerboxes.integration;

import fuzs.easyshulkerboxes.world.item.container.EnderChestProvider;
import fuzs.easyshulkerboxes.world.item.storage.ItemContainerProviders;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

public class InmisIntegration {

    public static void registerProviders() {
        final String nbtKey = "Inventory";
        ItemContainerProviders.registerBuiltInProvider(id("baby_backpack"), new InmisProvider(3, 1, DyeColor.ORANGE, nbtKey));
        ItemContainerProviders.registerBuiltInProvider(id("frayed_backpack"), new InmisProvider(9, 1, DyeColor.BROWN, nbtKey));
        ItemContainerProviders.registerBuiltInProvider(id("plated_backpack"), new InmisProvider(9, 2, DyeColor.WHITE, nbtKey));
        ItemContainerProviders.registerBuiltInProvider(id("gilded_backpack"), new InmisProvider(9, 3, DyeColor.YELLOW, nbtKey));
        ItemContainerProviders.registerBuiltInProvider(id("bejeweled_backpack"), new InmisProvider(9, 5, DyeColor.LIGHT_BLUE, nbtKey));
        ItemContainerProviders.registerBuiltInProvider(id("blazing_backpack"), new InmisProvider(9, 6, DyeColor.YELLOW, nbtKey));
        ItemContainerProviders.registerBuiltInProvider(id("withered_backpack"), new InmisProvider(11, 6, DyeColor.BLACK, nbtKey));
        ItemContainerProviders.registerBuiltInProvider(id("endless_backpack"), new InmisProvider(15, 6, DyeColor.CYAN, nbtKey));
        ItemContainerProviders.registerBuiltInProvider(id("ender_pouch"), new EnderChestProvider());
    }
    
    public static ResourceLocation id(String path) {
        return new ResourceLocation("inmis", path);
    }
}
