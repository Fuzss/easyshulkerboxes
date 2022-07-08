package fuzs.easyshulkerboxes;

import net.fabricmc.api.ModInitializer;

public class EasyShulkerBoxesFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        EasyShulkerBoxes.onConstructMod();
    }
}
