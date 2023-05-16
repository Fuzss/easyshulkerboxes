package fuzs.easyshulkerboxes.world.item.storage;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.api.world.item.container.ItemContainerProvider;
import fuzs.easyshulkerboxes.api.world.item.container.ItemContainerProviderSerializers;
import fuzs.easyshulkerboxes.core.CommonAbstractions;
import fuzs.easyshulkerboxes.network.S2CSyncItemContainerProvider;
import fuzs.easyshulkerboxes.world.item.container.ForwardingItemContainerProvider;
import fuzs.puzzleslib.core.ModLoaderEnvironment;
import fuzs.puzzleslib.json.JsonConfigFileUtil;
import net.minecraft.core.Registry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public class ItemContainerProvidersListener extends SimpleJsonResourceReloadListener {
    public static final ItemContainerProvidersListener INSTANCE = CommonAbstractions.INSTANCE.getItemContainerProviders();
    public static final String ITEM_CONTAINER_PROVIDERS_KEY = "item_container_providers";
    private static final Map<ResourceLocation, ItemContainerProvider> BUILT_IN_PROVIDERS = Maps.newHashMap();

    private Map<ResourceLocation, JsonElement> rawProviders = ImmutableMap.of();
    private Map<Item, ItemContainerProvider> providers = ImmutableMap.of();

    public ItemContainerProvidersListener() {
        super(JsonConfigFileUtil.GSON, ITEM_CONTAINER_PROVIDERS_KEY);
    }

    @Nullable
    public ItemContainerProvider get(ItemStack stack) {
        return stack.isEmpty() ? null : this.providers.get(stack.getItem());
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
                ItemContainerProvider provider = ItemContainerProviderSerializers.deserialize(jsonObject);
                builder.put(Registry.ITEM.get(item), new ForwardingItemContainerProvider(provider));
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

    public static void serializeBuiltInProviders(CachedOutput output, Path outputFolder) throws IOException {
        for (Map.Entry<ResourceLocation, ItemContainerProvider> entry : BUILT_IN_PROVIDERS.entrySet()) {
            JsonElement jsonElement = ItemContainerProviderSerializers.serialize(entry.getValue());
            ResourceLocation item = entry.getKey();
            DataProvider.saveStable(output, jsonElement, outputFolder.resolve("data").resolve(item.getNamespace()).resolve(ITEM_CONTAINER_PROVIDERS_KEY).resolve(item.getPath() + ".json"));
        }
    }

    public static void registerBuiltInProvider(ItemLike item, ItemContainerProvider provider) {
        registerBuiltInProvider(Registry.ITEM.getKey(item.asItem()), provider);
    }

    public static void registerBuiltInProvider(ResourceLocation item, ItemContainerProvider provider) {
        BUILT_IN_PROVIDERS.put(item, provider);
    }
}
