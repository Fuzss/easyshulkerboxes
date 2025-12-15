package fuzs.easyshulkerboxes.neoforge;

import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.data.ModItemContentsProvider;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import fuzs.puzzleslib.neoforge.api.data.v2.core.NeoForgeDataProviderContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.Item;
import net.neoforged.fml.common.Mod;

import java.util.function.BiConsumer;

@Mod(EasyShulkerBoxes.MOD_ID)
public class EasyShulkerBoxesNeoForge {

    public EasyShulkerBoxesNeoForge() {
        ModConstructor.construct(EasyShulkerBoxes.MOD_ID, EasyShulkerBoxes::new);
        registerBuiltInDataProviders(EasyShulkerBoxes.SHULKER_BOXES_LOCATION,
                ModItemContentsProvider::registerShulkerBoxProviders);
        registerBuiltInDataProviders(EasyShulkerBoxes.BUNDLES_LOCATION,
                ModItemContentsProvider::registerBundleProviders);
        registerBuiltInDataProviders(EasyShulkerBoxes.ENDER_CHEST_LOCATION,
                ModItemContentsProvider::registerEnderChestProvider);
        registerBuiltInDataProviders(EasyShulkerBoxes.CONTAINERS_LOCATION,
                ModItemContentsProvider::registerContainerProviders);
        registerBuiltInDataProviders(EasyShulkerBoxes.MAP_LOCATION, ModItemContentsProvider::registerMapProvider);
        registerBuiltInDataProviders(EasyShulkerBoxes.MOD_SUPPORT_LOCATION,
                ModItemContentsProvider::registerModProviders);
    }

    private static void registerBuiltInDataProviders(Identifier identifier, BiConsumer<ModItemContentsProvider, HolderLookup.RegistryLookup<Item>> providerRegistrar) {
        DataProviderHelper.registerDataProviders(identifier,
                PackType.SERVER_DATA,
                (NeoForgeDataProviderContext context) -> {
                    return ModItemContentsProvider.of(context, providerRegistrar);
                });
    }
}
