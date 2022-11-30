package fuzs.easyshulkerboxes;

import com.mrcrayfish.backpacked.core.ModItems;
import fuzs.easyshulkerboxes.api.world.item.container.SerializableItemContainerProvider;
import fuzs.easyshulkerboxes.capability.ContainerSlotCapability;
import fuzs.easyshulkerboxes.capability.EnderChestMenuCapability;
import fuzs.easyshulkerboxes.data.ModLanguageProvider;
import fuzs.easyshulkerboxes.handler.EnderChestMenuHandler;
import fuzs.easyshulkerboxes.init.ModRegistry;
import fuzs.easyshulkerboxes.integration.BackpackedProvider;
import fuzs.easyshulkerboxes.world.item.storage.ItemContainerProviders;
import fuzs.puzzleslib.capability.ForgeCapabilityController;
import fuzs.puzzleslib.core.CommonFactories;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
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
        ForgeCapabilityController.setCapabilityToken(ModRegistry.CONTAINER_SLOT_CAPABILITY, new CapabilityToken<ContainerSlotCapability>() {});
    }

    private static void registerHandlers() {
        MinecraftForge.EVENT_BUS.addListener((final LivingEvent.LivingTickEvent evt) -> {
            EnderChestMenuHandler.onLivingTick(evt.getEntity());
        });
        MinecraftForge.EVENT_BUS.addListener((final AddReloadListenerEvent evt) -> {
            evt.addListener(ItemContainerProviders.INSTANCE);
        });
    }

    @SubscribeEvent
    public static void onCommonSetup(final FMLCommonSetupEvent evt) {
        SerializableItemContainerProvider.register(BackpackedProvider.class, new ResourceLocation(EasyShulkerBoxes.MOD_ID, "backpacked"), BackpackedProvider::fromJson);
        ItemContainerProviders.registerBuiltIn(ModItems.BACKPACK.get(), new BackpackedProvider());
    }

    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent evt) {
        DataGenerator generator = evt.getGenerator();
        final ExistingFileHelper existingFileHelper = evt.getExistingFileHelper();
        generator.addProvider(true, new ModLanguageProvider(generator, EasyShulkerBoxes.MOD_ID));
    }
}
