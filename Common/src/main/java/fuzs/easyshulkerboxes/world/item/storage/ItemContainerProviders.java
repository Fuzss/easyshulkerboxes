package fuzs.easyshulkerboxes.world.item.storage;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.api.world.item.container.ItemContainerProvider;
import fuzs.easyshulkerboxes.api.world.item.container.SerializableItemContainerProvider;
import fuzs.easyshulkerboxes.core.CommonAbstractions;
import fuzs.easyshulkerboxes.integration.InmisProvider;
import fuzs.easyshulkerboxes.network.S2CSyncItemContainerProvider;
import fuzs.easyshulkerboxes.world.item.container.*;
import fuzs.puzzleslib.core.ModLoaderEnvironment;
import fuzs.puzzleslib.json.JsonConfigFileUtil;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.Map;

public class ItemContainerProviders extends SimpleJsonResourceReloadListener {
    public static final ItemContainerProviders INSTANCE = CommonAbstractions.INSTANCE.getItemContainerProviders();
    protected static final String ITEM_CONTAINER_PROVIDERS_KEY = "item_container_providers";
    private static final Map<ResourceLocation, SerializableItemContainerProvider> BUILT_IN_PROVIDERS = Maps.newHashMap();

    private Map<ResourceLocation, JsonElement> rawProviders = ImmutableMap.of();
    private Map<Item, ItemContainerProvider> providers = ImmutableMap.of();

    public ItemContainerProviders() {
        super(JsonConfigFileUtil.GSON, ITEM_CONTAINER_PROVIDERS_KEY);
    }

    @Nullable
    public ItemContainerProvider get(ItemLike item) {
        return this.providers.get(item.asItem());
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        this.rawProviders = ImmutableMap.copyOf(object);
        this.buildProviders(object);
    }

    public void buildProviders(Map<ResourceLocation, JsonElement> object) {
        ImmutableMap.Builder<Item, ItemContainerProvider> builder = ImmutableMap.builder();
        for (Map.Entry<ResourceLocation, JsonElement> entry : object.entrySet()) {
            ResourceLocation item = entry.getKey();
            try {
                JsonObject jsonObject = entry.getValue().getAsJsonObject();
                // modded items may not be present, but we register default providers for some
                if (!Registry.ITEM.containsKey(item)) continue;
                ItemContainerProvider provider = SerializableItemContainerProvider.deserialize(jsonObject);
                builder.put(Registry.ITEM.get(item), provider);
            } catch (Exception e) {
                EasyShulkerBoxes.LOGGER.error("Couldn't parse item container provider {}", item, e);
            }
        }
        this.providers = builder.build();
    }

    public void sendProvidersToPlayer(ServerPlayer player) {
        if (ModLoaderEnvironment.INSTANCE.isServer()) {
            EasyShulkerBoxes.NETWORK.sendTo(new S2CSyncItemContainerProvider(this.rawProviders), player);
        }
    }

    public static void serializeBuiltInProviders() {
        for (Map.Entry<ResourceLocation, SerializableItemContainerProvider> entry : BUILT_IN_PROVIDERS.entrySet()) {
            JsonElement jsonElement = SerializableItemContainerProvider.serialize(entry.getValue());
            ResourceLocation item = entry.getKey();
            Path path = ModLoaderEnvironment.INSTANCE.getGameDir().resolve("generated").resolve("data").resolve(item.getNamespace()).resolve(ITEM_CONTAINER_PROVIDERS_KEY).resolve(item.getPath() + ".json");
            JsonConfigFileUtil.saveToFile(path.toFile(), jsonElement);
        }
    }

    public static void registerBuiltIn(ItemLike item, SerializableItemContainerProvider provider) {
        registerBuiltIn(Registry.ITEM.getKey(item.asItem()), provider);
    }

    public static void registerBuiltIn(ResourceLocation item, SerializableItemContainerProvider provider) {
        BUILT_IN_PROVIDERS.put(item, provider);
    }
    
