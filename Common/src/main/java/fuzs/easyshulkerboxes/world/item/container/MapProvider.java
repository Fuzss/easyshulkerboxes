package fuzs.easyshulkerboxes.world.item.container;

import fuzs.easyshulkerboxes.api.world.item.container.ItemContainerProvider;
import fuzs.easyshulkerboxes.world.inventory.tooltip.MapTooltip;
import fuzs.puzzleslib.proxy.Proxy;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

import java.util.Optional;

public class MapProvider implements ItemContainerProvider {

    @Override
    public Optional<SimpleContainer> getItemContainer(Player player, ItemStack stack, boolean allowSaving) {
        return Optional.empty();
    }

    @Override
    public boolean isItemAllowedInContainer(ItemStack containerStack, ItemStack stack) {
        return false;
    }

    @Override
    public boolean canAddItem(Player player, ItemStack containerStack, ItemStack stack) {
        return false;
    }

    @Override
    public int getAcceptableItemCount(Player player, ItemStack containerStack, ItemStack stack) {
        return 0;
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(Player player, ItemStack stack) {
        Level level = Proxy.INSTANCE.getClientLevel();
        if (level == null) return Optional.empty();
        Integer mapId = MapItem.getMapId(stack);
        MapItemSavedData savedData = MapItem.getSavedData(mapId, level);
        if (mapId == null || savedData == null) return Optional.empty();
        return Optional.of(new MapTooltip(mapId, savedData));
    }

    @Override
    public void broadcastContainerChanges(Player player) {

    }
}
