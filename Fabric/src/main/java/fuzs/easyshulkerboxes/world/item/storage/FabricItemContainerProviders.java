package fuzs.easyshulkerboxes.world.item.storage;

import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;

public class FabricItemContainerProviders extends ItemContainerProviders implements IdentifiableResourceReloadListener {

    @Override
    public ResourceLocation getFabricId() {
        return new ResourceLocation(EasyShulkerBoxes.MOD_ID, ITEM_CONTAINER_PROVIDERS_KEY);
    }
}
