package fuzs.easyshulkerboxes.mixin;

import fuzs.easyshulkerboxes.api.world.item.container.ContainerItemHelper;
import fuzs.easyshulkerboxes.api.world.inventory.ContainerItemProvider;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

// run before Quark does as it has the same feature, but our implementation is more polished
// not sure if this helps very much, at least it does with Charm on Fabric
@Mixin(value = Item.class, priority = 900)
public abstract class ItemMixin {

    @Inject(method = "overrideStackedOnOther", at = @At("HEAD"), cancellable = true)
    public void easyshulkerboxes$overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction clickAction, Player player, CallbackInfoReturnable<Boolean> callback) {
        if (ContainerItemProvider.suppliesContainerProvider(stack)) {
            ContainerItemProvider itemProvider = ContainerItemProvider.get(stack.getItem());
            if (itemProvider.isAllowed()) {
                boolean success = ContainerItemHelper.overrideStackedOnOther(() -> itemProvider.getItemContainer(player, stack), slot, clickAction, player, itemProvider::canAcceptItem, SoundEvents.BUNDLE_INSERT, SoundEvents.BUNDLE_REMOVE_ONE);
                if (success) itemProvider.broadcastContainerChanges(player);
                callback.setReturnValue(success);
            }
        }
    }

    @Inject(method = "overrideOtherStackedOnMe", at = @At("HEAD"), cancellable = true)
    public void easyshulkerboxes$overrideOtherStackedOnMe(ItemStack stack, ItemStack stackOnMe, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess, CallbackInfoReturnable<Boolean> callback) {
        if (ContainerItemProvider.suppliesContainerProvider(stack)) {
            ContainerItemProvider itemProvider = ContainerItemProvider.get(stack.getItem());
            if (itemProvider.isAllowed()) {
                boolean success = ContainerItemHelper.overrideOtherStackedOnMe(() -> itemProvider.getItemContainer(player, stack), stackOnMe, slot, clickAction, player, slotAccess, itemProvider::canAcceptItem, SoundEvents.BUNDLE_INSERT, SoundEvents.BUNDLE_REMOVE_ONE);
                if (success) itemProvider.broadcastContainerChanges(player);
                callback.setReturnValue(success);
            }
        }
    }

    @Inject(method = "getTooltipImage", at = @At("HEAD"), cancellable = true)
    public void easyshulkerboxes$getTooltipImage(ItemStack stack, CallbackInfoReturnable<Optional<TooltipComponent>> callback) {
        if (ContainerItemProvider.suppliesContainerProvider(stack)) {
            ContainerItemProvider itemProvider = ContainerItemProvider.get(stack.getItem());
            if (itemProvider.isAllowed()) {
                callback.setReturnValue(itemProvider.getTooltipImage(stack));
            }
        }
    }
}
