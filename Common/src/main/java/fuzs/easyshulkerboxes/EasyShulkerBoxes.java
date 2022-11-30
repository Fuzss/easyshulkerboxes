package fuzs.easyshulkerboxes;

import fuzs.easyshulkerboxes.api.world.item.container.ItemContainerProvider;
import fuzs.easyshulkerboxes.config.ClientConfig;
import fuzs.easyshulkerboxes.config.ServerConfig;
import fuzs.easyshulkerboxes.init.ModRegistry;
import fuzs.easyshulkerboxes.network.S2CEnderChestSetContentMessage;
import fuzs.easyshulkerboxes.network.S2CEnderChestSetSlotMessage;
import fuzs.easyshulkerboxes.network.client.C2SCurrentSlotMessage;
import fuzs.easyshulkerboxes.network.client.C2SEnderChestMenuMessage;
import fuzs.easyshulkerboxes.network.client.C2SEnderChestSetSlotMessage;
import fuzs.easyshulkerboxes.world.item.container.*;
import fuzs.puzzleslib.config.ConfigHolder;
import fuzs.puzzleslib.core.CommonFactories;
import fuzs.puzzleslib.core.ModConstructor;
import fuzs.puzzleslib.network.MessageDirection;
import fuzs.puzzleslib.network.NetworkHandler;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class EasyShulkerBoxes implements ModConstructor {
    public static final String MOD_ID = "easyshulkerboxes";
    public static final String MOD_NAME = "Easy Shulker Boxes";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static final NetworkHandler NETWORK = CommonFactories.INSTANCE.network(MOD_ID);
    @SuppressWarnings("Convert2MethodRef")
    public static final ConfigHolder CONFIG = CommonFactories.INSTANCE
            .clientConfig(ClientConfig.class, () -> new ClientConfig())
            .serverConfig(ServerConfig.class, () -> new ServerConfig());

    @Override
    public void onConstructMod() {
        CONFIG.bakeConfigs(MOD_ID);
        ModRegistry.touch();
        registerMessages();
    }

    private static void registerMessages() {
        NETWORK.register(C2SCurrentSlotMessage.class, C2SCurrentSlotMessage::new, MessageDirection.TO_SERVER);
        NETWORK.register(S2CEnderChestSetContentMessage.class, S2CEnderChestSetContentMessage::new, MessageDirection.TO_CLIENT);
        NETWORK.register(S2CEnderChestSetSlotMessage.class, S2CEnderChestSetSlotMessage::new, MessageDirection.TO_CLIENT);
        NETWORK.register(C2SEnderChestSetSlotMessage.class, C2SEnderChestSetSlotMessage::new, MessageDirection.TO_SERVER);
        NETWORK.register(C2SEnderChestMenuMessage.class, C2SEnderChestMenuMessage::new, MessageDirection.TO_SERVER);
    }

    @Override
    public void onCommonSetup() {
        for (Map.Entry<ResourceKey<Block>, Block> entry : Registry.BLOCK.entrySet()) {
            // only affect vanilla shulker boxes, other mods might add shulker boxes with a different inventory size
            if (entry.getValue() instanceof ShulkerBoxBlock && entry.getKey().location().getNamespace().equals("minecraft")) {
                ItemContainerProvider.register(entry.getValue(), new ShulkerBoxProvider(entry.getValue()));
            }
        }
        ItemContainerProvider.register(Items.ENDER_CHEST, new EnderChestProvider());
        ItemContainerProvider.register(Items.BUNDLE, new BundleProvider());
        ItemContainerProvider.register(Items.DROPPER, new BlockEntityProvider(BlockEntityType.DROPPER, 3, 3));
        ItemContainerProvider.register(Items.DISPENSER, new BlockEntityProvider(BlockEntityType.DISPENSER, 3, 3));
        ItemContainerProvider.register(Items.CHEST, new BlockEntityProvider(BlockEntityType.CHEST, 9, 3));
    }
}
