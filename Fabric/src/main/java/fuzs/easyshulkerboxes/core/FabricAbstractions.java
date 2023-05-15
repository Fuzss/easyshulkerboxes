package fuzs.easyshulkerboxes.core;

import fuzs.easyshulkerboxes.world.item.storage.FabricItemContainerProvidersListener;
import fuzs.easyshulkerboxes.world.item.storage.ItemContainerProvidersListener;

public class FabricAbstractions implements CommonAbstractions {

    @Override
    public ItemContainerProvidersListener getItemContainerProviders() {
        return new FabricItemContainerProvidersListener();
    }
}
