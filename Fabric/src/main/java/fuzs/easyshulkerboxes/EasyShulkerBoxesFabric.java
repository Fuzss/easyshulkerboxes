package fuzs.easyshulkerboxes;

import fuzs.easyshulkerboxes.api.event.entity.living.LivingEvents;
import fuzs.easyshulkerboxes.core.FabricResourceReloadListener;
import fuzs.easyshulkerboxes.impl.handler.EnderChestMenuHandler;
import fuzs.easyshulkerboxes.impl.world.item.container.ItemContainerProviders;
import fuzs.puzzleslib.core.CommonFactories;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.entity.LivingEntity;

public class EasyShulkerBoxesFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CommonFactories.INSTANCE.modConstructor(EasyShulkerBoxes.MOD_ID).accept(new EasyShulkerBoxes());
        registerHandlers();
    }

    private static void registerHandlers() {
        LivingEvents.TICK.register((LivingEntity entity) -> {
            EnderChestMenuHandler.onLivingTick(entity);
            return true;
        });
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new FabricResourceReloadListener(EasyShulkerBoxes.id(ItemContainerProviders.ITEM_CONTAINER_PROVIDERS_KEY), ItemContainerProviders.INSTANCE));
        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register((player, joined) -> {
            ItemContainerProviders.INSTANCE.sendProvidersToPlayer(player);
        });
    }
}
