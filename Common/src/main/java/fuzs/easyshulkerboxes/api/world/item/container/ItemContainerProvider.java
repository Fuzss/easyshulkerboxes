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

/**
 * an interface that when implemented represents a provider for any item to enable bundle-like inventory item interactions (extracting and adding items via right-clicking on the item) and bundle-like tooltips
 * <p>a container does not necessarily need to provide both item interactions and tooltips, what is provided is defined
 * by implementing {@link ItemContainerProvider#canProvideContainer} and {@link ItemContainerProvider#canProvideTooltipImage}
 * <p>this overrides any already implemented behavior (the default providers in Easy Shulker Boxes actually do this for vanilla bundles)
 * <p>new providers are registered in {@link ItemContainerProvider#REGISTRY}
 */
public interface ItemContainerProvider {
    /**
     * the internal registry for providers
     */
    @ApiStatus.Internal
    Map<Item, ItemContainerProvider> REGISTRY = Collections.synchronizedMap(Maps.newIdentityHashMap());

    /**
     * registers a provider for a block
     *
     * @param item     the block
     * @param provider the provider
     */
    static void register(ItemLike item, ItemContainerProvider provider) {
        register(item.asItem(), provider);
    }

    /**
     * registers a provider for an item
     *
     * @param item     the item
     * @param provider the provider
     */
    static void register(Item item, ItemContainerProvider provider) {
        REGISTRY.put(item, provider);
    }

    /**
     * get a provider for a block
     *
     * @param item block to get provider for
     * @return provider if present or null
     */
    @Nullable
    static ItemContainerProvider get(ItemLike item) {
        return get(item.asItem());
    }

    /**
     * get a provider for an item
     *
     * @param item item to get provider for
     * @return provider if present or null
     */
    @Nullable
    static ItemContainerProvider get(Item item) {
        return REGISTRY.get(item);
    }

    /**
     * does this provider support item inventory interactions (extracting and adding items)
     *
     * @param stack  the container stack
     * @param player the player performing the interaction
     * @return are inventory interactions allowed (is a container present on this item)
     */
    boolean canProvideContainer(ItemStack stack, Player player);

    /**
     * get the container provided by <code>stack</code> as a {@link SimpleContainer}
     *
     * @param stack       item stack providing the container
     * @param player      player involved in the interaction
     * @param allowSaving attach a saving listener to the container (this is set to <code>false</code> when creating a container e.g. for rendering a tooltip)
     * @return the container
     */
    SimpleContainer getItemContainer(ItemStack stack, Player player, boolean allowSaving);

    /**
     * called on the client-side to sync changes made during inventory item interactions back to the server
     * <p>this only works in the creative inventory, as any other menu does not allow the client to sync changes
     * <p>only really need for ender chests right now
     *
     * @param player the player performing the item interaction
     */
    void broadcastContainerChanges(Player player);

    /**
     * is <code>stack</code> allowed to be added to the container supplied by <code>containerStack</code>
     * <p>this should be the same behavior as vanilla's {@link Item#canFitInsideContainerItems()}
     *
     * @param containerStack the item stack providing the container to add <code>stack</code> to
     * @param stackToAdd     the stack to be added to the container
     * @return is <code>stack</code> allowed to be added to the container
     */
    boolean isItemAllowedInContainer(ItemStack containerStack, ItemStack stackToAdd);

    /**
     * is there enough space in the container provided by <code>containerStack</code> to add <code>stack</code> (not necessarily the full stack)
     *
     * @param containerStack the item stack providing the container to add <code>stack</code> to
     * @param stackToAdd     the stack to be added to the container
     * @param player         the player interacting with both items
     * @return is adding any portion of <code>stack</code> to the container possible
     */
    boolean canAddItem(ItemStack containerStack, ItemStack stackToAdd, Player player);

    /**
     * how much space is available in the container provided by <code>containerStack</code> to add <code>stack</code>
     * <p>mainly used by bundles, otherwise {@link ItemContainerProvider#canAddItem} should be enough
     *
     * @param containerStack the item stack providing the container to add <code>stack</code> to
     * @param stackToAdd     the stack to be added to the container
     * @param player         the player interacting with both items
     * @return is adding any portion of <code>stack</code> to the container possible
     */
    int getAcceptableItemCount(ItemStack containerStack, ItemStack stackToAdd, Player player);

    /**
     * does this provider support an image tooltip
     *
     * @param containerStack the item stack providing the container to show a tooltip for
     * @param player         player involved in the interaction
     * @return does <code>containerStack</code> provide a tooltip image
     */
    boolean canProvideTooltipImage(ItemStack containerStack, Player player);

    /**
     * the image tooltip provided by <code>containerStack</code>
     *
     * @param containerStack the item stack providing the container to show a tooltip for
     * @param player         player involved in the interaction
     * @return the image tooltip provided by <code>containerStack</code>
     */
    Optional<TooltipComponent> getTooltipImage(ItemStack containerStack, Player player);
}
