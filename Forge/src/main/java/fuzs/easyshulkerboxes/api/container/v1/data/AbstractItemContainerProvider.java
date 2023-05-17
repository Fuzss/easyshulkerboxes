package fuzs.easyshulkerboxes.api.container.v1.data;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import fuzs.easyshulkerboxes.api.container.v1.ItemContainerProvider;
import fuzs.easyshulkerboxes.api.container.v1.ItemContainerProviderSerializers;
import fuzs.easyshulkerboxes.impl.world.item.container.ItemContainerProviders;
import net.minecraft.core.Registry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.ItemLike;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public abstract class AbstractItemContainerProvider implements DataProvider {
    private final Map<ResourceLocation, ItemContainerProvider> providers = Maps.newHashMap();
    private final DataGenerator dataGenerator;

    public AbstractItemContainerProvider(DataGenerator dataGenerator) {
        this.dataGenerator = dataGenerator;
    }

    @Override
    public final void run(CachedOutput output) throws IOException {
        this.registerBuiltInProviders();
        Path outputFolder = this.dataGenerator.getOutputFolder();
        for (Map.Entry<ResourceLocation, ItemContainerProvider> entry : this.providers.entrySet()) {
            JsonElement jsonElement = ItemContainerProviderSerializers.serialize(entry.getValue());
            ResourceLocation item = entry.getKey();
            Path path = outputFolder.resolve(PackType.SERVER_DATA.getDirectory()).resolve(item.getNamespace()).resolve(ItemContainerProviders.ITEM_CONTAINER_PROVIDERS_KEY).resolve(item.getPath() + ".json");
            DataProvider.saveStable(output, jsonElement, path);
        }
    }

    protected abstract void registerBuiltInProviders();

    public void add(ItemLike item, ItemContainerProvider provider) {
        this.add(Registry.ITEM.getKey(item.asItem()), provider);
    }

    public void add(ResourceLocation item, ItemContainerProvider provider) {
        this.providers.put(item, provider);
    }

    @Override
    public String getName() {
        return "Item Container Provider";
    }
}
