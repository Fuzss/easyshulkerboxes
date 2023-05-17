package fuzs.easyshulkerboxes;

import fuzs.easyshulkerboxes.data.ModItemContainerProvider;
import fuzs.easyshulkerboxes.data.ModLanguageProvider;
import fuzs.easyshulkerboxes.impl.handler.EnderChestMenuHandler;
import fuzs.easyshulkerboxes.impl.capability.ContainerClientInputCapability;
import fuzs.easyshulkerboxes.impl.capability.EnderChestMenuCapability;
import fuzs.easyshulkerboxes.impl.init.ModRegistry;
import fuzs.easyshulkerboxes.impl.world.item.container.ItemContainerProviders;
import fuzs.puzzleslib.capability.ForgeCapabilityController;
import fuzs.puzzleslib.core.CommonFactories;
import net.minecraft.data.DataGenerator;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod(EasyShulkerBoxes.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EasyShulkerBoxesForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        CommonFactories.INSTANCE.modConstructor(EasyShulkerBoxes.MOD_ID).accept(new EasyShulkerBoxes());
        registerCapabilities();
        registerHandlers();
    }

    private static void registerCapabilities() {
        ForgeCapabilityController.setCapabilityToken(ModRegistry.ENDER_CHEST_MENU_CAPABILITY, new CapabilityToken<EnderChestMenuCapability>() {});
        ForgeCapabilityController.setCapabilityToken(ModRegistry.CONTAINER_SLOT_CAPABILITY, new CapabilityToken<ContainerClientInputCapability>() {});
    }

    private static void registerHandlers() {
        MinecraftForge.EVENT_BUS.addListener((final LivingEvent.LivingTickEvent evt) -> {
            EnderChestMenuHandler.onLivingTick(evt.getEntity());
        });
        MinecraftForge.EVENT_BUS.addListener((final AddReloadListenerEvent evt) -> {
            evt.addListener(ItemContainerProviders.INSTANCE);
        });
        MinecraftForge.EVENT_BUS.addListener((final OnDatapackSyncEvent evt) -> {
            ServerPlayer player = evt.getPlayer();
            if (player != null) {
                ItemContainerProviders.INSTANCE.sendProvidersToPlayer(player);
            } else {
                for (ServerPlayer serverPlayer : evt.getPlayerList().getPlayers()) {
                    ItemContainerProviders.INSTANCE.sendProvidersToPlayer(serverPlayer);
                }
            }
        });
    }

    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent evt) {
        DataGenerator generator = evt.getGenerator();
        final ExistingFileHelper existingFileHelper = evt.getExistingFileHelper();
        generator.addProvider(true, new ModItemContainerProvider(generator));
        generator.addProvider(true, new ModLanguageProvider(generator, EasyShulkerBoxes.MOD_ID));
    }
}
