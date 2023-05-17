package fuzs.easyshulkerboxes.api.container.v1.tooltip;

import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public record ModBundleTooltip(NonNullList<ItemStack> items, boolean isBundleFull, float[] backgroundColor) implements TooltipComponent {

}