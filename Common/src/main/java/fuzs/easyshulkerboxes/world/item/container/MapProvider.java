package fuzs.easyshulkerboxes.world.item.container;

import com.google.gson.JsonObject;
import fuzs.easyshulkerboxes.api.world.item.container.ItemContainerProvider;
import fuzs.easyshulkerboxes.world.inventory.tooltip.MapTooltip;
import fuzs.puzzleslib.proxy.Proxy;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MapProvider implements ItemContainerProvider {

    @Override
    public boolean canPlayerUseContainer(ItemStack containerStack, Player player) {
        return false;
    }

    @Override
    public SimpleContainer getItemContainer(ItemStack containerStack, Player player, boolean allowSaving) {
        return null;
    }

    @Override
    public boolean hasItemContainerData(ItemStack containerStack) {
        return false;
    }

    @Override
    public @Nullable CompoundTag getItemContainerData(ItemStack containerStack) {
        return null;
    }

    @Override
    public void setItemContainerData(ItemStack containerStack, ListTag itemsTag, String nbtKey) {

    }

    @Override
    public void broadcastContainerChanges(Player player) {

    }

    @Override
    public boolean isItemAllowedInContainer(ItemStack containerStack, ItemStack stackToAdd) {
        return false;
    }

    @Override
    public boolean canAddItem(ItemStack containerStack, ItemStack stackToAdd, Player player) {
        return false;
    }

    @Override
    public int getAcceptableItemCount(ItemStack containerStack, ItemStack stackToAdd, Player player) {
        return 0;
    }

    @Override
    public boolean canProvideTooltipImage(ItemStack containerStack, Player player) {
        return true;
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack containerStack, Player player) {
        Level level = Proxy.INSTANCE.getClientLevel();
        if (level == null) return Optional.empty();
        Integer mapId = MapItem.getMapId(containerStack);
        MapItemSavedData savedData = MapItem.getSavedData(mapId, level);
        if (mapId == null || savedData == null) return Optional.empty();
        return Optional.of(new MapTooltip(mapId, savedData));
    }

    @Override
    public void toJson(JsonObject jsonObject) {

    }
}
