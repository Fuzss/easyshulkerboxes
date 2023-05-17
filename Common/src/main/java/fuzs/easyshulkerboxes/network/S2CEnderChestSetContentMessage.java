package fuzs.easyshulkerboxes.network;

import fuzs.easyshulkerboxes.impl.capability.EnderChestMenuCapability;
import fuzs.easyshulkerboxes.client.world.inventory.EnderChestClientSynchronizer;
import fuzs.easyshulkerboxes.impl.init.ModRegistry;
import fuzs.puzzleslib.network.Message;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class S2CEnderChestSetContentMessage implements Message<S2CEnderChestSetContentMessage> {
    private int stateId;
    private List<ItemStack> items;
    private ItemStack carriedItem;

    public S2CEnderChestSetContentMessage() {
        
    }

    public S2CEnderChestSetContentMessage(int stateId, List<ItemStack> items, ItemStack carriedItem) {
        this.stateId = stateId;
        this.items = NonNullList.withSize(items.size(), ItemStack.EMPTY);

        for (int k = 0; k < items.size(); ++k) {
            this.items.set(k, items.get(k).copy());
        }

        this.carriedItem = carriedItem.copy();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(this.stateId);
        buf.writeCollection(this.items, FriendlyByteBuf::writeItem);
        buf.writeItem(this.carriedItem);
    }

    @Override
    public void read(FriendlyByteBuf buf) {
        this.stateId = buf.readVarInt();
        this.items = buf.readCollection(NonNullList::createWithCapacity, FriendlyByteBuf::readItem);
        this.carriedItem = buf.readItem();
    }

    @Override
    public MessageHandler<S2CEnderChestSetContentMessage> makeHandler() {
        return new MessageHandler<>() {

            @Override
            public void handle(S2CEnderChestSetContentMessage message, Player player, Object gameInstance) {
                ModRegistry.ENDER_CHEST_MENU_CAPABILITY.maybeGet(player).map(EnderChestMenuCapability::getEnderChestMenu).ifPresent(menu -> {
                    menu.initializeContents(message.stateId, message.items, message.carriedItem);
                    // run this here so all values sent from server are set as last state,
                    // so when client synchronizer is called only changes since sending full data will be sent
                    menu.broadcastChanges();
                    menu.setSynchronizer(new EnderChestClientSynchronizer());
                });
            }
        };
    }
}
