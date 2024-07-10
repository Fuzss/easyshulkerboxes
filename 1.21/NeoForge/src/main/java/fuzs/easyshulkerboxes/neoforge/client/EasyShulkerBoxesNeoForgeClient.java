package fuzs.easyshulkerboxes.neoforge.client;

import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.client.EasyShulkerBoxesClient;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = EasyShulkerBoxes.MOD_ID, dist = Dist.CLIENT)
public class EasyShulkerBoxesNeoForgeClient {

    public EasyShulkerBoxesNeoForgeClient() {
        ClientModConstructor.construct(EasyShulkerBoxes.MOD_ID, EasyShulkerBoxesClient::new);
    }
}
