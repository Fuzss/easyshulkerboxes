package fuzs.easyshulkerboxes.api.world.item.container;

import com.google.common.collect.Maps;
import net.minecraft.core.Registry;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public interface ContainerItemProvider {
    Map<Item, ContainerItemProvider> REGISTRY = Maps.newIdentityHashMap();

    static <T extends Item> void registerItemLike(Class<? extends ItemLike> clazz, ContainerItemProvider provider) {
        for (Block block : Registry.BLOCK) {
            if (clazz.isInstance(block)) {
                REGISTRY.put(block.asItem(), provider);
            }
        }
    }

    static <T extends Item> void register(Class<? super T> clazz, ContainerItemProvider provider) {
        for (Item item : Registry.ITEM) {
            if (clazz.isInstance(item)) {
                REGISTRY.put(item, provider);
            }
        }
    }

    static boolean suppliesContainerProvider(ItemStack stack) {
        return stack.getCount() == 1 && REGISTRY.containsKey(stack.getItem());
    }

    static ContainerItemProvider get(Item item) {
        ContainerItemProvider provider = REGISTRY.get(item);
        Objects.requireNonNull(provider, "container item provider is null");
        return provider;
    }

    Supplier<SimpleContainer> getItemContainer(Player player, ItemStack stack);
}
