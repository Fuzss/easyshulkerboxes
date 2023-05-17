package fuzs.easyshulkerboxes.impl.client.handler;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.api.container.v1.provider.ItemContainerProvider;
import fuzs.easyshulkerboxes.impl.config.ClientConfig;
import fuzs.easyshulkerboxes.impl.config.ServerConfig;
import fuzs.easyshulkerboxes.mixin.client.accessor.AbstractContainerScreenAccessor;
import fuzs.easyshulkerboxes.mixin.client.accessor.ScreenAccessor;
import fuzs.easyshulkerboxes.impl.network.client.C2SContainerClientInputMessage;
import fuzs.easyshulkerboxes.impl.world.inventory.ContainerSlotHelper;
import fuzs.easyshulkerboxes.impl.world.item.container.ItemContainerProviders;
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

import java.util.Objects;
import java.util.Optional;

public class ClientInputActionHandler {
    private static int lastSentContainerSlot = -1;
    private static boolean lastSentExtractSingleItem;

    public static Optional<Unit> onBeforeKeyPressed(Screen screen, int keyCode, int scanCode, int modifiers) {
        // this must be sent before any slot click action is performed server side, by vanilla this can be caused by either mouse clicks (normal menu interactions)
        // or key presses (hotbar keys for swapping items to those slots)
        // this is already added via mixin to where vanilla sends the click packet, but creative screen doesn't use it, and you never know with other mods...
        if (!(screen instanceof AbstractContainerScreen<?>)) return Optional.empty();
        Minecraft minecraft = CommonScreens.INSTANCE.getMinecraft(screen);
        ensureHasSentContainerClientInput(screen, minecraft.player);
        return Optional.empty();
    }

    public static Optional<Unit> onBeforeMousePressed(Screen screen, double mouseX, double mouseY, int button) {
        // this must be sent before any slot click action is performed server side, by vanilla this can be caused by either mouse clicks (normal menu interactions)
        // or key presses (hotbar keys for swapping items to those slots)
        // this is already added via mixin to where vanilla sends the click packet, but creative screen doesn't use it, and you never know with other mods...
        if (!(screen instanceof AbstractContainerScreen<?>)) return Optional.empty();
        Minecraft minecraft = CommonScreens.INSTANCE.getMinecraft(screen);
        ensureHasSentContainerClientInput(screen, minecraft.player);
        return Optional.empty();
    }

    public static Optional<Unit> onBeforeMouseRelease(Screen screen, double mouseX, double mouseY, int button) {
        if (!(screen instanceof AbstractContainerScreen<?> containerScreen)) return Optional.empty();
        // prevent vanilla double click feature from interfering with our precision mode, adding an unnecessary delay when quickly inserting items via left-click
        // it wouldn't work anyway, and right-click is fine, leading to inconsistent behavior
        if (precisionModeAllowedAndActive() && !getContainerStack(containerScreen, false).isEmpty()) {
            ((AbstractContainerScreenAccessor) screen).easyshulkerboxes$setDoubleclick(false);
        }
        return Optional.empty();
    }

    public static void onAfterRender(Screen screen, PoseStack matrices, int mouseX, int mouseY, float tickDelta) {
        // renders vanilla item tooltips when a stack is carried and the cursor hovers over a container item
        // intended to be used with single item extraction/insertion feature to be able to continuously see what's going on in the container item
        if (!(screen instanceof AbstractContainerScreen<?> containerScreen)) return;
        if (!EasyShulkerBoxes.CONFIG.get(ClientConfig.class).carriedItemTooltips.isActive()) return;
        if (!containerScreen.getMenu().getCarried().isEmpty()) {
            ItemStack stack = getContainerStack(containerScreen, false);
            if (!stack.isEmpty()) {
                ((ScreenAccessor) screen).easyshulkerboxes$callRenderTooltip(matrices, stack, mouseX, mouseY);
            }
        }
    }

    private static boolean renderProviderItemTooltip(Screen screen, PoseStack matrices, int mouseX, int mouseY, ItemStack stack) {
        if (ItemContainerProviders.INSTANCE.get(stack) != null) {
            ((ScreenAccessor) screen).easyshulkerboxes$callRenderTooltip(matrices, stack, mouseX, mouseY);
            return true;
        }
        return false;
    }

    public static Optional<Unit> onBeforeMouseScroll(Screen screen, double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        // allows to scroll between filled slots on a container items tooltip to select the slot to be interacted with next
        if (verticalAmount == 0.0 || !(screen instanceof AbstractContainerScreen<?> containerScreen)) return Optional.empty();
        if (!EasyShulkerBoxes.CONFIG.get(ClientConfig.class).revealContents.isActive()) return Optional.empty();
        Slot slot = CommonScreens.INSTANCE.getHoveredSlot(containerScreen);
        if (precisionModeAllowedAndActive()) {
            if (slot != null) {
                if (ItemContainerProviders.INSTANCE.get(containerScreen.getMenu().getCarried()) != null || ItemContainerProviders.INSTANCE.get(slot.getItem()) != null) {
                    int mouseButton = (EasyShulkerBoxes.CONFIG.get(ClientConfig.class).invertPrecisionModeScrolling ? verticalAmount < 0.0 : verticalAmount > 0.0) ? InputConstants.MOUSE_BUTTON_RIGHT : InputConstants.MOUSE_BUTTON_LEFT;
                    ((AbstractContainerScreenAccessor) screen).easyshulkerboxes$callSlotClicked(slot, slot.index, mouseButton, ClickType.PICKUP);
                    return Optional.of(Unit.INSTANCE);
                }
            }
        } else if (EasyShulkerBoxes.CONFIG.get(ServerConfig.class).allowSlotCycling) {
            ItemStack carriedStack = containerScreen.getMenu().getCarried();
            if (!carriedStack.isEmpty() && !EasyShulkerBoxes.CONFIG.get(ClientConfig.class).carriedItemTooltips.isActive()) {
                return Optional.empty();
            }
            ItemStack stack = getContainerStack(containerScreen, true);
            if (!stack.isEmpty()) {
                Minecraft minecraft = CommonScreens.INSTANCE.getMinecraft(screen);
                int currentContainerSlot = ContainerSlotHelper.getCurrentContainerSlot(minecraft.player);
                ItemContainerProvider provider = ItemContainerProviders.INSTANCE.get(stack);
                Objects.requireNonNull(provider, "provider is null");
                SimpleContainer container = provider.getItemContainer(stack, minecraft.player, false);
                currentContainerSlot = ContainerSlotHelper.findClosestSlotWithContent(container, currentContainerSlot, verticalAmount < 0.0);
                ContainerSlotHelper.setCurrentContainerSlot(minecraft.player, currentContainerSlot);
                return Optional.of(Unit.INSTANCE);
            }
        }
        return Optional.empty();
    }

    public static ItemStack getContainerStack(AbstractContainerScreen<?> screen, boolean requireItemContainerData) {
        ItemStack stack = screen.getMenu().getCarried();
        ItemContainerProvider provider = ItemContainerProviders.INSTANCE.get(stack);
        if (provider != null && (!requireItemContainerData || provider.hasItemContainerData(stack))) {
            return stack;
        }
        Slot slot = CommonScreens.INSTANCE.getHoveredSlot(screen);
        if (slot != null) {
            stack = slot.getItem();
            provider = ItemContainerProviders.INSTANCE.get(stack);
            if (provider != null && (!requireItemContainerData || provider.hasItemContainerData(stack))) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
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
