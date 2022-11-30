package fuzs.easyshulkerboxes.world.item.container;

import fuzs.easyshulkerboxes.world.item.container.helper.ContainerItemHelper;
import fuzs.easyshulkerboxes.world.inventory.tooltip.ContainerItemTooltip;
import net.minecraft.core.NonNullList;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;

public class BlockEntityProvider extends ItemContainerProviderImpl {
    protected final BlockEntityType<?> blockEntityType;
    protected final int inventoryWidth;
    protected final int inventoryHeight;
    private final float[] backgroundColor;

    public BlockEntityProvider(BlockEntityType<?> blockEntityType, int inventoryWidth, int inventoryHeight) {
        this(blockEntityType, inventoryWidth, inventoryHeight, (DyeColor) null);
    }

    public BlockEntityProvider(BlockEntityType<?> blockEntityType, int inventoryWidth, int inventoryHeight, @Nullable DyeColor backgroundColor) {
        this(blockEntityType, inventoryWidth, inventoryHeight, ContainerItemHelper.getBackgroundColor(backgroundColor));
    }

    public BlockEntityProvider(BlockEntityType<?> blockEntityType, int inventoryWidth, int inventoryHeight, float[] backgroundColor) {
        this.blockEntityType = blockEntityType;
        this.inventoryWidth = inventoryWidth;
        this.inventoryHeight = inventoryHeight;
        this.backgroundColor = backgroundColor;
    }

    public int getInventorySize() {
        return this.inventoryWidth * this.inventoryHeight;
    }

    @Override
    protected boolean canProvideContainer(ItemStack stack, Player player) {
        return super.canProvideContainer(stack, player) && player.getAbilities().instabuild;
    }

    @Override
    protected SimpleContainer internal$getItemContainer(Player player, ItemStack stack, boolean allowSaving) {
        return ContainerItemHelper.loadGenericItemContainer(stack, this.blockEntityType, this.getInventorySize(), allowSaving);
    }

    @Override
    public boolean canProvideTooltipImage(ItemStack stack) {
        return ContainerItemHelper.hasItemContainerTag(stack, this.blockEntityType);
    }

    @Override
    protected TooltipComponent internal$getTooltipImage(ItemStack stack, NonNullList<ItemStack> items) {
        return new ContainerItemTooltip(items, this.inventoryWidth, this.inventoryHeight, this.backgroundColor);
    }
}
