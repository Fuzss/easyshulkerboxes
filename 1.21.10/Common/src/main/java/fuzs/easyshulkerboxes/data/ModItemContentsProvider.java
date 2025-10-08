package fuzs.easyshulkerboxes.data;

import fuzs.easyshulkerboxes.world.item.container.MapProvider;
import fuzs.iteminteractions.api.v1.DyeBackedColor;
import fuzs.iteminteractions.api.v1.data.AbstractItemContentsProvider;
import fuzs.iteminteractions.api.v1.provider.ItemContentsProvider;
import fuzs.iteminteractions.api.v1.provider.impl.BundleProvider;
import fuzs.iteminteractions.api.v1.provider.impl.ContainerProvider;
import fuzs.iteminteractions.api.v1.provider.impl.EnderChestProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.ShulkerBoxBlock;

public class ModItemContentsProvider extends AbstractItemContentsProvider {

    public ModItemContentsProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addItemProviders(HolderLookup.Provider registries) {
        HolderLookup.RegistryLookup<Item> items = registries.lookupOrThrow(Registries.ITEM);
        this.registerShulkerBoxProviders(items);
        this.registerBundleProviders(items);
        this.registerVanillaProviders(items);
    }

    private void registerVanillaProviders(HolderLookup.RegistryLookup<Item> items) {
        this.add(items, new EnderChestProvider(), Items.ENDER_CHEST);
        this.add(items, new MapProvider(), Items.FILLED_MAP);
        this.add(items,
                "dispenser",
                new ContainerProvider(3, 3).interactionPermissions(ContainerProvider.InteractionPermissions.NEVER),
                Items.DISPENSER,
                Items.DROPPER);
        this.add(items,
                "chest",
                new ContainerProvider(9, 3).interactionPermissions(ContainerProvider.InteractionPermissions.NEVER),
                Items.CHEST,
                Items.TRAPPED_CHEST,
                Items.BARREL);
        this.add(items,
                new ContainerProvider(5, 1).interactionPermissions(ContainerProvider.InteractionPermissions.NEVER),
                Items.HOPPER);
        this.add(items,
                "furnace",
                new ContainerProvider(3, 1).interactionPermissions(ContainerProvider.InteractionPermissions.NEVER),
                Items.FURNACE,
                Items.BLAST_FURNACE,
                Items.SMOKER);
        this.add(items,
                new ContainerProvider(5, 1).interactionPermissions(ContainerProvider.InteractionPermissions.NEVER),
                Items.BREWING_STAND);
        this.add(items,
                "campfire",
                new ContainerProvider(4, 1).interactionPermissions(ContainerProvider.InteractionPermissions.NEVER),
                Items.CAMPFIRE,
                Items.SOUL_CAMPFIRE);
    }

    private void registerShulkerBoxProviders(HolderLookup.RegistryLookup<Item> items) {
        this.add(items, new ContainerProvider(9, 3).filterContainerItems(true), Items.SHULKER_BOX);
        for (DyeColor dyeColor : DyeColor.values()) {
            ItemContentsProvider provider = new ContainerProvider(9,
                    3,
                    DyeBackedColor.fromDyeColor(dyeColor)).filterContainerItems(true);
            this.add(items, provider, ShulkerBoxBlock.getBlockByColor(dyeColor).asItem());
        }
    }

    private void registerBundleProviders(HolderLookup.RegistryLookup<Item> items) {
        this.add(items,
                new BundleProvider(DyeBackedColor.fromDyeColor(DyeColor.BROWN)).filterContainerItems(true),
                Items.BUNDLE);
        for (DyeColor dyeColor : DyeColor.values()) {
            ItemContentsProvider provider = new BundleProvider(DyeBackedColor.fromDyeColor(dyeColor)).filterContainerItems(
                    true);
            this.add(items, provider, BundleItem.getByColor(dyeColor));
        }
    }
}
