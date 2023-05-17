package fuzs.easyshulkerboxes.mixin;

import fuzs.easyshulkerboxes.api.container.v1.provider.ItemContainerProvider;
import fuzs.easyshulkerboxes.impl.world.item.container.ItemInteractionHelper;
import fuzs.easyshulkerboxes.impl.world.item.container.ItemContainerProviders;
import fuzs.puzzleslib.proxy.Proxy;
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

@Mixin(ItemStack.class)
abstract class ItemStackMixin {

    @Inject(method = "overrideStackedOnOther", at = @At("HEAD"), cancellable = true)
    public void overrideStackedOnOther(Slot slot, ClickAction clickAction, Player player, CallbackInfoReturnable<Boolean> callback) {
        ItemStack containerStack = ItemStack.class.cast(this);
        ItemContainerProvider provider = ItemContainerProviders.INSTANCE.get(containerStack);
        if (provider != null && provider.allowsPlayerInteractions(containerStack, player)) {
            boolean result = ItemInteractionHelper.overrideStackedOnOther(() -> provider.getItemContainer(containerStack, player, true), slot, clickAction, player, stack -> provider.getAcceptableItemCount(containerStack, stack, player));
            if (result) provider.broadcastContainerChanges(player);
            callback.setReturnValue(result);
        }
    }

    @Inject(method = "overrideOtherStackedOnMe", at = @At("HEAD"), cancellable = true)
    public void overrideOtherStackedOnMe(ItemStack stackOnMe, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess, CallbackInfoReturnable<Boolean> callback) {
        ItemStack containerStack = ItemStack.class.cast(this);
        ItemContainerProvider provider = ItemContainerProviders.INSTANCE.get(containerStack);
        if (provider != null && provider.allowsPlayerInteractions(containerStack, player)) {
            boolean result = ItemInteractionHelper.overrideOtherStackedOnMe(() -> provider.getItemContainer(containerStack, player, true), stackOnMe, slot, clickAction, player, slotAccess, stack -> provider.getAcceptableItemCount(containerStack, stack, player));
            if (result) provider.broadcastContainerChanges(player);
            callback.setReturnValue(result);
        }
    }

    @Inject(method = "getTooltipImage", at = @At("HEAD"), cancellable = true)
    public void getTooltipImage(CallbackInfoReturnable<Optional<TooltipComponent>> callback) {
        ItemStack containerStack = ItemStack.class.cast(this);
        ItemContainerProvider provider = ItemContainerProviders.INSTANCE.get(containerStack);
        if (provider != null && provider.canProvideTooltipImage(containerStack, Proxy.INSTANCE.getClientPlayer())) {
            callback.setReturnValue(provider.getTooltipImage(containerStack, Proxy.INSTANCE.getClientPlayer()));
        }
    }
}
