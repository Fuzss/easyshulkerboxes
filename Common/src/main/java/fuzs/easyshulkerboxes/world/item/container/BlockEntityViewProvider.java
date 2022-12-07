package fuzs.easyshulkerboxes.world.item.container;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;

public class BlockEntityViewProvider extends BlockEntityProvider {

    public BlockEntityViewProvider(BlockEntityType<?> blockEntityType, int inventoryWidth, int inventoryHeight) {
        super(blockEntityType, inventoryWidth, inventoryHeight);
    }

    public BlockEntityViewProvider(ResourceLocation blockEntityTypeId, int inventoryWidth, int inventoryHeight) {
        super(blockEntityTypeId, inventoryWidth, inventoryHeight);
    }

    public BlockEntityViewProvider(BlockEntityType<?> blockEntityType, int inventoryWidth, int inventoryHeight, @Nullable DyeColor dyeColor, String... nbtKey) {
        super(blockEntityType, inventoryWidth, inventoryHeight, dyeColor, nbtKey);
    }

    public BlockEntityViewProvider(ResourceLocation blockEntityTypeId, int inventoryWidth, int inventoryHeight, @Nullable DyeColor dyeColor, String... nbtKey) {
        super(blockEntityTypeId, inventoryWidth, inventoryHeight, dyeColor, nbtKey);
    }

    @Override
    public boolean canProvideContainer(ItemStack containerStack, Player player) {
        return false;
    }
}
