package fuzs.easyshulkerboxes.neoforge;

import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.data.ModItemContentsProvider;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.neoforged.fml.common.Mod;

@Mod(EasyShulkerBoxes.MOD_ID)
public class EasyShulkerBoxesNeoForge {

    public EasyShulkerBoxesNeoForge() {
        ModConstructor.construct(EasyShulkerBoxes.MOD_ID, EasyShulkerBoxes::new);
        DataProviderHelper.registerDataProviders(EasyShulkerBoxes.MOD_ID, ModItemContentsProvider::new);
    }
}
