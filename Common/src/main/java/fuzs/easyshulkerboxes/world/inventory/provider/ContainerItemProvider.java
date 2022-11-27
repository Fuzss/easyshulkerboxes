package fuzs.easyshulkerboxes.world.inventory.provider;

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

public abstract class ContainerItemProvider {
    public static final Map<Item, ContainerItemProvider> REGISTRY = Maps.newIdentityHashMap();

    public static void register(ItemLike item, ContainerItemProvider provider) {
        register(item.asItem(), provider);
    }

    public static void register(Item item, ContainerItemProvider provider) {
        REGISTRY.put(item, provider);
    }

    public static boolean canSupplyProvider(ItemStack stack) {
        // some mods make empty shulker boxes stackable, disable this mod then as it would allow for item duplication otherwise
        return stack.getCount() == 1 && REGISTRY.containsKey(stack.getItem());
    }

    public static ContainerItemProvider get(Item item) {
        ContainerItemProvider provider = REGISTRY.get(item);
        Objects.requireNonNull(provider, "container item provider is null");
        return provider;
    }

    public abstract SimpleContainer getItemContainer(Player player, ItemStack stack, boolean allowSaving);

    public boolean isItemContainerEmpty(Player player, ItemStack stack) {
        return this.getItemContainer(player, stack, false).isEmpty();
    }

    protected boolean canItemFitInside(ItemStack containerStack, ItemStack stack) {
        return true;
    }

    public final boolean canAcceptItem(ItemStack containerStack, ItemStack stack) {
        return !stack.isEmpty() && this.canItemFitInside(containerStack, stack) && this.internal$canAcceptItem(containerStack, stack);
    }

    protected abstract boolean internal$canAcceptItem(ItemStack containerStack, ItemStack stack);

    public final int getAcceptableItemCount(ItemStack containerStack, ItemStack stack) {
        return !stack.isEmpty() && this.canItemFitInside(containerStack, stack) ? this.internal$getAcceptableItemCount(containerStack, stack) : 0;
    }

    protected int internal$getAcceptableItemCount(ItemStack containerStack, ItemStack stack) {
        return stack.getCount();
    }

    public abstract boolean isAllowed();

    public abstract Optional<TooltipComponent> getTooltipImage(ItemStack stack);

    public void broadcastContainerChanges(Player player) {

    }
}
