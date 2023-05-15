package fuzs.easyshulkerboxes.integration.inmis;

import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.api.world.item.container.ItemContainerProvider;
import fuzs.easyshulkerboxes.api.world.item.container.ItemContainerProviderSerializers;
import fuzs.easyshulkerboxes.world.item.container.EnderChestProvider;
import fuzs.easyshulkerboxes.world.item.storage.ItemContainerProvidersListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

public class InmisIntegration {
    private static final List<String> DISALLOWED_ITEMS = Stream.of("baby_backpack", "frayed_backpack", "plated_backpack", "gilded_backpack", "bejeweled_backpack", "blazing_backpack", "withered_backpack", "endless_backpack")
            .map(InmisIntegration::id).map(ResourceLocation::toString).toList();

    public static void registerProviders() {
        ItemContainerProvidersListener.registerBuiltInProvider(id("baby_backpack"), provider(3, 1, DyeColor.ORANGE));
        ItemContainerProvidersListener.registerBuiltInProvider(id("frayed_backpack"), provider(9, 1, DyeColor.BROWN));
        ItemContainerProvidersListener.registerBuiltInProvider(id("plated_backpack"), provider(9, 2, DyeColor.WHITE));
        ItemContainerProvidersListener.registerBuiltInProvider(id("gilded_backpack"), provider(9, 3, DyeColor.YELLOW));
        ItemContainerProvidersListener.registerBuiltInProvider(id("bejeweled_backpack"), provider(9, 5, DyeColor.LIGHT_BLUE));
        ItemContainerProvidersListener.registerBuiltInProvider(id("blazing_backpack"), provider(9, 6, DyeColor.YELLOW));
        ItemContainerProvidersListener.registerBuiltInProvider(id("withered_backpack"), provider(11, 6, DyeColor.BLACK));
        ItemContainerProvidersListener.registerBuiltInProvider(id("endless_backpack"), provider(15, 6, DyeColor.CYAN));
        ItemContainerProvidersListener.registerBuiltInProvider(id("ender_pouch"), new EnderChestProvider());
        ItemContainerProviderSerializers.register(InmisProvider.class, new ResourceLocation(EasyShulkerBoxes.MOD_ID, "inmis"), InmisProvider::fromJson);
    }
    
    private static ItemContainerProvider provider(int inventoryWidth, int inventoryHeight, @Nullable DyeColor dyeColor) {
        return new InmisProvider(inventoryWidth, inventoryHeight, dyeColor, "Inventory").disallowValues(DISALLOWED_ITEMS);
    }
    
    public static ResourceLocation id(String path) {
        return new ResourceLocation("inmis", path);
    }
}
