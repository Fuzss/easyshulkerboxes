package fuzs.easyshulkerboxes.mixin;

import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.capability.EnderChestMenuCapability;
import fuzs.easyshulkerboxes.config.ServerConfig;
import fuzs.easyshulkerboxes.init.ModRegistry;
import fuzs.easyshulkerboxes.world.item.ContainerItemHelper;
import fuzs.puzzleslib.proxy.Proxy;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EnderChestBlock;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
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
    public void overrideStackedOnOther$inject$head(ItemStack stack, Slot slot, ClickAction clickAction, Player player, CallbackInfoReturnable<Boolean> callbackInfo) {
        // some mods make empty shulker boxes stackable, disable this mod then as it would allow for item duplication otherwise
        if (stack.getCount() != 1) return;
        if (EasyShulkerBoxes.CONFIG.get(ServerConfig.class).allowShulkerBox && Block.byItem(stack.getItem()) instanceof ShulkerBoxBlock) {
            boolean success = ContainerItemHelper.overrideStackedOnOther(stack, BlockEntityType.SHULKER_BOX, 3, slot, clickAction, player, s -> s.getItem().canFitInsideContainerItems(), SoundEvents.BUNDLE_INSERT, SoundEvents.BUNDLE_REMOVE_ONE);
            callbackInfo.setReturnValue(success);
        } else if (EasyShulkerBoxes.CONFIG.get(ServerConfig.class).allowEnderChest && Block.byItem(stack.getItem()) instanceof EnderChestBlock) {
            boolean success = ContainerItemHelper.overrideStackedOnOther(player::getEnderChestInventory, slot, clickAction, player, s -> true, SoundEvents.BUNDLE_INSERT, SoundEvents.BUNDLE_REMOVE_ONE);
            // will only actually broadcast when in creative menu as that menu needs manual syncing
            if (success && player.level.isClientSide) ModRegistry.ENDER_CHEST_MENU_CAPABILITY.maybeGet(player).map(EnderChestMenuCapability::getEnderChestMenu).ifPresent(AbstractContainerMenu::broadcastChanges);
            callbackInfo.setReturnValue(success);
        }
    }

    @Inject(method = "overrideOtherStackedOnMe", at = @At("HEAD"), cancellable = true)
    public void overrideOtherStackedOnMe$inject$head(ItemStack stack, ItemStack stackOnMe, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess, CallbackInfoReturnable<Boolean> callbackInfo) {
        // some mods make empty shulker boxes stackable, disable this mod then as it would allow for item duplication otherwise
        if (stack.getCount() != 1) return;
        if (EasyShulkerBoxes.CONFIG.get(ServerConfig.class).allowShulkerBox && Block.byItem(stack.getItem()) instanceof ShulkerBoxBlock) {
            boolean success = ContainerItemHelper.overrideOtherStackedOnMe(stack, BlockEntityType.SHULKER_BOX, 3, stackOnMe, slot, clickAction, player, slotAccess, s -> s.getItem().canFitInsideContainerItems(), SoundEvents.BUNDLE_INSERT, SoundEvents.BUNDLE_REMOVE_ONE);
            callbackInfo.setReturnValue(success);
        } else if (EasyShulkerBoxes.CONFIG.get(ServerConfig.class).allowEnderChest && Block.byItem(stack.getItem()) instanceof EnderChestBlock) {
            boolean success = ContainerItemHelper.overrideOtherStackedOnMe(player::getEnderChestInventory, stackOnMe, slot, clickAction, player, slotAccess, s -> true, SoundEvents.BUNDLE_INSERT, SoundEvents.BUNDLE_REMOVE_ONE);
            // will only actually broadcast when in creative menu as that menu needs manual syncing
            if (success && player.level.isClientSide) ModRegistry.ENDER_CHEST_MENU_CAPABILITY.maybeGet(player).map(EnderChestMenuCapability::getEnderChestMenu).ifPresent(AbstractContainerMenu::broadcastChanges);
            callbackInfo.setReturnValue(success);
        }
    }

    @Inject(method = "getTooltipImage", at = @At("HEAD"), cancellable = true)
    public void getTooltipImage$inject$head(ItemStack stack, CallbackInfoReturnable<Optional<TooltipComponent>> callbackInfo) {
        // some mods make empty shulker boxes stackable, disable this mod then as it would allow for item duplication otherwise
        if (stack.getCount() != 1) return;
        if (EasyShulkerBoxes.CONFIG.get(ServerConfig.class).allowShulkerBox && Block.byItem(stack.getItem()) instanceof ShulkerBoxBlock) {
            DyeColor color = ShulkerBoxBlock.getColorFromItem(stack.getItem());
            Optional<TooltipComponent> component = ContainerItemHelper.getTooltipImage(stack, BlockEntityType.SHULKER_BOX, 3, color);
            callbackInfo.setReturnValue(component);
        } else if (EasyShulkerBoxes.CONFIG.get(ServerConfig.class).allowEnderChest && Block.byItem(stack.getItem()) instanceof EnderChestBlock) {
            Optional<TooltipComponent> component = ContainerItemHelper.getTooltipImage(Proxy.INSTANCE.getClientPlayer().getEnderChestInventory(), 3, null);
            callbackInfo.setReturnValue(component);
        }
    }
}
