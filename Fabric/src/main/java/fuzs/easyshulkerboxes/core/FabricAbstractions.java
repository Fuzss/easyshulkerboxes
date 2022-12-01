package fuzs.easyshulkerboxes.core;

import fuzs.easyshulkerboxes.world.item.storage.FabricItemContainerProviders;
import fuzs.easyshulkerboxes.world.item.storage.ItemContainerProviders;

public class FabricAbstractions implements CommonAbstractions {

    @Override
    public ItemContainerProviders getItemContainerProviders() {
        return new FabricItemContainerProviders();
    }
}
