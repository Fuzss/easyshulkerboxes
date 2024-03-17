package fuzs.easyshulkerboxes.world.item.container;

import fuzs.easyshulkerboxes.world.inventory.tooltip.MapTooltip;
import fuzs.iteminteractions.api.v1.provider.ItemContainerProviderImpl;
import fuzs.puzzleslib.api.core.v1.Proxy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

import java.util.Optional;

public class MapProvider extends ItemContainerProviderImpl {

    @Override
    public boolean canProvideTooltipImage(ItemStack containerStack, Player player) {
        return true;
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack containerStack, Player player) {
        Level level = Proxy.INSTANCE.getClientLevel();
        if (level != null) {
            Integer mapId = MapItem.getMapId(containerStack);
            MapItemSavedData savedData = MapItem.getSavedData(mapId, level);
            if (savedData != null) {
                return Optional.of(new MapTooltip(mapId, savedData));
            }
        }

        return Optional.empty();
    }
}
