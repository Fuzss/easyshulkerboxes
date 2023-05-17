package fuzs.easyshulkerboxes.impl.network;

import fuzs.easyshulkerboxes.impl.capability.EnderChestMenuCapability;
import fuzs.easyshulkerboxes.impl.init.ModRegistry;
import fuzs.puzzleslib.network.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class S2CEnderChestSetSlotMessage implements Message<S2CEnderChestSetSlotMessage> {
    public int stateId;
    public int slot;
    public ItemStack itemStack;

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
    public MessageHandler<S2CEnderChestSetSlotMessage> makeHandler() {
        return new MessageHandler<>() {

            @Override
            public void handle(S2CEnderChestSetSlotMessage message, Player player, Object gameInstance) {
                ((Minecraft) gameInstance).getTutorial().onGetItem(message.itemStack);
                ModRegistry.ENDER_CHEST_MENU_CAPABILITY.maybeGet(player).map(EnderChestMenuCapability::getEnderChestMenu).ifPresent(menu -> menu.setItem(message.slot, message.stateId, message.itemStack));
            }
        };
    }
}
