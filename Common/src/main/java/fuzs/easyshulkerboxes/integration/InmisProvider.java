package fuzs.easyshulkerboxes.integration;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fuzs.easyshulkerboxes.api.world.item.container.ItemContainerProvider;
import fuzs.easyshulkerboxes.world.item.container.GenericItemContainerProvider;
import fuzs.easyshulkerboxes.world.item.container.helper.ContainerItemHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class InmisProvider extends GenericItemContainerProvider {
    private static final List<ResourceLocation> INMIS_BACKPACK_IDS = Stream.of("inmis:baby_backpack", "inmis:frayed_backpack", "inmis:plated_backpack", "inmis:gilded_backpack", "inmis:bejeweled_backpack", "inmis:blazing_backpack", "inmis:withered_backpack", "inmis:endless_backpack").map(ResourceLocation::new).toList();

    @Nullable
    private Set<Item> inmisBackpacks;

    public InmisProvider(int inventoryWidth, int inventoryHeight, @Nullable DyeColor backgroundColor, String... nbtKey) {
        super(inventoryWidth, inventoryHeight, backgroundColor, nbtKey);
    }

    @Override
    public boolean isItemAllowedInContainer(ItemStack containerStack, ItemStack stackToAdd) {
        return !this.isInmisBackpack(stackToAdd.getItem());
    }

    private boolean isInmisBackpack(Item item) {
        if (this.inmisBackpacks == null) {
            this.inmisBackpacks = INMIS_BACKPACK_IDS.stream()
                    .filter(Registry.ITEM::containsKey)
                    .map(Registry.ITEM::get)
                    .collect(ImmutableSet.toImmutableSet());
        }
        return this.inmisBackpacks.contains(item);
    }

    public static ItemContainerProvider fromJson(JsonElement jsonElement) {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        int inventoryWidth = GsonHelper.getAsInt(jsonObject, "inventory_width");
        int inventoryHeight = GsonHelper.getAsInt(jsonObject, "inventory_height");
        DyeColor dyeColor = null;
        if (jsonObject.has("background_color")) {
            dyeColor = DyeColor.byName(GsonHelper.getAsString(jsonObject, "background_color"), null);
        }
        String[] nbtKey = GsonHelper.getAsString(jsonObject, "nbt_key", ContainerItemHelper.TAG_ITEMS).split("/");
        return new InmisProvider(inventoryWidth, inventoryHeight, dyeColor, nbtKey);
    }
}
