package fuzs.easyshulkerboxes.integration.reinforcedshulkerboxes;

import fuzs.puzzlesapi.api.iteminteractions.v1.provider.BlockEntityProvider;
import fuzs.puzzlesapi.api.iteminteractions.v1.provider.ItemContainerProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public class ReinforcedShulkerBoxesIntegration {

    public static void registerProviders(BiConsumer<ResourceLocation, ItemContainerProvider> consumer) {
        for (ShulkerBoxMaterial material : ShulkerBoxMaterial.values()) {
            consumer.accept(material.id(), BlockEntityProvider.shulkerBoxProvider(material.id(), material.width, material.height, null));
            for (DyeColor dyeColor : DyeColor.values()) {
                consumer.accept(material.id(dyeColor), BlockEntityProvider.shulkerBoxProvider(material.id(), material.width, material.height, dyeColor));
            }
        }
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation("reinfshulker", path);
    }

    private enum ShulkerBoxMaterial {
        COPPER("copper", 9, 5),
        IRON("iron", 9, 6),
        GOLD("gold", 9, 9),
        DIAMOND("diamond", 12, 9),
        NETHERITE("netherite", 12, 9);

        public final String name;
        public final int width;
        public final int height;

        ShulkerBoxMaterial(String name, int width, int height) {
            this.name = name;
            this.width = width;
            this.height = height;
        }

        public ResourceLocation id() {
            return this.id(null);
        }

        public ResourceLocation id(@Nullable DyeColor dyeColor) {
            String path = this.name + "_shulker_box";
            if (dyeColor != null) path = "%s_%s".formatted(dyeColor.getSerializedName(), path);
            return ReinforcedShulkerBoxesIntegration.id(path);
        }
    }
}
