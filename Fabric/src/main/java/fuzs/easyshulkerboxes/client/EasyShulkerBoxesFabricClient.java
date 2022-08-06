package fuzs.easyshulkerboxes.client;

import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.client.handler.EnderChestMenuClientHandler;
import fuzs.puzzleslib.client.core.ClientCoreServices;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;

public class EasyShulkerBoxesFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientCoreServices.FACTORIES.clientModConstructor(EasyShulkerBoxes.MOD_ID).accept(new EasyShulkerBoxesClient());
        registerHandlers();
    }

    private static void registerHandlers() {
        EnderChestMenuClientHandler enderChestMenuHandler = new EnderChestMenuClientHandler();
        ClientEntityEvents.ENTITY_LOAD.register(enderChestMenuHandler::onEntityJoinLevel);
    }
}
