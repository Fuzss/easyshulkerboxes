package fuzs.easyshulkerboxes.integration.backpacked;

import com.google.common.collect.ImmutableList;
import fuzs.easyshulkerboxes.world.item.container.SimpleItemProvider;
import fuzs.easyshulkerboxes.world.item.storage.ItemContainerProvidersListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

import java.util.ArrayList;
import java.util.List;

public class BackpackedIntegration {
    /**
     * copied from Backpacked's config
     */
    private static final List<String> DISALLOWED_ITEMS = ImmutableList.copyOf(new ArrayList<>() {{
        this.add("travelersbackpack:custom_travelers_backpack");
        this.add("pinesbarrels:better_barrel");
        this.add("quark:seed_pouch");
        this.add("quark:backpack");
        this.add("sophisticatedbackpacks:backpack");
        this.add("sophisticatedbackpacks:iron_backpack");
        this.add("sophisticatedbackpacks:gold_backpack");
        this.add("sophisticatedbackpacks:diamond_backpack");
        this.add("sophisticatedbackpacks:netherite_backpack");
        this.add("improvedbackpacks:tiny_pocket");
        this.add("improvedbackpacks:medium_pocket");
        this.add("improvedbackpacks:large_pocket");
        this.add("improvedbackpacks:white_backpack");
        this.add("improvedbackpacks:orange_backpack");
        this.add("improvedbackpacks:magenta_backpack");
        this.add("improvedbackpacks:light_blue_backpack");
        this.add("improvedbackpacks:yellow_backpack");
        this.add("improvedbackpacks:lime_backpack");
        this.add("improvedbackpacks:pink_backpack");
        this.add("improvedbackpacks:gray_backpack");
        this.add("improvedbackpacks:light_gray_backpack");
        this.add("improvedbackpacks:cyan_backpack");
        this.add("improvedbackpacks:purple_backpack");
        this.add("improvedbackpacks:blue_backpack");
        this.add("improvedbackpacks:brown_backpack");
        this.add("improvedbackpacks:green_backpack");
        this.add("improvedbackpacks:red_backpack");
        this.add("improvedbackpacks:black_backpack");
        this.add("immersiveengineering:toolbox");
        this.add("immersiveengineering:crate");
        this.add("immersiveengineering:reinforced_crate");
        this.add("create:white_toolbox");
        this.add("create:orange_toolbox");
        this.add("create:magenta_toolbox");
        this.add("create:light_blue_toolbox");
        this.add("create:yellow_toolbox");
        this.add("create:lime_toolbox");
        this.add("create:pink_toolbox");
        this.add("create:gray_toolbox");
        this.add("create:light_gray_toolbox");
        this.add("create:cyan_toolbox");
        this.add("create:purple_toolbox");
        this.add("create:blue_toolbox");
        this.add("create:brown_toolbox");
        this.add("create:green_toolbox");
        this.add("create:red_toolbox");
        this.add("create:black_toolbox");
        this.add("mekanism:personal_chest");
        this.add("supplementaries:sack");
    }});

    public static void registerProviders() {
        ItemContainerProvidersListener.registerBuiltInProvider(id("backpack"), new SimpleItemProvider(9, 1, DyeColor.BROWN).filterContainerItems().disallowValues(DISALLOWED_ITEMS));
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation("backpacked", path);
    }
}
