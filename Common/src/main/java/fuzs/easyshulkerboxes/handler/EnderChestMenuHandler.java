package fuzs.easyshulkerboxes.handler;

import fuzs.easyshulkerboxes.capability.EnderChestMenuCapability;
import fuzs.easyshulkerboxes.init.ModRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;

import java.util.Optional;

public class EnderChestMenuHandler {

    public void onLivingTick(LivingEntity entity) {
        if (entity instanceof ServerPlayer) {
            // this should be ok to do every tick as only actual changes are sent
            // vanilla also does this every tick for the current menu (inventory most of the time)
            ModRegistry.ENDER_CHEST_MENU_CAPABILITY.maybeGet(entity)
                    .map(EnderChestMenuCapability::getEnderChestMenu)
                    .ifPresent(AbstractContainerMenu::broadcastChanges);
        }
    }

    public static Optional<AbstractContainerMenu> openEnderChestMenu(Player player) {
        return ModRegistry.ENDER_CHEST_MENU_CAPABILITY.maybeGet(player).map(capability -> {
            // container id doesn't matter since we do the syncing ourselves where the id is never used
            ChestMenu menu = ChestMenu.threeRows(-100, new Inventory(player), player.getEnderChestInventory());
            capability.setEnderChestMenu(menu);
            return menu;
        });
    }
}
