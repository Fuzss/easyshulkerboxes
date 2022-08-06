package fuzs.easyshulkerboxes.network.client.message;

import fuzs.easyshulkerboxes.capability.EnderChestMenuCapability;
import fuzs.easyshulkerboxes.init.ModRegistry;
import fuzs.easyshulkerboxes.network.message.S2CEnderChestSetSlotMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class C2SEnderChestSetSlotMessage extends S2CEnderChestSetSlotMessage {

    public C2SEnderChestSetSlotMessage() {

    }

    public C2SEnderChestSetSlotMessage(int stateId, int slot, ItemStack itemStack) {
        super(stateId, slot, itemStack);
    }

    @Override
    public PacketHandler<S2CEnderChestSetSlotMessage> makeHandler() {
        return new EnderChestSetSlotHandler();
    }

    private static class EnderChestSetSlotHandler extends PacketHandler<S2CEnderChestSetSlotMessage> {

        @Override
        public void handle(S2CEnderChestSetSlotMessage packet, Player player, Object gameInstance) {
            if (((ServerPlayer) player).gameMode.isCreative()) {
                ModRegistry.ENDER_CHEST_MENU_CAPABILITY.maybeGet(player)
                        .map(EnderChestMenuCapability::getEnderChestMenu)
                        .ifPresent(menu -> menu.setItem(packet.slot, packet.stateId, packet.itemStack));
            }
        }
    }
}
