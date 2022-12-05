package fuzs.easyshulkerboxes.integration;

import fuzs.easyshulkerboxes.world.item.container.ShulkerBoxProvider;
import fuzs.easyshulkerboxes.world.item.storage.ItemContainerProviders;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.Nullable;

public class ReinforcedShulkerBoxesIntegration {

    public static void registerProviders() {
        for (ShulkerBoxMaterial material : ShulkerBoxMaterial.values()) {
            ItemContainerProviders.registerBuiltInProvider(material.id(), new ShulkerBoxProvider(material.id(), material.width, material.height));
            for (DyeColor dyeColor : DyeColor.values()) {
                ItemContainerProviders.registerBuiltInProvider(material.id(dyeColor), new ShulkerBoxProvider(material.id(), material.width, material.height, dyeColor));
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
