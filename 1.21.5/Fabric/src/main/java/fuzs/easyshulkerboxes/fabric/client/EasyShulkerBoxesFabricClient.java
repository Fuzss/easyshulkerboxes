package fuzs.easyshulkerboxes.fabric.client;

import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.client.EasyShulkerBoxesClient;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.fabricmc.api.ClientModInitializer;

public class EasyShulkerBoxesFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(EasyShulkerBoxes.MOD_ID, EasyShulkerBoxesClient::new);
    }
}
