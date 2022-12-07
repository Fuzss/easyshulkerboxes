package fuzs.easyshulkerboxes.integration.reinforcedshulkerboxes;

import fuzs.easyshulkerboxes.world.item.container.BlockEntityProvider;
import fuzs.easyshulkerboxes.world.item.storage.ItemContainerProviders;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.Nullable;

public class ReinforcedShulkerBoxesIntegration {

    public static void registerProviders() {
        for (ShulkerBoxMaterial material : ShulkerBoxMaterial.values()) {
            ItemContainerProviders.registerBuiltInProvider(material.id(), BlockEntityProvider.shulkerBoxProvider(material.id(), material.width, material.height, null));
            for (DyeColor dyeColor : DyeColor.values()) {
                ItemContainerProviders.registerBuiltInProvider(material.id(dyeColor), BlockEntityProvider.shulkerBoxProvider(material.id(), material.width, material.height, dyeColor));
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
