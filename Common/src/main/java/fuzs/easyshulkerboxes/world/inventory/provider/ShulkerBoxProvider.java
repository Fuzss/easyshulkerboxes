package fuzs.easyshulkerboxes.world.inventory.provider;

import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.world.inventory.ContainerItemHelper;
import fuzs.easyshulkerboxes.config.ServerConfig;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Optional;

public class ShulkerBoxProvider extends ItemWithBlockEntityProvider {
    public static final ContainerItemProvider INSTANCE = new ShulkerBoxProvider();

    private ShulkerBoxProvider() {
        super(BlockEntityType.SHULKER_BOX, 9, 3);
    }

    @Override
    protected boolean canItemFitInside(ItemStack containerStack, ItemStack stack) {
        return stack.getItem().canFitInsideContainerItems();
    }

    @Override
    public boolean isAllowed() {
        return EasyShulkerBoxes.CONFIG.get(ServerConfig.class).allowShulkerBox;
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        DyeColor color = ShulkerBoxBlock.getColorFromItem(stack.getItem());
        if (color != null) {
            return ContainerItemHelper.getTooltipImage(stack, this.blockEntityType, this.inventoryWidth, this.inventoryHeight, color);
        } else {
            // beautiful lavender color from Tinted mod once again
            return ContainerItemHelper.getTooltipImageRaw(ContainerItemHelper.getTooltipContainer(stack, this.blockEntityType, this.getInventorySize()), this.inventoryWidth, this.inventoryHeight, new float[]{0.88235295F, 0.6901961F, 0.99607843F});
        }
    }
}
