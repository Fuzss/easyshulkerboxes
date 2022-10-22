package fuzs.easyshulkerboxes.client;

import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.api.client.handler.MouseScrollHandler;
import fuzs.easyshulkerboxes.client.handler.EnderChestMenuClientHandler;
import fuzs.easyshulkerboxes.config.ClientConfig;
import fuzs.easyshulkerboxes.config.ServerConfig;
import fuzs.puzzleslib.client.core.ClientFactories;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
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
            MouseScrollHandler.onMouseScroll(evt.getScreen(), evt.getMouseX(), evt.getMouseY(), evt.getScrollDelta(), evt.getScrollDelta(), EasyShulkerBoxes.CONFIG.get(ClientConfig.class), EasyShulkerBoxes.CONFIG.get(ServerConfig.class)).ifPresent(unit -> evt.setCanceled(true));
        });
//        MinecraftForge.EVENT_BUS.addListener((final TickEvent.ClientTickEvent evt) -> {
//            if (evt.phase == TickEvent.Phase.END) MouseScrollHandler.onClientTick$End(Minecraft.getInstance(), EasyShulkerBoxes.CONFIG.get(ClientConfig.class));
//        });
    }
}
