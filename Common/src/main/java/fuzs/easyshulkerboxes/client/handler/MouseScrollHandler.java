package fuzs.easyshulkerboxes.client.handler;

import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.config.ClientConfig;
import fuzs.easyshulkerboxes.config.ServerConfig;
import fuzs.easyshulkerboxes.network.client.C2SCurrentSlotMessage;
import fuzs.easyshulkerboxes.api.world.item.container.ItemContainerProvider;
import fuzs.easyshulkerboxes.world.inventory.helper.ContainerSlotHelper;
import fuzs.puzzleslib.client.gui.screens.CommonScreens;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.util.Unit;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MouseScrollHandler {
    private static int lastHoveredContainerItemSlotIndex = -1;
    private static int lastSentContainerSlot = -1;

    public static Optional<Unit> onMouseScroll(Screen screen, double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (!shouldHandleMouseScroll(screen)) return Optional.empty();
        Slot hoveredSlot = CommonScreens.INSTANCE.getHoveredSlot((AbstractContainerScreen<?>) screen);
        if (hoveredSlot != null) {
            ItemStack stack = hoveredSlot.getItem();
            ItemContainerProvider provider = ItemContainerProvider.get(stack.getItem());
            if (provider != null) {
                int signum = (int) Math.signum(verticalAmount);
                if (signum != 0) {
                    Player player = CommonScreens.INSTANCE.getMinecraft(screen).player;
                    int currentContainerSlot = ContainerSlotHelper.getCurrentContainerSlot(player);
                    Optional<SimpleContainer> optional = provider.getItemContainer(player, stack, false);
                    if (optional.isEmpty()) return Optional.empty();
                    currentContainerSlot = ContainerSlotHelper.findClosestSlotWithContent(optional.get(), currentContainerSlot, signum < 0);
                    ContainerSlotHelper.setCurrentContainerSlot(player, currentContainerSlot);
                }
                return Optional.of(Unit.INSTANCE);
            }
        }
        return Optional.empty();
    }

    private static boolean shouldHandleMouseScroll(Screen screen) {
        if (!(screen instanceof AbstractContainerScreen<?>)) return false;
        if (!EasyShulkerBoxes.CONFIG.get(ServerConfig.class).allowSlotCycling) return false;
        return EasyShulkerBoxes.CONFIG.get(ClientConfig.class).revealContents.isActive();
    }

    public static void onClientTick$End(Minecraft minecraft) {
        getHoveredSlotWithContainerItem(minecraft.screen).ifPresent(slot -> {
            if (lastHoveredContainerItemSlotIndex != slot.getContainerSlot()) {
                lastHoveredContainerItemSlotIndex = slot.getContainerSlot();
                ContainerSlotHelper.resetCurrentContainerSlot(minecraft.player);
            }
        });
    }

    private static Optional<Slot> getHoveredSlotWithContainerItem(@Nullable Screen screen) {
        if (!shouldHandleMouseScroll(screen)) return Optional.empty();
        Slot hoveredSlot = CommonScreens.INSTANCE.getHoveredSlot((AbstractContainerScreen<?>) screen);
        if (hoveredSlot != null && ItemContainerProvider.canSupplyProvider(hoveredSlot.getItem())) {
            return Optional.of(hoveredSlot);
        }
        return Optional.empty();
    }

    public static void ensureHasSentCurrentSlot() {
        int currentContainerSlot = ContainerSlotHelper.getCurrentContainerSlot(Minecraft.getInstance().player);
        if (currentContainerSlot != lastSentContainerSlot) {
            lastSentContainerSlot = currentContainerSlot;
            EasyShulkerBoxes.NETWORK.sendToServer(new C2SCurrentSlotMessage(currentContainerSlot));
        }
    }
}
