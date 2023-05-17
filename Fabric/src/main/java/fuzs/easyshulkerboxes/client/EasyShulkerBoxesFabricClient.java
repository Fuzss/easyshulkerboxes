package fuzs.easyshulkerboxes.client;

import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.api.client.event.MouseDragEvents;
import fuzs.easyshulkerboxes.api.event.PlayLevelSoundEvents;
import fuzs.easyshulkerboxes.impl.client.handler.ClientInputActionHandler;
import fuzs.easyshulkerboxes.client.handler.EnderChestMenuClientHandler;
import fuzs.easyshulkerboxes.impl.client.handler.KeyBindingTogglesHandler;
import fuzs.easyshulkerboxes.impl.client.handler.MouseDraggingHandler;
import fuzs.puzzleslib.client.core.ClientFactories;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;

public class EasyShulkerBoxesFabricClient implements ClientModInitializer {
    private static final ResourceLocation BEFORE_PHASE = EasyShulkerBoxes.id("before");

    @Override
    public void onInitializeClient() {
        ClientFactories.INSTANCE.clientModConstructor(EasyShulkerBoxes.MOD_ID).accept(new EasyShulkerBoxesClient());
        registerHandlers();
    }

    private static void registerHandlers() {
        ClientEntityEvents.ENTITY_LOAD.register(EnderChestMenuClientHandler::onEntityJoinLevel);
        ScreenEvents.BEFORE_INIT.register((client, _screen, scaledWidth, scaledHeight) -> {
            if (_screen instanceof AbstractContainerScreen<?>) {
                ScreenMouseEvents.allowMouseScroll(_screen).register((screen, mouseX, mouseY, horizontalAmount, verticalAmount) -> {
                    return ClientInputActionHandler.onBeforeMouseScroll(screen, mouseX, mouseY, horizontalAmount, verticalAmount).isEmpty();
                });
                ScreenMouseEvents.allowMouseClick(_screen).register((Screen screen, double mouseX, double mouseY, int button) -> {
                    return MouseDraggingHandler.INSTANCE.onBeforeMousePressed(screen, mouseX, mouseY, button).isEmpty();
                });
                ScreenMouseEvents.allowMouseClick(_screen).addPhaseOrdering(BEFORE_PHASE, Event.DEFAULT_PHASE);
                ScreenMouseEvents.allowMouseClick(_screen).register(BEFORE_PHASE, (Screen screen, double mouseX, double mouseY, int button) -> {
                    return ClientInputActionHandler.onBeforeMousePressed(screen, mouseX, mouseY, button).isEmpty();
                });
                ScreenMouseEvents.allowMouseRelease(_screen).register((Screen screen, double mouseX, double mouseY, int button) -> {
                    return MouseDraggingHandler.INSTANCE.onBeforeMouseRelease(screen, mouseX, mouseY, button).isEmpty();
                });
                ScreenKeyboardEvents.allowKeyPress(_screen).register((Screen screen, int key, int scancode, int modifiers) -> {
                    return KeyBindingTogglesHandler.onBeforeKeyPressed(screen, key, scancode, modifiers).isEmpty();
                });
                ScreenKeyboardEvents.allowKeyPress(_screen).addPhaseOrdering(BEFORE_PHASE, Event.DEFAULT_PHASE);
                ScreenKeyboardEvents.allowKeyPress(_screen).register(BEFORE_PHASE, (Screen screen, int key, int scancode, int modifiers) -> {
                    return ClientInputActionHandler.onBeforeKeyPressed(screen, key, scancode, modifiers).isEmpty();
                });
                ScreenEvents.afterRender(_screen).register(ClientInputActionHandler::onAfterRender);
                ScreenMouseEvents.allowMouseRelease(_screen).register((Screen screen, double mouseX, double mouseY, int button) -> {
                    return ClientInputActionHandler.onBeforeMouseRelease(screen, mouseX, mouseY, button).isEmpty();
                });
            }
        });
        MouseDragEvents.BEFORE.register(MouseDraggingHandler.INSTANCE::onBeforeMouseDragged);
        PlayLevelSoundEvents.ENTITY.register(MouseDraggingHandler.INSTANCE::onPlaySoundAtPosition);
        PlayLevelSoundEvents.ENTITY.register(ClientInputActionHandler::onPlaySoundAtPosition);
    }
}
