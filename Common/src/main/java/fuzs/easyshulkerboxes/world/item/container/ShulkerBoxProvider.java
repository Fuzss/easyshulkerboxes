package fuzs.easyshulkerboxes.world.item.container;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fuzs.easyshulkerboxes.api.world.item.container.ItemContainerProvider;
import fuzs.easyshulkerboxes.api.world.item.container.SerializableItemContainerProvider;
import fuzs.easyshulkerboxes.world.item.container.helper.ContainerItemHelper;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;

public class ShulkerBoxProvider extends BlockEntityProvider {
    private static final float[] DEFAULT_SHULKER_BOX_COLOR = {0.88235295F, 0.6901961F, 0.99607843F};

    public ShulkerBoxProvider(@NotNull DyeColor color) {
        super(BlockEntityType.SHULKER_BOX, 9, 3, color, ContainerItemHelper.TAG_ITEMS);
    }

    public ShulkerBoxProvider() {
        super(BlockEntityType.SHULKER_BOX, 9, 3, DEFAULT_SHULKER_BOX_COLOR, ContainerItemHelper.TAG_ITEMS);
    }

    @Override
    public boolean isItemAllowedInContainer(ItemStack containerStack, ItemStack stackToAdd) {
        return stackToAdd.getItem().canFitInsideContainerItems();
    }

    @Override
    public boolean canProvideContainer(ItemStack containerStack, Player player) {
        // some mods make empty shulker boxes stackable, disable this mod then as it would allow for item duplication otherwise
        return containerStack.getCount() == 1;
    }

    @Override
    public void toJson(JsonObject jsonObject) {
        if (this.getDyeColor() != null) {
            jsonObject.addProperty("background_color", this.getDyeColor().getName());
        }
    }

    public static ItemContainerProvider fromJson(JsonElement jsonElement) {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        DyeColor dyeColor = null;
        if (jsonObject.has("background_color")) {
            dyeColor = DyeColor.byName(GsonHelper.getAsString(jsonObject, "background_color"), null);
        }
        return create(dyeColor);
    }

    public static SerializableItemContainerProvider create(ItemLike item) {
        return create(ShulkerBoxBlock.getColorFromItem(item.asItem()));
    }

    public static SerializableItemContainerProvider create(DyeColor color) {
        if (color != null) {
            return new ShulkerBoxProvider(color);
        }
        return new ShulkerBoxProvider();
    }

    private static float[] getBackgroundColor(ItemLike item) {
        DyeColor color = ShulkerBoxBlock.getColorFromItem(item.asItem());
        if (color != null) {
            return ContainerItemHelper.getBackgroundColor(color);
        } else {
            // beautiful lavender color from Tinted mod once again
            return DEFAULT_SHULKER_BOX_COLOR;
        }
    }
}
