package fuzs.easyshulkerboxes.core;

import fuzs.easyshulkerboxes.world.item.storage.ItemContainerProvidersListener;

public class ForgeAbstractions implements CommonAbstractions {

    @Override
    public ItemContainerProvidersListener getItemContainerProviders() {
        return new ItemContainerProvidersListener();
    }
}
