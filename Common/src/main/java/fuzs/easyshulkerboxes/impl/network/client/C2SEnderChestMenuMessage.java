package fuzs.easyshulkerboxes.impl.network.client;

import fuzs.easyshulkerboxes.impl.handler.EnderChestMenuHandler;
import fuzs.easyshulkerboxes.impl.world.inventory.EnderChestSynchronizer;
import fuzs.puzzleslib.network.Message;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class C2SEnderChestMenuMessage implements Message<C2SEnderChestMenuMessage> {

    @Override
    public void write(FriendlyByteBuf buf) {

    }

    @Override
    public void read(FriendlyByteBuf buf) {

    }

    @Override
    public MessageHandler<C2SEnderChestMenuMessage> makeHandler() {
        return new MessageHandler<>() {

            @Override
            public void handle(C2SEnderChestMenuMessage message, Player player, Object gameInstance) {
                EnderChestMenuHandler.openEnderChestMenu(player).ifPresent(menu -> {
                    menu.setSynchronizer(new EnderChestSynchronizer((ServerPlayer) player));
                });
            }
        };
    }
}
