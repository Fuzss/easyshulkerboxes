package fuzs.easyshulkerboxes.mixin.client;

import fuzs.easyshulkerboxes.client.handler.MouseScrollHandler;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
abstract class MultiPlayerGameModeMixin {

    @Inject(method = "handleInventoryMouseClick", at = @At("HEAD"))
    public void easyshulkerboxes$handleInventoryMouseClick(int containerId, int slotId, int mouseButton, ClickType clickType, Player player, CallbackInfo callback) {
        AbstractContainerMenu abstractContainerMenu = player.containerMenu;
        if (containerId == abstractContainerMenu.containerId) {
            MouseScrollHandler.ensureHasSentCurrentSlot();
        }
    }
}
