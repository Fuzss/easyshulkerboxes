package fuzs.easyshulkerboxes.world.item.container;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;

public class ShulkerBoxProvider extends BlockEntityProvider {
    private static final float[] DEFAULT_SHULKER_BOX_COLOR = {0.88235295F, 0.6901961F, 0.99607843F};

    public ShulkerBoxProvider(BlockEntityType<?> blockEntityType, int inventoryWidth, int inventoryHeight) {
        super(blockEntityType, inventoryWidth, inventoryHeight);
    }

    public ShulkerBoxProvider(ResourceLocation blockEntityTypeId, int inventoryWidth, int inventoryHeight) {
        super(blockEntityTypeId, inventoryWidth, inventoryHeight);
    }

    public ShulkerBoxProvider(BlockEntityType<?> blockEntityType, int inventoryWidth, int inventoryHeight, @Nullable DyeColor dyeColor, String... nbtKey) {
        super(blockEntityType, inventoryWidth, inventoryHeight, dyeColor, nbtKey);
    }

    public ShulkerBoxProvider(ResourceLocation blockEntityTypeId, int inventoryWidth, int inventoryHeight, @Nullable DyeColor dyeColor, String... nbtKey) {
        super(blockEntityTypeId, inventoryWidth, inventoryHeight, dyeColor, nbtKey);
    }

    @Override
    public SimpleItemProvider filterContainerItems() {
        return this;
    }

    @Override
    protected float[] getDefaultBackgroundColor() {
        return DEFAULT_SHULKER_BOX_COLOR;
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
}
