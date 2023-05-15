package fuzs.easyshulkerboxes.data;

import fuzs.easyshulkerboxes.world.item.storage.ItemContainerProvidersListener;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;

import java.io.IOException;

public class ModItemContainerProvider implements DataProvider {
    private final DataGenerator dataGenerator;

    public ModItemContainerProvider(DataGenerator dataGenerator) {
        this.dataGenerator = dataGenerator;
    }

    @Override
    public void run(CachedOutput output) throws IOException {
        ItemContainerProvidersListener.serializeBuiltInProviders(output, this.dataGenerator.getOutputFolder());
    }

    @Override
    public String getName() {
        return "Item Container Provider";
    }
}
