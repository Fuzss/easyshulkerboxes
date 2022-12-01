package fuzs.easyshulkerboxes.core;

import fuzs.easyshulkerboxes.world.item.storage.ItemContainerProviders;
import fuzs.puzzleslib.util.PuzzlesUtil;

public interface CommonAbstractions {
    CommonAbstractions INSTANCE = PuzzlesUtil.loadServiceProvider(CommonAbstractions.class);

    ItemContainerProviders getItemContainerProviders();
}
