package fuzs.easyshulkerboxes.integration.inmis;

import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.api.container.v1.ItemContainerProvider;
import fuzs.easyshulkerboxes.api.container.v1.ItemContainerProviderSerializers;
import fuzs.easyshulkerboxes.world.item.container.EnderChestProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class InmisIntegration {
    private static final List<String> DISALLOWED_ITEMS = Stream.of("baby_backpack", "frayed_backpack", "plated_backpack", "gilded_backpack", "bejeweled_backpack", "blazing_backpack", "withered_backpack", "endless_backpack")
            .map(InmisIntegration::id).map(ResourceLocation::toString).toList();

    public static void registerProviders(BiConsumer<ResourceLocation, ItemContainerProvider> consumer) {
        consumer.accept(id("baby_backpack"), provider(3, 1, DyeColor.ORANGE));
        consumer.accept(id("frayed_backpack"), provider(9, 1, DyeColor.BROWN));
        consumer.accept(id("plated_backpack"), provider(9, 2, DyeColor.WHITE));
        consumer.accept(id("gilded_backpack"), provider(9, 3, DyeColor.YELLOW));
        consumer.accept(id("bejeweled_backpack"), provider(9, 5, DyeColor.LIGHT_BLUE));
        consumer.accept(id("blazing_backpack"), provider(9, 6, DyeColor.YELLOW));
        consumer.accept(id("withered_backpack"), provider(11, 6, DyeColor.BLACK));
        consumer.accept(id("endless_backpack"), provider(15, 6, DyeColor.CYAN));
        consumer.accept(id("ender_pouch"), new EnderChestProvider());
        ItemContainerProviderSerializers.register(InmisProvider.class, EasyShulkerBoxes.id("inmis"), InmisProvider::fromJson);
    }
    
    private static ItemContainerProvider provider(int inventoryWidth, int inventoryHeight, @Nullable DyeColor dyeColor) {
        return new InmisProvider(inventoryWidth, inventoryHeight, dyeColor, "Inventory").disallowValues(DISALLOWED_ITEMS);
    }
    
    public static ResourceLocation id(String path) {
        return new ResourceLocation("inmis", path);
    }
}
