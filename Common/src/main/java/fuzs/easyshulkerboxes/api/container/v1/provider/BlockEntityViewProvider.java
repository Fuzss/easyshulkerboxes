package fuzs.easyshulkerboxes.api.container.v1.provider;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

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
    public NestedTagItemProvider disallowValues(Collection<String> value) {
        return this;
    }

    @Override
    public NestedTagItemProvider disallowValue(String value) {
        return this;
    }

    @Override
    public SimpleItemProvider filterContainerItems() {
        return this;
    }

    @Override
    public BlockEntityProvider anyGameMode() {
        return this;
    }

    @Override
    public boolean allowsPlayerInteractions(ItemStack containerStack, Player player) {
        return false;
    }
}
