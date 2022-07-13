package fuzs.easyshulkerboxes;

import fuzs.easyshulkerboxes.data.ModLanguageProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod(EasyShulkerBoxes.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EasyShulkerBoxesForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        EasyShulkerBoxes.onConstructMod();
    }

    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent evt) {
        DataGenerator generator = evt.getGenerator();
        final ExistingFileHelper existingFileHelper = evt.getExistingFileHelper();
        generator.addProvider(true, new ModLanguageProvider(generator, EasyShulkerBoxes.MOD_ID));
    }
}
