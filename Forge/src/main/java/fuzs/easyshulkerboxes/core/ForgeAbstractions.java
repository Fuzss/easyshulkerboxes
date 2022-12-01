package fuzs.easyshulkerboxes.core;

import fuzs.easyshulkerboxes.world.item.storage.ItemContainerProviders;

public class ForgeAbstractions implements CommonAbstractions {

    @Override
    public ItemContainerProviders getItemContainerProviders() {
        return new ItemContainerProviders();
    }
}
