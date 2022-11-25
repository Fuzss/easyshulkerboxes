package fuzs.easyshulkerboxes.api.world.inventory.tooltip;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public record MapTooltip(int mapId, MapItemSavedData savedData) implements TooltipComponent {

}
