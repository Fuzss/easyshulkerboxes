package fuzs.easyshulkerboxes.impl.client.handler;

import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.impl.handler.EnderChestMenuHandler;
import fuzs.easyshulkerboxes.impl.network.client.C2SEnderChestMenuMessage;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class EnderChestMenuClientHandler {

    public static void onEntityJoinLevel(Entity entity, Level level) {
        // client needs to notify server it has created its menu, otherwise server runs way too early and client isn't ready for syncing menu data
        if (entity instanceof LocalPlayer player && level.isClientSide) {
            EnderChestMenuHandler.openEnderChestMenu(player).ifPresent(menu -> {
                EasyShulkerBoxes.NETWORK.sendToServer(new C2SEnderChestMenuMessage());
            });
        }
    }
}
