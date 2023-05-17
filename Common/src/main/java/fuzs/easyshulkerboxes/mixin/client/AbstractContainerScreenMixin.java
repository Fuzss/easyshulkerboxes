package fuzs.easyshulkerboxes.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.easyshulkerboxes.impl.client.handler.MouseDraggingHandler;
import fuzs.easyshulkerboxes.impl.client.helper.ItemDecorationHelper;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
abstract class AbstractContainerScreenMixin<T extends AbstractContainerMenu> extends Screen {
    @Unique
    private int easyshulkerboxes$mouseX;
    @Unique
    private int easyshulkerboxes$mouseY;

    protected AbstractContainerScreenMixin(Component component) {
        super(component);
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick, CallbackInfo callback) {
        this.easyshulkerboxes$mouseX = mouseX;
        this.easyshulkerboxes$mouseY = mouseY;
    }

    // having 'remap = false' on here crashes Forge on start-up for some reason
    @Inject(method = "renderSlot", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;enableDepthTest()V", shift = At.Shift.BEFORE))
    private void renderSlot$0(PoseStack poseStack, Slot slot, CallbackInfo callback) {
        // this was supposed to render on the Forge container foreground event, but the blitOffset wouldn't behave
        // (it was rendering in front of items and behaved differently on the creative screen and there were also difference between Forge and Fabric for some reason)
        // so here goes the mixin ¯\_(ツ)_/¯
        if (MouseDraggingHandler.INSTANCE.containerDragSlots.contains(slot)) {
            // slots will sometimes be added to dragged slots when simply clicking on a slot, so don't render our overlay then
            if (MouseDraggingHandler.INSTANCE.containerDragSlots.size() > 1 || !this.isHovering(slot, this.easyshulkerboxes$mouseX, this.easyshulkerboxes$mouseY)) {
                GuiComponent.fill(poseStack, slot.x, slot.y, slot.x + 16, slot.y + 16, -2130706433);
            }
        }
    }

    @Shadow
    private boolean isHovering(Slot slot, double mouseX, double mouseY) {
        throw new RuntimeException();
    }

    @Inject(method = "renderSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;renderGuiItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", shift = At.Shift.BEFORE))
    private void renderSlot$1(PoseStack poseStack, Slot slot, CallbackInfo callback) {
        ItemDecorationHelper.setActiveSlot(slot);
    }

    @Inject(method = "renderSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;renderGuiItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", shift = At.Shift.AFTER))
    private void renderSlot$2(PoseStack poseStack, Slot slot, CallbackInfo callback) {
        ItemDecorationHelper.setActiveSlot(null);
    }
}
