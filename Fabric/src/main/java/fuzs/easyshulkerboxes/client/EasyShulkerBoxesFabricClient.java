package fuzs.easyshulkerboxes.client;

import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.api.client.handler.MouseScrollHandler;
import fuzs.easyshulkerboxes.client.handler.EnderChestMenuClientHandler;
import fuzs.easyshulkerboxes.config.ClientConfig;
import fuzs.easyshulkerboxes.config.ServerConfig;
import fuzs.puzzleslib.client.core.ClientFactories;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

public class EasyShulkerBoxesFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientFactories.INSTANCE.clientModConstructor(EasyShulkerBoxes.MOD_ID).accept(new EasyShulkerBoxesClient());
        registerHandlers();
    }

    private static void registerHandlers() {
        ClientEntityEvents.ENTITY_LOAD.register(EnderChestMenuClientHandler::onEntityJoinLevel);
        ScreenEvents.BEFORE_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof AbstractContainerScreen<?>) {
                ScreenMouseEvents.allowMouseScroll(screen).register((screen1, mouseX, mouseY, horizontalAmount, verticalAmount) -> {
                    return MouseScrollHandler.onMouseScroll(screen1, mouseX, mouseY, horizontalAmount, verticalAmount, EasyShulkerBoxes.CONFIG.get(ClientConfig.class), EasyShulkerBoxes.CONFIG.get(ServerConfig.class)).isEmpty();
                });
            }
        });
//        ClientTickEvents.END_CLIENT_TICK.register(minecraft -> MouseScrollHandler.onClientTick$End(minecraft, EasyShulkerBoxes.CONFIG.get(ClientConfig.class)));
    }
}
