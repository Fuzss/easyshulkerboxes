package fuzs.easyshulkerboxes.world.inventory.provider;

import fuzs.easyshulkerboxes.world.inventory.helper.ContainerItemHelper;
import fuzs.puzzleslib.proxy.Proxy;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ItemWithBlockEntityProvider extends ContainerItemProvider {
    protected final BlockEntityType<?> blockEntityType;
    protected final int inventoryWidth;
    protected final int inventoryHeight;
    @Nullable
    private final DyeColor backgroundColor;

    public ItemWithBlockEntityProvider(BlockEntityType<?> blockEntityType, int inventoryWidth, int inventoryHeight) {
        this(blockEntityType, inventoryWidth, inventoryHeight, null);
    }

    public ItemWithBlockEntityProvider(BlockEntityType<?> blockEntityType, int inventoryWidth, int inventoryHeight, @Nullable DyeColor backgroundColor) {
        this.blockEntityType = blockEntityType;
        this.inventoryWidth = inventoryWidth;
        this.inventoryHeight = inventoryHeight;
        this.backgroundColor = backgroundColor;
    }

    public int getInventorySize() {
        return this.inventoryWidth * this.inventoryHeight;
    }

    @Override
    public SimpleContainer getItemContainer(Player player, ItemStack stack, boolean allowSaving) {
        return ContainerItemHelper.loadItemContainer(stack, this.blockEntityType, this.getInventorySize(), allowSaving);
    }

    @Override
    protected boolean internal$canAcceptItem(ItemStack containerStack, ItemStack stack) {
        return this.getItemContainer(Proxy.INSTANCE.getClientPlayer(), containerStack, false).canAddItem(stack);
    }

    @Override
    public boolean isAllowed() {
        return true;
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        return ContainerItemHelper.getTooltipImage(stack, this.blockEntityType, this.inventoryWidth, this.inventoryHeight, this.backgroundColor);
    }
}
