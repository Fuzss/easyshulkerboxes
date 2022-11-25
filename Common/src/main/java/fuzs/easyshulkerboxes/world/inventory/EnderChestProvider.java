package fuzs.easyshulkerboxes.world.inventory;

import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.api.world.inventory.ContainerItemProvider;
import fuzs.easyshulkerboxes.api.world.item.container.ContainerItemHelper;
import fuzs.easyshulkerboxes.capability.EnderChestMenuCapability;
import fuzs.easyshulkerboxes.config.ServerConfig;
import fuzs.easyshulkerboxes.init.ModRegistry;
import fuzs.puzzleslib.proxy.Proxy;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class EnderChestProvider extends ContainerItemProvider {
    public static final ContainerItemProvider INSTANCE = new EnderChestProvider();

    private EnderChestProvider() {

    }

    @Override
    public SimpleContainer getItemContainer(Player player, ItemStack stack, boolean allowSaving) {
        return player.getEnderChestInventory();
    }

    @Override
    protected boolean canItemFitInside(ItemStack containerStack, ItemStack stack) {
        return true;
    }

    @Override
    protected boolean _canAcceptItem(ItemStack containerStack, ItemStack stack) {
        return this.getItemContainer(Proxy.INSTANCE.getClientPlayer(), containerStack, false).canAddItem(stack);
    }

    @Override
    protected int _getAcceptableItemCount(ItemStack containerStack, ItemStack stack) {
        return stack.getCount();
    }

    @Override
    public boolean isAllowed() {
        return EasyShulkerBoxes.CONFIG.get(ServerConfig.class).allowEnderChest;
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        // pretty ender color from tinted mod
        return ContainerItemHelper.getTooltipImageWithColor(Optional.of(this.getItemContainer(Proxy.INSTANCE.getClientPlayer(), stack, false)), 3, new float[]{0.16470589F, 0.38431373F, 0.33333334F});
    }

    @Override
    public void broadcastContainerChanges(Player player) {
        // will only actually broadcast when in creative menu as that menu needs manual syncing
        if (player.level.isClientSide) {
            ModRegistry.ENDER_CHEST_MENU_CAPABILITY.maybeGet(player).map(EnderChestMenuCapability::getEnderChestMenu).ifPresent(AbstractContainerMenu::broadcastChanges);
        }
    }
}
