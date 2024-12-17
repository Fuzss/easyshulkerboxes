package fuzs.easyshulkerboxes.data;

import fuzs.easyshulkerboxes.world.item.container.MapProvider;
import fuzs.iteminteractions.api.v1.DyeBackedColor;
import fuzs.iteminteractions.api.v1.data.AbstractItemContentsProvider;
import fuzs.iteminteractions.api.v1.provider.impl.BundleProvider;
import fuzs.iteminteractions.api.v1.provider.impl.ContainerProvider;
import fuzs.iteminteractions.api.v1.provider.impl.EnderChestProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.ShulkerBoxBlock;

public class ModItemContentsProvider extends AbstractItemContentsProvider {

    public ModItemContentsProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addItemProviders() {
        this.registerVanillaProviders();
    }

    private void registerVanillaProviders() {
        this.registerShulkerBoxProviders();
        this.add(new EnderChestProvider(), Items.ENDER_CHEST);
        this.add(new BundleProvider(1, DyeBackedColor.fromDyeColor(DyeColor.BROWN)), Items.BUNDLE);
        this.add(new MapProvider(), Items.FILLED_MAP);
        this.add("dispenser",
                new ContainerProvider(3, 3).interactionPermissions(ContainerProvider.InteractionPermissions.NEVER),
                Items.DISPENSER,
                Items.DROPPER
        );
        this.add("chest",
                new ContainerProvider(9, 3).interactionPermissions(ContainerProvider.InteractionPermissions.NEVER),
                Items.CHEST,
                Items.TRAPPED_CHEST,
                Items.BARREL
        );
        this.add(new ContainerProvider(5, 1).interactionPermissions(ContainerProvider.InteractionPermissions.NEVER),
                Items.HOPPER
        );
        this.add("furnace",
                new ContainerProvider(3, 1).interactionPermissions(ContainerProvider.InteractionPermissions.NEVER),
                Items.FURNACE,
                Items.BLAST_FURNACE,
                Items.SMOKER
        );
        this.add(new ContainerProvider(5, 1).interactionPermissions(ContainerProvider.InteractionPermissions.NEVER),
                Items.BREWING_STAND
        );
        this.add("campfire",
                new ContainerProvider(4, 1).interactionPermissions(ContainerProvider.InteractionPermissions.NEVER),
                Items.CAMPFIRE,
                Items.SOUL_CAMPFIRE
        );
    }

    private void registerShulkerBoxProviders() {
        this.add(new ContainerProvider(9, 3).filterContainerItems(true), Items.SHULKER_BOX);
        // only affects vanilla shulker boxes, other mods might add shulker boxes with a different inventory size
        for (DyeColor dyeColor : DyeColor.values()) {
            Item item = ShulkerBoxBlock.getBlockByColor(dyeColor).asItem();
            this.add(new ContainerProvider(9, 3, DyeBackedColor.fromDyeColor(dyeColor)).filterContainerItems(true),
                    item
            );
        }
    }
}
