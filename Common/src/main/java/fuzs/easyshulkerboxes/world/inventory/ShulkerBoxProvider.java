package fuzs.easyshulkerboxes.world.inventory;

import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.api.world.inventory.ContainerItemProvider;
import fuzs.easyshulkerboxes.api.world.item.container.ContainerItemHelper;
import fuzs.easyshulkerboxes.config.ServerConfig;
import fuzs.puzzleslib.proxy.Proxy;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Optional;

public class ShulkerBoxProvider extends ContainerItemProvider {
    public static final ContainerItemProvider INSTANCE = new ShulkerBoxProvider();

    private ShulkerBoxProvider() {

    }

    @Override
    public SimpleContainer getItemContainer(Player player, ItemStack stack, boolean allowSaving) {
        return ContainerItemHelper.loadItemContainer(stack, BlockEntityType.SHULKER_BOX, 27, allowSaving);
    }

    @Override
    protected boolean canItemFitInside(ItemStack containerStack, ItemStack stack) {
        return stack.getItem().canFitInsideContainerItems();
    }

    @Override
    protected boolean _canAcceptItem(ItemStack containerStack, ItemStack stack) {
        return this.getItemContainer(Proxy.INSTANCE.getClientPlayer(), containerStack, false).canAddItem(stack);
    }

    @Override
    protected int _getAcceptableItemCount(ItemStack containerStack, ItemStack stack) {
        return stack.getCount();
    }

    @Override
    public boolean isAllowed() {
        return EasyShulkerBoxes.CONFIG.get(ServerConfig.class).allowShulkerBox;
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        DyeColor color = ShulkerBoxBlock.getColorFromItem(stack.getItem());
        if (color != null) {
            return ContainerItemHelper.getTooltipImage(stack, BlockEntityType.SHULKER_BOX, 3, color);
        } else {
            // beautiful lavender color from Tinted mod once again
            return ContainerItemHelper.getTooltipImageWithColor(ContainerItemHelper.getTooltipContainer(stack, BlockEntityType.SHULKER_BOX, 27), 3, new float[]{0.88235295F, 0.6901961F, 0.99607843F});
        }
    }
}
