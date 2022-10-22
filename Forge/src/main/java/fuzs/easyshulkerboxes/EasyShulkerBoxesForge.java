package fuzs.easyshulkerboxes;

import fuzs.easyshulkerboxes.api.SimpleInventoryContainersApi;
import fuzs.easyshulkerboxes.api.capability.ContainerSlotCapability;
import fuzs.easyshulkerboxes.capability.EnderChestMenuCapability;
import fuzs.easyshulkerboxes.data.ModLanguageProvider;
import fuzs.easyshulkerboxes.handler.EnderChestMenuHandler;
import fuzs.easyshulkerboxes.init.ModRegistry;
import fuzs.puzzleslib.capability.ForgeCapabilityController;
import fuzs.puzzleslib.core.CommonFactories;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod(EasyShulkerBoxes.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EasyShulkerBoxesForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        CommonFactories.INSTANCE.modConstructor(EasyShulkerBoxes.MOD_ID).accept(new SimpleInventoryContainersApi());
        CommonFactories.INSTANCE.modConstructor(EasyShulkerBoxes.MOD_ID).accept(new EasyShulkerBoxes());
        registerCapabilities();
        registerHandlers();
    }

    private static void registerCapabilities() {
        ForgeCapabilityController.setCapabilityToken(ModRegistry.ENDER_CHEST_MENU_CAPABILITY, new CapabilityToken<EnderChestMenuCapability>() {});
        ForgeCapabilityController.setCapabilityToken(fuzs.easyshulkerboxes.api.init.ModRegistry.CONTAINER_SLOT_CAPABILITY, new CapabilityToken<ContainerSlotCapability>() {});
    }

    private static void registerHandlers() {
        EnderChestMenuHandler enderChestMenuHandler = new EnderChestMenuHandler();
        MinecraftForge.EVENT_BUS.addListener((final LivingEvent.LivingTickEvent evt) -> {
            enderChestMenuHandler.onLivingTick(evt.getEntity());
        });
    }

    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent evt) {
        DataGenerator generator = evt.getGenerator();
        final ExistingFileHelper existingFileHelper = evt.getExistingFileHelper();
        generator.addProvider(true, new ModLanguageProvider(generator, EasyShulkerBoxes.MOD_ID));
    }
}
