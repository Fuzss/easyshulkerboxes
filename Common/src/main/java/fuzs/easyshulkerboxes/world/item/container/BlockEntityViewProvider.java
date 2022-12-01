package fuzs.easyshulkerboxes.world.item.container;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fuzs.easyshulkerboxes.api.world.item.container.ItemContainerProvider;
import fuzs.easyshulkerboxes.world.item.container.helper.ContainerItemHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockEntityViewProvider extends BlockEntityProvider {

    public BlockEntityViewProvider(BlockEntityType<?> blockEntityType, int inventoryWidth, int inventoryHeight) {
        super(blockEntityType, inventoryWidth, inventoryHeight);
    }

    public BlockEntityViewProvider(BlockEntityType<?> blockEntityType, int inventoryWidth, int inventoryHeight, @Nullable DyeColor backgroundColor, String nbtKey) {
        super(blockEntityType, inventoryWidth, inventoryHeight, backgroundColor, nbtKey);
    }

    public BlockEntityViewProvider(BlockEntityType<?> blockEntityType, int inventoryWidth, int inventoryHeight, @NotNull float[] backgroundColor, String nbtKey) {
        super(blockEntityType, inventoryWidth, inventoryHeight, backgroundColor, nbtKey);
    }

    public BlockEntityViewProvider(BlockEntityType<?> blockEntityType, int inventoryWidth, int inventoryHeight, String nbtKey) {
        super(blockEntityType, inventoryWidth, inventoryHeight, nbtKey);
    }

    @Override
    public boolean canProvideContainer(ItemStack containerStack, Player player) {
        return false;
    }

    public static ItemContainerProvider fromJson(JsonElement jsonElement) {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        int inventoryWidth = GsonHelper.getAsInt(jsonObject, "inventory_width");
        int inventoryHeight = GsonHelper.getAsInt(jsonObject, "inventory_height");
        DyeColor dyeColor = null;
        if (jsonObject.has("background_color")) {
            dyeColor = DyeColor.byName(GsonHelper.getAsString(jsonObject, "background_color"), null);
        }
        String nbtKey = GsonHelper.getAsString(jsonObject, "nbt_key", ContainerItemHelper.TAG_ITEMS);
        ResourceLocation blockEntityTypeKey = new ResourceLocation(GsonHelper.getAsString(jsonObject, "block_entity_type"));
        BlockEntityType<?> blockEntityType = Registry.BLOCK_ENTITY_TYPE.get(blockEntityTypeKey);
        return new BlockEntityViewProvider(blockEntityType, inventoryWidth, inventoryHeight, dyeColor, nbtKey);
    }
}
