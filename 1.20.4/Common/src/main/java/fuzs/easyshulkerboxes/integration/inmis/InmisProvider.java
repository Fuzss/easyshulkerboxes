package fuzs.easyshulkerboxes.integration.inmis;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fuzs.iteminteractions.api.v1.ContainerItemHelper;
import fuzs.iteminteractions.api.v1.provider.ItemContainerProvider;
import fuzs.iteminteractions.api.v1.provider.SimpleItemProvider;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.StreamSupport;

public class InmisProvider extends SimpleItemProvider {

    public InmisProvider(int inventoryWidth, int inventoryHeight, @Nullable DyeColor dyeColor, String... nbtKey) {
        super(inventoryWidth, inventoryHeight, dyeColor, nbtKey);
    }

    @Override
    public SimpleContainer getItemContainer(ItemStack containerStack, Player player, boolean allowSaving) {
        return ContainerItemHelper.INSTANCE.loadItemContainer(containerStack, this, items -> new SimpleInmisContainer(this.getInventorySize()), allowSaving, this.getNbtKey());
    }

    public static ItemContainerProvider fromJson(JsonElement jsonElement) {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        int inventoryWidth = GsonHelper.getAsInt(jsonObject, "inventory_width");
        int inventoryHeight = GsonHelper.getAsInt(jsonObject, "inventory_height");
        DyeColor dyeColor = DyeColor.byName(GsonHelper.getAsString(jsonObject, "background_color", ""), null);
        String[] nbtKey = GsonHelper.getAsString(jsonObject, "nbt_key", "Items").split("/");
        boolean filterContainerItems = GsonHelper.getAsBoolean(jsonObject, "filter_container_items", false);
        JsonArray disallowedItemsData = GsonHelper.getAsJsonArray(jsonObject, "disallowed_items", new JsonArray());
        List<String> disallowedItems = StreamSupport.stream(disallowedItemsData.spliterator(), false).map(JsonElement::getAsString).toList();
        InmisProvider provider = new InmisProvider(inventoryWidth, inventoryHeight, dyeColor, nbtKey);
        if (filterContainerItems) provider.filterContainerItems();
        provider.disallowValues(disallowedItems);
        return provider;
    }
}
