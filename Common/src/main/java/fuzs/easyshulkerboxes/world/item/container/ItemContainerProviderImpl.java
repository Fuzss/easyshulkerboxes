package fuzs.easyshulkerboxes.world.item.container;

import com.google.gson.JsonObject;
import fuzs.easyshulkerboxes.api.world.item.container.SerializableItemContainerProvider;
import fuzs.easyshulkerboxes.world.item.container.helper.ContainerItemHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class ItemContainerProviderImpl implements SerializableItemContainerProvider {

    @Override
    public boolean canProvideContainer(ItemStack containerStack, Player player) {
        return containerStack.getCount() == 1;
    }

    @Override
    public boolean isItemAllowedInContainer(ItemStack containerStack, ItemStack stackToAdd) {
        return true;
    }

    @Override
    public final boolean canAddItem(ItemStack containerStack, ItemStack stackToAdd, Player player) {
        if (this.canAcceptItem(player, containerStack, stackToAdd)) {
            return this.internal$canAddItem(this.getItemContainer(containerStack, player, false), containerStack, stackToAdd);
        }
        return false;
    }

    protected boolean internal$canAddItem(SimpleContainer container, ItemStack containerStack, ItemStack stack) {
        return container.canAddItem(stack);
    }

    @Override
    public final int getAcceptableItemCount(ItemStack containerStack, ItemStack stackToAdd, Player player) {
        if (this.canAcceptItem(player, containerStack, stackToAdd)) {
            return this.internal$getAcceptableItemCount(this.getItemContainer(containerStack, player, false), containerStack, stackToAdd);
        }
        return 0;
    }

    protected int internal$getAcceptableItemCount(SimpleContainer container, ItemStack containerStack, ItemStack stack) {
        return stack.getCount();
    }

    private boolean canAcceptItem(Player player, ItemStack containerStack, ItemStack stack) {
        return this.canProvideContainer(containerStack, player) && !stack.isEmpty() && this.isItemAllowedInContainer(containerStack, stack);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack containerStack, Player player) {
        SimpleContainer itemContainer = this.getItemContainer(containerStack, player, false);
        return Optional.of(this.internal$getTooltipImage(containerStack, ContainerItemHelper.containerToList(itemContainer)));
    }

    protected abstract TooltipComponent internal$getTooltipImage(ItemStack stack, NonNullList<ItemStack> items);

    @Override
    public void broadcastContainerChanges(Player player) {

    }

    @Override
    public void toJson(JsonObject jsonObject) {

    }
}
