package fuzs.easyshulkerboxes;

import fuzs.easyshulkerboxes.api.event.entity.living.LivingEvents;
import fuzs.easyshulkerboxes.config.CommonConfig;
import fuzs.easyshulkerboxes.handler.EnderChestMenuHandler;
import fuzs.easyshulkerboxes.integration.SimpleBackpackIntegration;
import fuzs.easyshulkerboxes.world.item.storage.ItemContainerProviders;
import fuzs.puzzleslib.core.CommonFactories;
import fuzs.puzzleslib.core.ModLoaderEnvironment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.entity.LivingEntity;

public class EasyShulkerBoxesFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        // do this first as on Fabric common setup runs immediately, and there we do custom data gen
        registerIntegration();
        CommonFactories.INSTANCE.modConstructor(EasyShulkerBoxes.MOD_ID).accept(new EasyShulkerBoxes());
        registerHandlers();
    }

    private static void registerIntegration() {
        // needs no config, no classes are referenced
        if (ModLoaderEnvironment.INSTANCE.isModLoaded("simple_backpack")) {
            SimpleBackpackIntegration.registerContents();
        }
    }

    private static void registerHandlers() {
        LivingEvents.TICK.register((LivingEntity entity) -> {
            EnderChestMenuHandler.onLivingTick(entity);
            return true;
        });
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener((IdentifiableResourceReloadListener) ItemContainerProviders.INSTANCE);
        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register((player, joined) -> {
            ItemContainerProviders.INSTANCE.sendProvidersToPlayer(player);
        });
    }
}
