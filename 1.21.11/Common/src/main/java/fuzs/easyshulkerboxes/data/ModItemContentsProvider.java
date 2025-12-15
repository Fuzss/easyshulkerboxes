package fuzs.easyshulkerboxes.data;

import fuzs.easyshulkerboxes.integration.ReinforcedShulkerBoxesIntegration;
import fuzs.easyshulkerboxes.world.item.container.MapProvider;
import fuzs.iteminteractions.api.v1.DyeBackedColor;
import fuzs.iteminteractions.api.v1.data.AbstractItemContentsProvider;
import fuzs.iteminteractions.api.v1.provider.ItemContentsProvider;
import fuzs.iteminteractions.api.v1.provider.impl.BundleProvider;
import fuzs.iteminteractions.api.v1.provider.impl.ContainerProvider;
import fuzs.iteminteractions.api.v1.provider.impl.EnderChestProvider;
import net.minecraft.resources.Identifier;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.ShulkerBoxBlock;

import java.util.function.BiConsumer;

public abstract class ModItemContentsProvider extends AbstractItemContentsProvider {

    ModItemContentsProvider(DataProviderContext context) {
        super(context);
    }

    public static DataProvider of(DataProviderContext context, BiConsumer<ModItemContentsProvider, HolderLookup.RegistryLookup<Item>> providerRegistrar) {
        return new ModItemContentsProvider(context) {
            @Override
            public void addItemProviders(HolderLookup.Provider registries) {
                providerRegistrar.accept(this, registries.lookupOrThrow(Registries.ITEM));
            }
        };
    }

    public final void registerShulkerBoxProviders(HolderLookup.RegistryLookup<Item> itemLookup) {
        this.add(new ContainerProvider(9, 3).filterContainerItems(true), Items.SHULKER_BOX);
        for (DyeColor dyeColor : DyeColor.values()) {
            ItemContentsProvider provider = new ContainerProvider(9,
                    3,
                    DyeBackedColor.fromDyeColor(dyeColor)).filterContainerItems(true);
            this.add(provider, ShulkerBoxBlock.getBlockByColor(dyeColor).asItem());
        }
    }

    public final void registerBundleProviders(HolderLookup.RegistryLookup<Item> itemLookup) {
        this.add(new BundleProvider(DyeBackedColor.fromDyeColor(DyeColor.BROWN)).filterContainerItems(true),
                Items.BUNDLE);
        for (DyeColor dyeColor : DyeColor.values()) {
            ItemContentsProvider provider = new BundleProvider(DyeBackedColor.fromDyeColor(dyeColor)).filterContainerItems(
                    true);
            this.add(provider, BundleItem.getByColor(dyeColor));
        }
    }

    public final void registerEnderChestProvider(HolderLookup.RegistryLookup<Item> itemLookup) {
        this.add(new EnderChestProvider(), Items.ENDER_CHEST);
    }

    public final void registerContainerProviders(HolderLookup.RegistryLookup<Item> itemLookup) {
        this.add(Identifier.withDefaultNamespace("dispenser"),
                new ContainerProvider(3,
                        3).interactionPermissions(ContainerProvider.InteractionPermissions.CREATIVE_ONLY),
                Items.DISPENSER,
                Items.DROPPER);
        this.add(Identifier.withDefaultNamespace("chest"),
                new ContainerProvider(9,
                        3).interactionPermissions(ContainerProvider.InteractionPermissions.CREATIVE_ONLY),
                Items.CHEST,
                Items.TRAPPED_CHEST,
                Items.BARREL);
        this.add(itemLookup,
                new ContainerProvider(9,
                        3).interactionPermissions(ContainerProvider.InteractionPermissions.CREATIVE_ONLY),
                ItemTags.COPPER_CHESTS);
        this.add(new ContainerProvider(5,
                1).interactionPermissions(ContainerProvider.InteractionPermissions.CREATIVE_ONLY), Items.HOPPER);
        this.add(Identifier.withDefaultNamespace("furnace"),
                new ContainerProvider(3,
                        1).interactionPermissions(ContainerProvider.InteractionPermissions.CREATIVE_ONLY),
                Items.FURNACE,
                Items.BLAST_FURNACE,
                Items.SMOKER);
        this.add(new ContainerProvider(5,
                1).interactionPermissions(ContainerProvider.InteractionPermissions.CREATIVE_ONLY), Items.BREWING_STAND);
        this.add(Identifier.withDefaultNamespace("campfire"),
                new ContainerProvider(4,
                        1).interactionPermissions(ContainerProvider.InteractionPermissions.CREATIVE_ONLY),
                Items.CAMPFIRE,
                Items.SOUL_CAMPFIRE);
    }

    public final void registerMapProvider(HolderLookup.RegistryLookup<Item> itemLookup) {
        this.add(new MapProvider(), Items.FILLED_MAP);
    }

    public final void registerModProviders(HolderLookup.RegistryLookup<Item> itemLookup) {
        ReinforcedShulkerBoxesIntegration.registerModProviders((Identifier identifier, ItemContentsProvider provider) -> {
            Holder<Item> holder = Holder.Reference.createStandAlone(itemLookup,
                    ResourceKey.create(Registries.ITEM, identifier));
            this.add(identifier, provider, holder);
        });
    }
}
