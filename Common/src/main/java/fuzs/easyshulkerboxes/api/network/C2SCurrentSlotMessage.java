package fuzs.easyshulkerboxes.api.network;

import fuzs.easyshulkerboxes.api.SimpleInventoryContainersApi;
import fuzs.easyshulkerboxes.api.world.item.container.ContainerSlotHelper;
import fuzs.puzzleslib.network.Message;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class C2SCurrentSlotMessage implements Message<C2SCurrentSlotMessage> {
    private int currentSlot;

    public C2SCurrentSlotMessage() {

    }

    public C2SCurrentSlotMessage(int currentSlot) {
        this.currentSlot = currentSlot;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeShort(this.currentSlot);
    }

    @Override
    public void read(FriendlyByteBuf buf) {
        this.currentSlot = buf.readShort();
    }

    @Override
    public MessageHandler<C2SCurrentSlotMessage> makeHandler() {
        return new MessageHandler<>() {

            @Override
            public void handle(C2SCurrentSlotMessage message, Player player, Object gameInstance) {
                AbstractContainerMenu containerMenu = player.containerMenu;
                if (!containerMenu.stillValid(player)) {
                    SimpleInventoryContainersApi.LOGGER.debug("Player {} interacted with invalid menu {}", player, containerMenu);
                    return;
                }
                if (message.currentSlot >= -1) {
                    ContainerSlotHelper.setCurrentContainerSlot(player, message.currentSlot);
                } else {
                    SimpleInventoryContainersApi.LOGGER.warn("{} tried to set an invalid current container item slot", player);
                }
            }
        };
    }
}