    static {
        // Vanilla
        registerBuiltIn(Items.FILLED_MAP, new MapProvider());
        for (Map.Entry<ResourceKey<Block>, Block> entry : Registry.BLOCK.entrySet()) {
            // only affect vanilla shulker boxes, other mods might add shulker boxes with a different inventory size
            if (entry.getValue() instanceof ShulkerBoxBlock && entry.getKey().location().getNamespace().equals("minecraft")) {
                registerBuiltIn(entry.getValue(), ShulkerBoxProvider.create(entry.getValue()));
            }
        }
        registerBuiltIn(Items.ENDER_CHEST, new EnderChestProvider());
        registerBuiltIn(Items.BUNDLE, new BundleProvider());
        registerBuiltIn(Items.DROPPER, new BlockEntityProvider(BlockEntityType.DROPPER, 3, 3));
        registerBuiltIn(Items.DISPENSER, new BlockEntityProvider(BlockEntityType.DISPENSER, 3, 3));
        registerBuiltIn(Items.CHEST, new BlockEntityProvider(BlockEntityType.CHEST, 9, 3));
        registerBuiltIn(Items.TRAPPED_CHEST, new BlockEntityProvider(BlockEntityType.TRAPPED_CHEST, 9, 3));
        registerBuiltIn(Items.HOPPER, new BlockEntityProvider(BlockEntityType.HOPPER, 5, 1));
        registerBuiltIn(Items.FURNACE, new BlockEntityViewProvider(BlockEntityType.FURNACE, 3, 1));
        registerBuiltIn(Items.BLAST_FURNACE, new BlockEntityViewProvider(BlockEntityType.BLAST_FURNACE, 3, 1));
        registerBuiltIn(Items.SMOKER, new BlockEntityViewProvider(BlockEntityType.SMOKER, 3, 1));
        registerBuiltIn(Items.BREWING_STAND, new BlockEntityViewProvider(BlockEntityType.BREWING_STAND, 5, 1));
        registerBuiltIn(Items.CAMPFIRE, new BlockEntityViewProvider(BlockEntityType.CAMPFIRE, 4, 1));
        registerBuiltIn(Items.SOUL_CAMPFIRE, new BlockEntityViewProvider(BlockEntityType.CAMPFIRE, 4, 1));
        // Backpacked (Forge)
        registerBuiltIn(new ResourceLocation("backpacked:backpack"), new GenericItemContainerProvider(9, 1, DyeColor.BROWN));
        // Simple Backpack (Fabric)
        ItemContainerProviders.registerBuiltIn(new ResourceLocation("simple_backpack:ender_pack"), new EnderChestProvider());
        ItemContainerProviders.registerBuiltIn(new ResourceLocation("simple_backpack:backpack"), new GenericItemContainerProvider(9, 6, DyeColor.BROWN, "backpack/Items"));
        ItemContainerProviders.registerBuiltIn(new ResourceLocation("simple_backpack:simple_bundle"), new BundleProvider(3456));
        // Inmis (Fabric)
        ItemContainerProviders.registerBuiltIn(new ResourceLocation("inmis:ender_pouch"), new EnderChestProvider());
        ItemContainerProviders.registerBuiltIn(new ResourceLocation("inmis:baby_backpack"), new InmisProvider(3, 1, DyeColor.ORANGE, "Inventory"));
        ItemContainerProviders.registerBuiltIn(new ResourceLocation("inmis:frayed_backpack"), new InmisProvider(9, 1, DyeColor.BROWN, "Inventory"));
        ItemContainerProviders.registerBuiltIn(new ResourceLocation("inmis:plated_backpack"), new InmisProvider(9, 2, DyeColor.WHITE, "Inventory"));
        ItemContainerProviders.registerBuiltIn(new ResourceLocation("inmis:gilded_backpack"), new InmisProvider(9, 3, DyeColor.YELLOW, "Inventory"));
        ItemContainerProviders.registerBuiltIn(new ResourceLocation("inmis:bejeweled_backpack"), new InmisProvider(9, 5, DyeColor.LIGHT_BLUE, "Inventory"));
        ItemContainerProviders.registerBuiltIn(new ResourceLocation("inmis:blazing_backpack"), new InmisProvider(9, 6, DyeColor.YELLOW, "Inventory"));
        ItemContainerProviders.registerBuiltIn(new ResourceLocation("inmis:withered_backpack"), new InmisProvider(11, 6, DyeColor.BLACK, "Inventory"));
        ItemContainerProviders.registerBuiltIn(new ResourceLocation("inmis:endless_backpack"), new InmisProvider(15, 6, DyeColor.CYAN, "Inventory"));
    }
}
