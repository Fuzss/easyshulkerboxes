package fuzs.easyshulkerboxes.world.item.container;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fuzs.easyshulkerboxes.api.world.item.container.ItemContainerProvider;
import fuzs.easyshulkerboxes.world.item.container.helper.ContainerItemHelper;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockEntityProvider extends GenericItemContainerProvider {
    protected final BlockEntityType<?> blockEntityType;

    public BlockEntityProvider(BlockEntityType<?> blockEntityType, int inventoryWidth, int inventoryHeight) {
        super(inventoryWidth, inventoryHeight);
        this.blockEntityType = blockEntityType;
    }

    public BlockEntityProvider(BlockEntityType<?> blockEntityType, int inventoryWidth, int inventoryHeight, @Nullable DyeColor backgroundColor, String nbtKey) {
        super(inventoryWidth, inventoryHeight, backgroundColor, nbtKey);
        this.blockEntityType = blockEntityType;
    }

    public BlockEntityProvider(BlockEntityType<?> blockEntityType, int inventoryWidth, int inventoryHeight, @NotNull float[] backgroundColor, String nbtKey) {
        super(inventoryWidth, inventoryHeight, backgroundColor, nbtKey);
        this.blockEntityType = blockEntityType;
    }

    public BlockEntityProvider(BlockEntityType<?> blockEntityType, int inventoryWidth, int inventoryHeight, String nbtKey) {
        super(inventoryWidth, inventoryHeight, nbtKey);
        this.blockEntityType = blockEntityType;
    }

    @Override
    public boolean canProvideContainer(ItemStack containerStack, Player player) {
        return super.canProvideContainer(containerStack, player) && player.getAbilities().instabuild;
    }

    @Override
    public SimpleContainer getItemContainer(ItemStack containerStack, Player player, boolean allowSaving) {
        return ContainerItemHelper.loadGenericItemContainer(containerStack, this.blockEntityType, this, this.getInventorySize(), allowSaving, this.nbtKey);
    }

    @Override
    public @Nullable CompoundTag getItemTag(ItemStack containerStack) {
        return BlockItem.getBlockEntityData(containerStack);
    }

    @Override
    public boolean canProvideTooltipImage(ItemStack containerStack, Player player) {
        return ContainerItemHelper.hasItemContainerTag(containerStack, this, this.nbtKey);
    }

    @Override
    public void toJson(JsonObject jsonObject) {
        super.toJson(jsonObject);
        jsonObject.addProperty("block_entity_type", Registry.BLOCK_ENTITY_TYPE.getKey(this.blockEntityType).toString());
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
        return new BlockEntityProvider(blockEntityType, inventoryWidth, inventoryHeight, dyeColor, nbtKey);
    }
}
