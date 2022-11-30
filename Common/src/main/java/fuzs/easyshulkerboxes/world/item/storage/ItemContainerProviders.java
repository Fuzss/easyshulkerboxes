package fuzs.easyshulkerboxes.world.item.storage;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.api.world.item.container.ItemContainerProvider;
import fuzs.easyshulkerboxes.api.world.item.container.SerializableItemContainerProvider;
import fuzs.easyshulkerboxes.world.item.container.*;
import fuzs.puzzleslib.core.ModLoaderEnvironment;
import fuzs.puzzleslib.json.JsonConfigFileUtil;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
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
    public static final ItemContainerProviders INSTANCE = new ItemContainerProviders();
    private static final String ITEM_CONTAINER_PROVIDERS_KEY = "item_container_providers";
    private static final Map<ResourceLocation, SerializableItemContainerProvider> BUILT_IN_PROVIDERS = Maps.newHashMap();

    private Map<Item, ItemContainerProvider> providers = ImmutableMap.of();

    private ItemContainerProviders() {
        super(JsonConfigFileUtil.GSON, ITEM_CONTAINER_PROVIDERS_KEY);
    }

    @Nullable
    public ItemContainerProvider get(ItemLike item) {
        return this.providers.get(item.asItem());
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        ImmutableMap.Builder<Item, ItemContainerProvider> builder = ImmutableMap.builder();
        for (Map.Entry<ResourceLocation, JsonElement> entry : object.entrySet()) {
            try {
                JsonObject jsonObject = entry.getValue().getAsJsonObject();
                ResourceLocation id = new ResourceLocation(GsonHelper.getAsString(jsonObject, "item"));
                // modded items may not be present, but we register default providers for some
                if (!Registry.ITEM.containsKey(id)) continue;
                Item item = Registry.ITEM.get(id);
                ItemContainerProvider provider = SerializableItemContainerProvider.deserialize(jsonObject);
                builder.put(item, provider);
            } catch (Exception e) {
                EasyShulkerBoxes.LOGGER.error("Couldn't parse item container provider {}", entry.getKey(), e);
            }
        }
        this.providers = builder.build();
    }

    public static void serializeBuiltInProviders() {
        for (Map.Entry<ResourceLocation, SerializableItemContainerProvider> entry : BUILT_IN_PROVIDERS.entrySet()) {
            ResourceLocation item = entry.getKey();
            JsonElement jsonElement = SerializableItemContainerProvider.serialize(item, entry.getValue());
            Path path = ModLoaderEnvironment.INSTANCE.getGameDir().resolve("data").resolve(item.getNamespace()).resolve(ITEM_CONTAINER_PROVIDERS_KEY).resolve(item.getPath() + ".json");
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
    }
}
