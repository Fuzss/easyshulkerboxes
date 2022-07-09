package fuzs.easyshulkerboxes.client;

import net.fabricmc.api.ClientModInitializer;

public class EasyShulkerBoxesFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EasyShulkerBoxesClient.onConstructMod();
    }
}
