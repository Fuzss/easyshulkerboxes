package fuzs.easyshulkerboxes.impl.network;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import fuzs.easyshulkerboxes.impl.client.helper.ItemDecorationHelper;
import fuzs.easyshulkerboxes.impl.world.item.container.ItemContainerProviders;
import fuzs.puzzleslib.json.JsonConfigFileUtil;
import fuzs.puzzleslib.network.Message;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.Map;

public class S2CSyncItemContainerProvider implements Message<S2CSyncItemContainerProvider> {
    private Map<ResourceLocation, JsonElement> providers;

    public S2CSyncItemContainerProvider() {

    }

    public S2CSyncItemContainerProvider(Map<ResourceLocation, JsonElement> providers) {
        this.providers = providers;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeMap(this.providers, FriendlyByteBuf::writeResourceLocation, (friendlyByteBuf, jsonElement) -> {
            String json = JsonConfigFileUtil.GSON.toJson(jsonElement);
            friendlyByteBuf.writeUtf(json, 262144);
        });
    }

    @Override
    public void read(FriendlyByteBuf buf) {
        Map<ResourceLocation, JsonElement> map = buf.readMap(FriendlyByteBuf::readResourceLocation, friendlyByteBuf -> {
            String json = friendlyByteBuf.readUtf(262144);
            return JsonConfigFileUtil.GSON.fromJson(json, JsonElement.class);
        });
        this.providers = ImmutableMap.copyOf(map);
    }

    @Override
    public MessageHandler<S2CSyncItemContainerProvider> makeHandler() {
        return new MessageHandler<>() {

            @Override
            public void handle(S2CSyncItemContainerProvider message, Player player, Object gameInstance) {
                ItemContainerProviders.INSTANCE.buildProviders(message.providers);
                ItemDecorationHelper.clearCache();
            }
        };
    }
}
