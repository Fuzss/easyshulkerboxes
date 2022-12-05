package fuzs.easyshulkerboxes.api.world.item.container;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * an interface that when implemented represents a provider for any item to enable bundle-like inventory item interactions (extracting and adding items via right-clicking on the item) and bundle-like tooltips
 * <p>a container does not necessarily need to provide both item interactions and tooltips, what is provided is defined
 * by implementing {@link ItemContainerProvider#canProvideContainer} and {@link ItemContainerProvider#canProvideTooltipImage}
 * <p>this overrides any already implemented behavior (the default providers in Easy Shulker Boxes actually do this for vanilla bundles)
 */
public interface ItemContainerProvider {

    /**
     * does this provider support item inventory interactions (extracting and adding items)
     *
     * @param containerStack the container stack
     * @param player         the player performing the interaction
     * @return are inventory interactions allowed (is a container present on this item)
     */
    boolean canProvideContainer(ItemStack containerStack, Player player);

    /**
     * does the item stack have data for stored items
     * <p>an easy check if the corresponding container is empty without having to create a container instance
     * <p>mainly used by tooltip image and client-side mouse scroll handler
     *
     * @param containerStack the container stack
     * @return is the item stack tag with stored item data present
     */
    boolean hasItemContainerTag(ItemStack containerStack);

    /**
     * get the container provided by <code>stack</code> as a {@link SimpleContainer}
     *
     * @param containerStack item stack providing the container
     * @param player         player involved in the interaction
     * @param allowSaving    attach a saving listener to the container (this is set to <code>false</code> when creating a container e.g. for rendering a tooltip)
     * @return the container
     */
    SimpleContainer getItemContainer(ItemStack containerStack, Player player, boolean allowSaving);

    /**
     * read the item tag containing the stored inventory (usually as an <code>Items</code> sub-tag)
     * <p>this method is mainly required for items with block entity data, as the inventory is stored as part of the block entity data,
     * not directly in the item tag, so we need to override this method then
     *
     * @param containerStack stack to read item tag from
     * @return the tag
     */
    @Nullable
    default CompoundTag getItemData(ItemStack containerStack) {
        return containerStack.getTag();
    }

    /**
     * sets items stored in a {@link ListTag} to an item stack's tag, if the list is empty the tag key (<code>nbtKey</code>)
     * is properly removed (possibly removing the whole stack tag if it is empty afterwards)
     *
     * @param containerStack stack to set item tag data for
     * @param itemsTag       items to save as {@link ListTag}
     * @param nbtKey         nbt key to store <code>itemsTag</code> as
     */
    default void setItemData(ItemStack containerStack, ListTag itemsTag, String nbtKey) {
        if (itemsTag.isEmpty()) {
            containerStack.removeTagKey(nbtKey);
        } else {
            containerStack.addTagElement(nbtKey, itemsTag);
        }
    }

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
