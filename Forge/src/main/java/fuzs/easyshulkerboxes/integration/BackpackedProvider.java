package fuzs.easyshulkerboxes.integration;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mrcrayfish.backpacked.Config;
import fuzs.easyshulkerboxes.api.world.item.container.ItemContainerProvider;
import fuzs.easyshulkerboxes.world.item.container.GenericItemContainerProvider;
import fuzs.easyshulkerboxes.world.item.container.helper.ContainerItemHelper;
import net.minecraft.world.item.DyeColor;

public class BackpackedProvider extends GenericItemContainerProvider {

    public BackpackedProvider() {
        super(0, 0, DyeColor.BROWN, ContainerItemHelper.TAG_ITEMS);
    }

    @Override
    public int getInventoryWidth() {
        return Config.COMMON.backpackInventorySizeColumns.get();
    }

    @Override
    public int getInventoryHeight() {
        return Config.COMMON.backpackInventorySizeRows.get();
    }

    @Override
    public void toJson(JsonObject jsonObject) {

    }

    public static ItemContainerProvider fromJson(JsonElement jsonElement) {
        return new BackpackedProvider();
    }
}
