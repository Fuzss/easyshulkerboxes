package fuzs.easyshulkerboxes;

import fuzs.easyshulkerboxes.api.event.entity.living.LivingEvents;
import fuzs.easyshulkerboxes.handler.EnderChestMenuHandler;
import fuzs.puzzleslib.core.CoreServices;
import net.fabricmc.api.ModInitializer;
import net.minecraft.world.entity.LivingEntity;

public class EasyShulkerBoxesFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CoreServices.FACTORIES.modConstructor(EasyShulkerBoxes.MOD_ID).accept(new EasyShulkerBoxes());
        registerHandlers();
    }

    private static void registerHandlers() {
        EnderChestMenuHandler enderChestMenuHandler = new EnderChestMenuHandler();
        LivingEvents.TICK.register((LivingEntity entity) -> {
            enderChestMenuHandler.onLivingTick(entity);
            return true;
        });
    }
}
