package fuzs.easyshulkerboxes.integration.inmis;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fuzs.easyshulkerboxes.api.world.item.container.ItemContainerProvider;
import fuzs.easyshulkerboxes.world.item.container.SimpleItemProvider;
import fuzs.easyshulkerboxes.world.item.container.helper.ContainerItemHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class InmisProvider extends SimpleItemProvider {
    private static final List<ResourceLocation> INMIS_BACKPACK_IDS = Stream.of("baby_backpack", "frayed_backpack", "plated_backpack", "gilded_backpack", "bejeweled_backpack", "blazing_backpack", "withered_backpack", "endless_backpack")
            .map(InmisIntegration::id).toList();

    @Nullable
    private Set<Item> inmisBackpacks;

    public InmisProvider(int inventoryWidth, int inventoryHeight, @Nullable DyeColor backgroundColor) {
        super(inventoryWidth, inventoryHeight, backgroundColor, "Inventory");
    }

    @Override
    public SimpleContainer getItemContainer(ItemStack containerStack, Player player, boolean allowSaving) {
        return ContainerItemHelper.loadItemContainer(containerStack, this, items -> new SimpleInmisContainerWithSlots(this.getInventorySize()), allowSaving, this.getNbtKey());
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
        DyeColor dyeColor = DyeColor.byName(GsonHelper.getAsString(jsonObject, "background_color", ""), null);
        return new InmisProvider(inventoryWidth, inventoryHeight, dyeColor);
    }
}
