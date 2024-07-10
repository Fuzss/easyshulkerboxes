package fuzs.easyshulkerboxes.world.inventory.tooltip;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public record MapContentsTooltip(MapId mapId, MapItemSavedData savedData) implements TooltipComponent {

}
