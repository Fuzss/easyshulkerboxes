package fuzs.easyshulkerboxes.world.inventory.tooltip;

import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public record ContainerItemTooltip(NonNullList<ItemStack> items, int gridSizeX, int gridSizeY, float[] backgroundColor) implements TooltipComponent {

}