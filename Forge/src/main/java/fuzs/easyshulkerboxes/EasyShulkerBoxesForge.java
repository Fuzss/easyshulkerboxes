package fuzs.easyshulkerboxes;

import fuzs.easyshulkerboxes.data.ModLanguageProvider;
import fuzs.easyshulkerboxes.handler.EnderChestMenuHandler;
import fuzs.easyshulkerboxes.init.ForgeModRegistry;
import fuzs.puzzleslib.core.CoreServices;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod(EasyShulkerBoxes.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EasyShulkerBoxesForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        CoreServices.FACTORIES.modConstructor(EasyShulkerBoxes.MOD_ID).accept(new EasyShulkerBoxes());
        ForgeModRegistry.touch();
        registerHandlers();
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
