package fuzs.easyshulkerboxes.api.world.inventory.tooltip;

import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record ContainerItemTooltip(NonNullList<ItemStack> items, int containerRows, @Nullable DyeColor backgroundColor) implements TooltipComponent {

}