package fuzs.easyshulkerboxes.world.item.storage;

import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;

public class FabricItemContainerProvidersListener extends ItemContainerProvidersListener implements IdentifiableResourceReloadListener {

    @Override
    public ResourceLocation getFabricId() {
        return EasyShulkerBoxes.id(ITEM_CONTAINER_PROVIDERS_KEY);
    }
}
