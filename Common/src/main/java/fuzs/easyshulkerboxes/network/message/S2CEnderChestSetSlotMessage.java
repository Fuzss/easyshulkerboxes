package fuzs.easyshulkerboxes.network.message;

import fuzs.easyshulkerboxes.capability.EnderChestMenuCapability;
import fuzs.easyshulkerboxes.init.ModRegistry;
import fuzs.puzzleslib.network.message.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class S2CEnderChestSetSlotMessage implements Message<S2CEnderChestSetSlotMessage> {
    private int stateId;
    private int slot;
    private ItemStack itemStack;

    public S2CEnderChestSetSlotMessage() {
        
    }

    public S2CEnderChestSetSlotMessage(int stateId, int slot, ItemStack itemStack) {
        this.stateId = stateId;
        this.slot = slot;
        this.itemStack = itemStack.copy();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(this.stateId);
        buf.writeShort(this.slot);
        buf.writeItem(this.itemStack);
    }

    @Override
    public void read(FriendlyByteBuf buf) {
        this.stateId = buf.readVarInt();
        this.slot = buf.readShort();
        this.itemStack = buf.readItem();
    }

    @Override
    public PacketHandler<S2CEnderChestSetSlotMessage> makeHandler() {
        return new EnderChestSetSlotHandler();
    }

    private static class EnderChestSetSlotHandler extends PacketHandler<S2CEnderChestSetSlotMessage> {

        @Override
        public void handle(S2CEnderChestSetSlotMessage packet, Player player, Object gameInstance) {
            ((Minecraft) gameInstance).getTutorial().onGetItem(packet.itemStack);
            ModRegistry.ENDER_CHEST_MENU_CAPABILITY.maybeGet(player)
                    .map(EnderChestMenuCapability::getEnderChestMenu)
                    .ifPresent(menu -> menu.setItem(packet.slot, packet.stateId, packet.itemStack));
        }
    }
}
