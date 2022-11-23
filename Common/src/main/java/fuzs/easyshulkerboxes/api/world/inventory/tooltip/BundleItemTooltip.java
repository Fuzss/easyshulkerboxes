package fuzs.easyshulkerboxes.api.world.inventory.tooltip;

import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public record BundleItemTooltip(NonNullList<ItemStack> items, boolean isBundleFull) implements TooltipComponent {

}