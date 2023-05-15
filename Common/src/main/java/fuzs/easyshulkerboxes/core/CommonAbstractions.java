package fuzs.easyshulkerboxes.core;

import fuzs.easyshulkerboxes.world.item.storage.ItemContainerProvidersListener;
import fuzs.puzzleslib.util.PuzzlesUtil;

public interface CommonAbstractions {
    CommonAbstractions INSTANCE = PuzzlesUtil.loadServiceProvider(CommonAbstractions.class);

    ItemContainerProvidersListener getItemContainerProviders();
}
