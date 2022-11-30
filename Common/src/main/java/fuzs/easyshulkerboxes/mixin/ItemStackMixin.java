package fuzs.easyshulkerboxes.mixin;

import fuzs.easyshulkerboxes.api.world.item.container.ItemContainerProvider;
import fuzs.easyshulkerboxes.world.item.container.helper.ContainerItemHelper;
import fuzs.puzzleslib.proxy.Proxy;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

// run before Quark does as it has the same feature, but our implementation is more polished
// not sure if this helps very much, at least it does with Charm on Fabric
@Mixin(value = ItemStack.class, priority = 900)
abstract class ItemStackMixin {

    @Inject(method = "overrideStackedOnOther", at = @At("HEAD"), cancellable = true)
    public void simpleinventorycontainers$overrideStackedOnOther(Slot slot, ClickAction clickAction, Player player, CallbackInfoReturnable<Boolean> callback) {
        ItemStack containerStack = (ItemStack) (Object) this;
        ItemContainerProvider provider = ItemContainerProvider.get(containerStack.getItem());
        if (provider != null && provider.canProvideContainer(containerStack, player)) {
            boolean success = ContainerItemHelper.overrideStackedOnOther(() -> provider.getItemContainer(containerStack, player, true), slot, clickAction, player, stack -> provider.getAcceptableItemCount(containerStack, stack, player), SoundEvents.BUNDLE_INSERT, SoundEvents.BUNDLE_REMOVE_ONE);
            if (success) provider.broadcastContainerChanges(player);
            callback.setReturnValue(success);
        }
    }

    @Inject(method = "overrideOtherStackedOnMe", at = @At("HEAD"), cancellable = true)
    public void simpleinventorycontainers$overrideOtherStackedOnMe(ItemStack stackOnMe, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess, CallbackInfoReturnable<Boolean> callback) {
        ItemStack containerStack = (ItemStack) (Object) this;
        ItemContainerProvider provider = ItemContainerProvider.get(containerStack.getItem());
        if (provider != null && provider.canProvideContainer(containerStack, player)) {
            boolean success = ContainerItemHelper.overrideOtherStackedOnMe(() -> provider.getItemContainer(containerStack, player, true), stackOnMe, slot, clickAction, player, slotAccess, stack -> provider.getAcceptableItemCount(containerStack, stack, player), SoundEvents.BUNDLE_INSERT, SoundEvents.BUNDLE_REMOVE_ONE);
            if (success) provider.broadcastContainerChanges(player);
            callback.setReturnValue(success);
        }
    }

    @Inject(method = "getTooltipImage", at = @At("HEAD"), cancellable = true)
    public void simpleinventorycontainers$getTooltipImage(CallbackInfoReturnable<Optional<TooltipComponent>> callback) {
        ItemStack containerStack = (ItemStack) (Object) this;
        ItemContainerProvider provider = ItemContainerProvider.get(containerStack.getItem());
        if (provider != null && provider.canProvideTooltipImage(containerStack, Proxy.INSTANCE.getClientPlayer())) {
            callback.setReturnValue(provider.getTooltipImage(containerStack, Proxy.INSTANCE.getClientPlayer()));
        }
    }
}
