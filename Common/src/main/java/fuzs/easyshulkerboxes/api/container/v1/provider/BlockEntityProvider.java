package fuzs.easyshulkerboxes.api.container.v1.provider;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;

public class BlockEntityProvider extends SimpleItemProvider {
    private final ResourceLocation blockEntityTypeId;
    @Nullable
    private BlockEntityType<?> blockEntityType;
    private boolean anyGameMode;

    public BlockEntityProvider(BlockEntityType<?> blockEntityType, int inventoryWidth, int inventoryHeight) {
        this(Registry.BLOCK_ENTITY_TYPE.getKey(blockEntityType), inventoryWidth, inventoryHeight);
    }

    public BlockEntityProvider(ResourceLocation blockEntityTypeId, int inventoryWidth, int inventoryHeight) {
        super(inventoryWidth, inventoryHeight);
        this.blockEntityTypeId = blockEntityTypeId;
    }

    public BlockEntityProvider(BlockEntityType<?> blockEntityType, int inventoryWidth, int inventoryHeight, @Nullable DyeColor dyeColor, String... nbtKey) {
        this(Registry.BLOCK_ENTITY_TYPE.getKey(blockEntityType), inventoryWidth, inventoryHeight, dyeColor, nbtKey);
    }

    public BlockEntityProvider(ResourceLocation blockEntityTypeId, int inventoryWidth, int inventoryHeight, @Nullable DyeColor dyeColor, String... nbtKey) {
        super(inventoryWidth, inventoryHeight, dyeColor, nbtKey);
        this.blockEntityTypeId = blockEntityTypeId;
    }

    public static ItemContainerProvider shulkerBoxProvider(BlockEntityType<?> blockEntityType, int inventoryWidth, int inventoryHeight, @Nullable DyeColor dyeColor) {
        return new BlockEntityProvider(blockEntityType, inventoryWidth, inventoryHeight, dyeColor).anyGameMode().filterContainerItems();
    }

    public static ItemContainerProvider shulkerBoxProvider(ResourceLocation blockEntityTypeId, int inventoryWidth, int inventoryHeight, @Nullable DyeColor dyeColor) {
        return new BlockEntityProvider(blockEntityTypeId, inventoryWidth, inventoryHeight, dyeColor).anyGameMode().filterContainerItems();
    }

    public BlockEntityProvider anyGameMode() {
        this.anyGameMode = true;
        return this;
    }

    @Override
    public boolean allowsPlayerInteractions(ItemStack containerStack, Player player) {
        return super.allowsPlayerInteractions(containerStack, player) && (this.anyGameMode || player.getAbilities().instabuild);
    }

    @Override
    protected @Nullable CompoundTag getItemDataBase(ItemStack containerStack) {
        return BlockItem.getBlockEntityData(containerStack);
    }

    @Override
    protected void setItemDataToStack(ItemStack containerStack, @Nullable CompoundTag tag) {
        BlockItem.setBlockEntityData(containerStack, this.getBlockEntityType(), tag == null ? new CompoundTag() : tag);
    }

    public BlockEntityType<?> getBlockEntityType() {
        if (this.blockEntityType == null) {
            if (Registry.BLOCK_ENTITY_TYPE.containsKey(this.blockEntityTypeId)) {
                this.blockEntityType = Registry.BLOCK_ENTITY_TYPE.get(this.blockEntityTypeId);
            } else {
                throw new IllegalArgumentException("%s is not a valid block entity type".formatted(this.blockEntityTypeId));
            }
        }
        return this.blockEntityType;
    }

    @Override
    public void toJson(JsonObject jsonObject) {
        jsonObject.addProperty("block_entity_type", this.blockEntityTypeId.toString());
        if (this.anyGameMode) {
            jsonObject.addProperty("any_game_mode", true);
        }
        super.toJson(jsonObject);
    }
}
