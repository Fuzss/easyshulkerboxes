package fuzs.easyshulkerboxes.world.item.container;

import com.mojang.serialization.MapCodec;
import fuzs.easyshulkerboxes.init.ModRegistry;
import fuzs.easyshulkerboxes.world.inventory.tooltip.MapContentsTooltip;
import fuzs.iteminteractions.api.v1.provider.impl.EmptyProvider;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

import java.util.Optional;

public class MapProvider extends EmptyProvider {
    public static final MapCodec<MapProvider> CODEC = MapCodec.unit(MapProvider::new);

    @Override
    public boolean canProvideTooltipImage(ItemStack containerStack, Player player) {
        return true;
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack containerStack, Player player) {
        MapId mapId = containerStack.get(DataComponents.MAP_ID);
        MapItemSavedData savedData = MapItem.getSavedData(mapId, player.level());
        if (savedData != null) {
            return Optional.of(new MapContentsTooltip(mapId, savedData));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Type getType() {
        return ModRegistry.MAP_ITEM_CONTENTS_PROVIDER_TYPE.value();
    }
}
