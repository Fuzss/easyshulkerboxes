package fuzs.easyshulkerboxes;

import fuzs.puzzleslib.core.CoreServices;
import net.fabricmc.api.ModInitializer;

public class EasyShulkerBoxesFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CoreServices.FACTORIES.modConstructor(EasyShulkerBoxes.MOD_ID).accept(new EasyShulkerBoxes());
    }
}
