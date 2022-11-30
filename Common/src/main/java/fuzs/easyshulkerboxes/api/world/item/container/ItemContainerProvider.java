package fuzs.easyshulkerboxes.api.world.item.container;

import com.google.common.collect.Maps;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public interface ItemContainerProvider {
    @ApiStatus.Internal
    Map<Item, ItemContainerProvider> REGISTRY = Collections.synchronizedMap(Maps.newIdentityHashMap());

    static void register(ItemLike item, ItemContainerProvider provider) {
        register(item.asItem(), provider);
    }

    static void register(Item item, ItemContainerProvider provider) {
        REGISTRY.put(item, provider);
    }

    static boolean canSupplyProvider(ItemStack stack) {
        return REGISTRY.containsKey(stack.getItem());
    }

    @Nullable
    static ItemContainerProvider get(Item item) {
        return REGISTRY.get(item);
    }

    Optional<SimpleContainer> getItemContainer(Player player, ItemStack stack, boolean allowSaving);

    boolean isItemAllowedInContainer(ItemStack containerStack, ItemStack stack);

    /**
     * is there enough space in the container provided by <code>containerStack</code> to add <code>stack</code> (not necessarily the full stack)
     *
     * @param player            the player interacting with both items
     * @param containerStack    the item stack providing the container to add <code>stack</code> to
     * @param stack             the stack to be added to the container
     * @return                  is adding any portion of <code>stack</code> to the container possible
     */
    boolean canAddItem(Player player, ItemStack containerStack, ItemStack stack);

    int getAcceptableItemCount(Player player, ItemStack containerStack, ItemStack stack);

    Optional<TooltipComponent> getTooltipImage(Player player, ItemStack stack);

    void broadcastContainerChanges(Player player);
}
