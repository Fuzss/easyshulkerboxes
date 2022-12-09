package fuzs.easyshulkerboxes.world.item.container;

import com.google.gson.JsonObject;
import fuzs.easyshulkerboxes.capability.EnderChestMenuCapability;
import fuzs.easyshulkerboxes.init.ModRegistry;
import fuzs.easyshulkerboxes.world.inventory.tooltip.ContainerItemTooltip;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class EnderChestProvider extends AbstractItemContainerProvider {
    /**
     * pretty ender color from tinted mod
     */
    private static final float[] DEFAULT_ENDER_CHEST_COLOR = {0.16470589F, 0.38431373F, 0.33333334F};

    @Override
    public SimpleContainer getItemContainer(ItemStack containerStack, Player player, boolean allowSaving) {
        return player.getEnderChestInventory();
    }

    @Override
    public boolean hasItemContainerData(ItemStack containerStack) {
        return true;
    }

    @Override
    public @Nullable CompoundTag getItemContainerData(ItemStack containerStack) {
        return null;
    }

    @Override
    public void setItemContainerData(ItemStack containerStack, ListTag itemsTag, String nbtKey) {

    }

    @Override
    public boolean canProvideTooltipImage(ItemStack containerStack, Player player) {
        return true;
    }

    @Override
    protected TooltipComponent internal$getTooltipImage(ItemStack stack, NonNullList<ItemStack> items) {
        return new ContainerItemTooltip(items, 9, 3, DEFAULT_ENDER_CHEST_COLOR);
    }

    @Override
    public void broadcastContainerChanges(Player player) {
        // will only actually broadcast when in creative menu as that menu needs manual syncing
        if (player.level.isClientSide) {
            ModRegistry.ENDER_CHEST_MENU_CAPABILITY.maybeGet(player).map(EnderChestMenuCapability::getEnderChestMenu).ifPresent(AbstractContainerMenu::broadcastChanges);
        }
    }

    @Override
    public void toJson(JsonObject jsonObject) {

    }
}
