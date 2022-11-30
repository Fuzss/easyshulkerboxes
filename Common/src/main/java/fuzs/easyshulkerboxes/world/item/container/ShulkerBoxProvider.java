package fuzs.easyshulkerboxes.world.item.container;

import fuzs.easyshulkerboxes.world.item.container.helper.ContainerItemHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ShulkerBoxProvider extends BlockEntityProvider {

    public ShulkerBoxProvider(ItemLike item) {
        this(getBackgroundColor(item));
    }

    public ShulkerBoxProvider(float[] backgroundColor) {
        super(BlockEntityType.SHULKER_BOX, 9, 3, backgroundColor);
    }

    @Override
    public boolean isItemAllowedInContainer(ItemStack containerStack, ItemStack stack) {
        return stack.getItem().canFitInsideContainerItems();
    }

    @Override
    protected boolean canProvideContainer(ItemStack stack, Player player) {
        // some mods make empty shulker boxes stackable, disable this mod then as it would allow for item duplication otherwise
        return stack.getCount() == 1;
    }

    private static float[] getBackgroundColor(ItemLike item) {
        DyeColor color = ShulkerBoxBlock.getColorFromItem(item.asItem());
        if (color != null) {
            return ContainerItemHelper.getBackgroundColor(color);
        } else {
            // beautiful lavender color from Tinted mod once again
            return new float[]{0.88235295F, 0.6901961F, 0.99607843F};
        }
    }
}
