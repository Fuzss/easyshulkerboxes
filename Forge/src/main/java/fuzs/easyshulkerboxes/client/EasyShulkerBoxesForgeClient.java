package fuzs.easyshulkerboxes.client;

import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.client.handler.EnderChestMenuClientHandler;
import fuzs.easyshulkerboxes.client.handler.KeyBindingTogglesHandler;
import fuzs.easyshulkerboxes.client.handler.MouseDragHandler;
import fuzs.easyshulkerboxes.client.handler.ClientInputActionHandler;
import fuzs.puzzleslib.client.core.ClientFactories;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.PlayLevelSoundEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod.EventBusSubscriber(modid = EasyShulkerBoxes.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EasyShulkerBoxesForgeClient {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ClientFactories.INSTANCE.clientModConstructor(EasyShulkerBoxes.MOD_ID).accept(new EasyShulkerBoxesClient());
        registerHandlers();
    }

    private static void registerHandlers() {
        MinecraftForge.EVENT_BUS.addListener((final EntityJoinLevelEvent evt) -> {
            EnderChestMenuClientHandler.onEntityJoinLevel(evt.getEntity(), evt.getLevel());
        });
        MinecraftForge.EVENT_BUS.addListener((final ScreenEvent.MouseScrolled.Pre evt) -> {
            ClientInputActionHandler.onBeforeMouseScroll(evt.getScreen(), evt.getMouseX(), evt.getMouseY(), evt.getScrollDelta(), evt.getScrollDelta()).ifPresent(unit -> evt.setCanceled(true));
        });
        MinecraftForge.EVENT_BUS.addListener((final ScreenEvent.MouseButtonPressed.Pre evt) -> {
            MouseDragHandler.INSTANCE.onBeforeMousePressed(evt.getScreen(), evt.getMouseX(), evt.getMouseY(), evt.getButton()).ifPresent(unit -> evt.setCanceled(true));
        });
        MinecraftForge.EVENT_BUS.addListener((final ScreenEvent.MouseDragged.Pre evt) -> {
            MouseDragHandler.INSTANCE.onBeforeMouseDragged(evt.getScreen(), evt.getMouseX(), evt.getMouseY(), evt.getMouseButton(), evt.getDragX(), evt.getDragY()).ifPresent(unit -> evt.setCanceled(true));
        });
        MinecraftForge.EVENT_BUS.addListener((final ScreenEvent.MouseButtonReleased.Pre evt) -> {
            MouseDragHandler.INSTANCE.onBeforeMouseRelease(evt.getScreen(), evt.getMouseX(), evt.getMouseY(), evt.getButton()).ifPresent(unit -> evt.setCanceled(true));
        });
        MinecraftForge.EVENT_BUS.addListener((final ScreenEvent.KeyPressed.Pre evt) -> {
            KeyBindingTogglesHandler.onBeforeKeyPressed(evt.getScreen(), evt.getKeyCode(), evt.getScanCode(), evt.getModifiers()).ifPresent(unit -> evt.setCanceled(true));
        });
        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, (final ScreenEvent.MouseButtonPressed.Pre evt) -> {
            ClientInputActionHandler.onBeforeMousePressed(evt.getScreen(), evt.getMouseX(), evt.getMouseY(), evt.getButton()).ifPresent(unit -> evt.setCanceled(true));
        });
        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, (final ScreenEvent.KeyPressed.Pre evt) -> {
            ClientInputActionHandler.onBeforeKeyPressed(evt.getScreen(), evt.getKeyCode(), evt.getScanCode(), evt.getModifiers()).ifPresent(unit -> evt.setCanceled(true));
        });
        MinecraftForge.EVENT_BUS.addListener((final ScreenEvent.Render.Post evt) -> {
            ClientInputActionHandler.onAfterRender(evt.getScreen(), evt.getPoseStack(), evt.getMouseX(), evt.getMouseY(), evt.getPartialTick());
        });
        MinecraftForge.EVENT_BUS.addListener((final PlayLevelSoundEvent.AtEntity evt) -> {
            MouseDragHandler.INSTANCE.onPlaySoundAtPosition(evt.getEntity(), evt.getSound(), evt.getSource(), evt.getOriginalVolume(), evt.getOriginalPitch()).ifPresent(unit -> evt.setCanceled(true));
        });
    }
}
