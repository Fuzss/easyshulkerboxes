package fuzs.easyshulkerboxes.client.handler;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.api.world.item.container.ItemContainerProvider;
import fuzs.easyshulkerboxes.config.ClientConfig;
import fuzs.easyshulkerboxes.config.ServerConfig;
import fuzs.easyshulkerboxes.mixin.client.accessor.AbstractContainerScreenAccessor;
import fuzs.easyshulkerboxes.mixin.client.accessor.ScreenAccessor;
import fuzs.easyshulkerboxes.network.client.C2SContainerClientInputMessage;
import fuzs.easyshulkerboxes.world.inventory.helper.ContainerSlotHelper;
import fuzs.easyshulkerboxes.world.item.storage.ItemContainerProvidersListener;
import fuzs.puzzleslib.client.gui.screens.CommonScreens;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Unit;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class ClientInputActionHandler {
    private static int lastSentContainerSlot = -1;
    private static boolean lastSentExtractSingleItem;

    public static Optional<Unit> onBeforeKeyPressed(Screen screen, int keyCode, int scanCode, int modifiers) {
        // this must be sent before any slot click action is performed server side, by vanilla this can be caused by either mouse clicks (normal menu interactions)
        // or key presses (hotbar keys for swapping items to those slots)
        // this is already added via mixin to where vanilla sends the click packet, but creative screen doesn't use it, and you never know with other mods...
        Minecraft minecraft = CommonScreens.INSTANCE.getMinecraft(screen);
        ensureHasSentContainerClientInput(screen, minecraft.player);
        return Optional.empty();
    }

    public static Optional<Unit> onBeforeMousePressed(Screen screen, double mouseX, double mouseY, int button) {
        // this must be sent before any slot click action is performed server side, by vanilla this can be caused by either mouse clicks (normal menu interactions)
        // or key presses (hotbar keys for swapping items to those slots)
        // this is already added via mixin to where vanilla sends the click packet, but creative screen doesn't use it, and you never know with other mods...
        Minecraft minecraft = CommonScreens.INSTANCE.getMinecraft(screen);
        ensureHasSentContainerClientInput(screen, minecraft.player);
        return Optional.empty();
    }

    public static void onAfterRender(Screen screen, PoseStack matrices, int mouseX, int mouseY, float tickDelta) {
        // renders vanilla item tooltips when a stack is carried and the cursor hovers over a container item
        // intended to be used with single item extraction/insertion feature to be able to continuously see what's going on in the container item
        if (!(screen instanceof AbstractContainerScreen<?> containerScreen)) return;
        if (EasyShulkerBoxes.CONFIG.get(ClientConfig.class).carriedItemTooltips.isActive()) {
            ItemStack stack = containerScreen.getMenu().getCarried();
            if (!stack.isEmpty()) {
                if (!renderProviderItemTooltip(screen, matrices, mouseX, mouseY, stack)) {
                    Slot slot = CommonScreens.INSTANCE.getHoveredSlot(containerScreen);
                    if (slot != null) {
                        renderProviderItemTooltip(screen, matrices, mouseX, mouseY, slot.getItem());
                    }
                }
            }
        }
    }

    private static boolean renderProviderItemTooltip(Screen screen, PoseStack matrices, int mouseX, int mouseY, ItemStack stack) {
        if (ItemContainerProvidersListener.INSTANCE.get(stack) != null) {
            ((ScreenAccessor) screen).easyshulkerboxes$callRenderTooltip(matrices, stack, mouseX, mouseY);
            return true;
        }
        return false;
    }

    public static Optional<Unit> onBeforeMouseScroll(Screen screen, double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        // allows to scroll between filled slots on a container items tooltip to select the slot to be interacted with next
        if (verticalAmount == 0.0F || !(screen instanceof AbstractContainerScreen<?> containerScreen)) return Optional.empty();
        if (!EasyShulkerBoxes.CONFIG.get(ClientConfig.class).revealContents.isActive()) return Optional.empty();
        Slot slot = CommonScreens.INSTANCE.getHoveredSlot(containerScreen);
        if (precisionModeAllowedAndActive()) {
            if (slot != null) {
                if (ItemContainerProvidersListener.INSTANCE.get(containerScreen.getMenu().getCarried()) != null || ItemContainerProvidersListener.INSTANCE.get(slot.getItem()) != null) {
                    int mouseButton = verticalAmount > 0 ? InputConstants.MOUSE_BUTTON_RIGHT : InputConstants.MOUSE_BUTTON_LEFT;
                    ((AbstractContainerScreenAccessor) screen).easyshulkerboxes$slotClicked(slot, slot.index, mouseButton, ClickType.PICKUP);
                    return Optional.of(Unit.INSTANCE);
                }
            }
        } else if (EasyShulkerBoxes.CONFIG.get(ServerConfig.class).allowSlotCycling) {
            ItemStack stack = containerScreen.getMenu().getCarried();
            if (!stack.isEmpty() && !EasyShulkerBoxes.CONFIG.get(ClientConfig.class).carriedItemTooltips.isActive()) {
                return Optional.empty();
            }
            ItemContainerProvider provider = ItemContainerProvidersListener.INSTANCE.get(stack);
            if (slot != null && (provider == null || !provider.hasItemContainerData(stack))) {
                provider = ItemContainerProvidersListener.INSTANCE.get(slot.getItem());
                stack = slot.getItem();
            }
            if (provider != null && provider.hasItemContainerData(stack)) {
                Minecraft minecraft = CommonScreens.INSTANCE.getMinecraft(screen);
                int currentContainerSlot = ContainerSlotHelper.getCurrentContainerSlot(minecraft.player);
                SimpleContainer container = provider.getItemContainer(stack, minecraft.player, false);
                currentContainerSlot = ContainerSlotHelper.findClosestSlotWithContent(container, currentContainerSlot, verticalAmount < 0);
                ContainerSlotHelper.setCurrentContainerSlot(minecraft.player, currentContainerSlot);
                return Optional.of(Unit.INSTANCE);
            }
        }
        return Optional.empty();
    }

    public static Optional<Unit> onPlaySoundAtPosition(Entity entity, SoundEvent sound, SoundSource source, float volume, float pitch) {
        if (!EasyShulkerBoxes.CONFIG.get(ClientConfig.class).disableInteractionSounds) return Optional.empty();
        if (source == SoundSource.PLAYERS && (sound == SoundEvents.BUNDLE_INSERT || sound == SoundEvents.BUNDLE_REMOVE_ONE)) {
            return Optional.of(Unit.INSTANCE);
        }
        return Optional.empty();
    }

    public static boolean precisionModeAllowedAndActive() {
        return EasyShulkerBoxes.CONFIG.get(ServerConfig.class).allowPrecisionMode && EasyShulkerBoxes.CONFIG.get(ClientConfig.class).precisionMode.isActive();
    }

    public static void ensureHasSentContainerClientInput(Screen screen, Player player) {
        if (!(screen instanceof AbstractContainerScreen<?>)) return;
        int currentContainerSlot = ContainerSlotHelper.getCurrentContainerSlot(player);
        boolean extractSingleItem = precisionModeAllowedAndActive();
        if (currentContainerSlot != lastSentContainerSlot || extractSingleItem != lastSentExtractSingleItem) {
            lastSentContainerSlot = currentContainerSlot;
            lastSentExtractSingleItem = extractSingleItem;
            // this is where the client sets this value, so it's important to call before click actions even when syncing isn't so important (applies mostly to creative menu)
            ContainerSlotHelper.extractSingleItem(player, extractSingleItem);
            EasyShulkerBoxes.NETWORK.sendToServer(new C2SContainerClientInputMessage(currentContainerSlot, extractSingleItem));
        }
    }
}
