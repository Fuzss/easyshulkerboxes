package fuzs.easyshulkerboxes.api.world.inventory;

import com.google.common.collect.Maps;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public interface ContainerItemProvider {
    Map<Item, ContainerItemProvider> REGISTRY = Maps.newIdentityHashMap();

    static void register(ItemLike item, ContainerItemProvider provider) {
        register(item.asItem(), provider);
    }

    static void register(Item item, ContainerItemProvider provider) {
        REGISTRY.put(item, provider);
    }

    static boolean suppliesContainerProvider(ItemStack stack) {
        // some mods make empty shulker boxes stackable, disable this mod then as it would allow for item duplication otherwise
        return stack.getCount() == 1 && REGISTRY.containsKey(stack.getItem());
    }

    static ContainerItemProvider get(Item item) {
        ContainerItemProvider provider = REGISTRY.get(item);
        Objects.requireNonNull(provider, "container item provider is null");
        return provider;
    }

    default SimpleContainer getItemContainer(Player player, ItemStack stack) {
        return this.getItemContainer(player, stack, true);
    }

    SimpleContainer getItemContainer(Player player, ItemStack stack, boolean allowSaving);

    int acceptableItemCount(ItemStack containerStack, ItemStack stack);

    boolean isAllowed();

    Optional<TooltipComponent> getTooltipImage(ItemStack stack);

    default void broadcastContainerChanges(Player player) {

    }
}
