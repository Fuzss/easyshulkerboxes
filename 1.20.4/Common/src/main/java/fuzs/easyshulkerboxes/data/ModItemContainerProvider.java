package fuzs.easyshulkerboxes.data;

import fuzs.easyshulkerboxes.integration.inmis.InmisIntegration;
import fuzs.easyshulkerboxes.integration.reinforcedshulkerboxes.ReinforcedShulkerBoxesIntegration;
import fuzs.easyshulkerboxes.integration.simplebackpack.SimpleBackpackIntegration;
import fuzs.easyshulkerboxes.world.item.container.MapProvider;
import fuzs.iteminteractions.api.v1.data.AbstractItemContainerProvider;
import fuzs.iteminteractions.api.v1.provider.BlockEntityProvider;
import fuzs.iteminteractions.api.v1.provider.BlockEntityViewProvider;
import fuzs.iteminteractions.api.v1.provider.BundleProvider;
import fuzs.iteminteractions.api.v1.provider.EnderChestProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModItemContainerProvider extends AbstractItemContainerProvider {

    public ModItemContainerProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addItemProviders() {
        this.registerVanillaProviders();
        SimpleBackpackIntegration.registerProviders(this::add);
        InmisIntegration.registerProviders(this::add);
        ReinforcedShulkerBoxesIntegration.registerProviders(this::add);
    }

    private void registerVanillaProviders() {
        this.registerShulkerBoxProviders();
        this.add(Items.ENDER_CHEST, new EnderChestProvider());
        this.add(Items.BUNDLE, new BundleProvider(64, DyeColor.BROWN));
        this.add(Items.FILLED_MAP, new MapProvider());
        this.add(Items.DROPPER, new BlockEntityViewProvider(BlockEntityType.DROPPER, 3, 3));
        this.add(Items.DISPENSER, new BlockEntityViewProvider(BlockEntityType.DISPENSER, 3, 3));
        this.add(Items.CHEST, new BlockEntityViewProvider(BlockEntityType.CHEST, 9, 3));
        this.add(Items.TRAPPED_CHEST, new BlockEntityViewProvider(BlockEntityType.TRAPPED_CHEST, 9, 3));
        this.add(Items.BARREL, new BlockEntityViewProvider(BlockEntityType.BARREL, 9, 3));
        this.add(Items.HOPPER, new BlockEntityViewProvider(BlockEntityType.HOPPER, 5, 1));
        this.add(Items.FURNACE, new BlockEntityViewProvider(BlockEntityType.FURNACE, 3, 1));
        this.add(Items.BLAST_FURNACE, new BlockEntityViewProvider(BlockEntityType.BLAST_FURNACE, 3, 1));
        this.add(Items.SMOKER, new BlockEntityViewProvider(BlockEntityType.SMOKER, 3, 1));
        this.add(Items.BREWING_STAND, new BlockEntityViewProvider(BlockEntityType.BREWING_STAND, 5, 1));
        this.add(Items.CAMPFIRE, new BlockEntityViewProvider(BlockEntityType.CAMPFIRE, 4, 1));
        this.add(Items.SOUL_CAMPFIRE, new BlockEntityViewProvider(BlockEntityType.CAMPFIRE, 4, 1));
    }

    private void registerShulkerBoxProviders() {
        this.add(Items.SHULKER_BOX, BlockEntityProvider.shulkerBoxProvider(BlockEntityType.SHULKER_BOX, 9, 3, null));
        // only affects vanilla shulker boxes, other mods might add shulker boxes with a different inventory size
        for (DyeColor dyeColor : DyeColor.values()) {
            Item item = ShulkerBoxBlock.getBlockByColor(dyeColor).asItem();
            this.add(item, BlockEntityProvider.shulkerBoxProvider(BlockEntityType.SHULKER_BOX, 9, 3, dyeColor));
        }
    }
}
