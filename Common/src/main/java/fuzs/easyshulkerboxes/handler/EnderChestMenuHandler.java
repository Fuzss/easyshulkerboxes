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
            ModRegistry.ENDER_CHEST_MENU_CAPABILITY.maybeGet(entity)
                    .map(EnderChestMenuCapability::getEnderChestMenu)
                    .ifPresent(AbstractContainerMenu::broadcastChanges);
        }
    }

    public static Optional<AbstractContainerMenu> openEnderChestMenu(Player player) {
        return ModRegistry.ENDER_CHEST_MENU_CAPABILITY.maybeGet(player).map(capability -> {
            ChestMenu menu = ChestMenu.threeRows(-100, new Inventory(player), player.getEnderChestInventory());
            capability.setEnderChestMenu(menu);
            return menu;
        });
    }
}
