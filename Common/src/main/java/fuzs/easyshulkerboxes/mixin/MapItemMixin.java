package fuzs.easyshulkerboxes.mixin;

import fuzs.easyshulkerboxes.api.world.inventory.tooltip.MapTooltip;
import fuzs.puzzleslib.proxy.Proxy;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ComplexItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Optional;

@Mixin(MapItem.class)
abstract class MapItemMixin extends ComplexItem {

    public MapItemMixin(Properties properties) {
        super(properties);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        Level level = Proxy.INSTANCE.getClientLevel();
        if (level == null) return Optional.empty();
        Integer mapId = MapItem.getMapId(stack);
        MapItemSavedData savedData = MapItem.getSavedData(mapId, level);
        if (mapId == null || savedData == null) return Optional.empty();
        return Optional.of(new MapTooltip(mapId, savedData));
    }
}
