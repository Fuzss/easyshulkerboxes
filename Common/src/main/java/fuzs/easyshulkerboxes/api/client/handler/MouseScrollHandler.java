package fuzs.easyshulkerboxes.api.client.handler;

import fuzs.easyshulkerboxes.api.SimpleInventoryContainersApi;
import fuzs.easyshulkerboxes.api.config.ClientConfigCore;
import fuzs.easyshulkerboxes.api.config.ServerConfigCore;
import fuzs.easyshulkerboxes.api.network.C2SCurrentSlotMessage;
import fuzs.easyshulkerboxes.api.world.inventory.ContainerItemProvider;
import fuzs.easyshulkerboxes.api.world.inventory.ContainerSlotHelper;
import fuzs.puzzleslib.client.gui.screens.CommonScreens;
import fuzs.puzzleslib.proxy.Proxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MouseScrollHandler {
    private static int lastHoveredContainerItemSlotIndex = -1;
    private static int lastSentContainerSlot = -1;

    public static Optional<Unit> onMouseScroll(Screen screen, double mouseX, double mouseY, double horizontalAmount, double verticalAmount, ClientConfigCore clientConfig, ServerConfigCore serverConfig) {
        if (!serverConfig.allowSlotCycling()) return Optional.empty();
        if (showInventoryContents(clientConfig) && screen instanceof AbstractContainerScreen<?> containerScreen) {
            Slot hoveredSlot = CommonScreens.INSTANCE.getHoveredSlot(containerScreen);
            if (hoveredSlot != null) {
                ItemStack stack = hoveredSlot.getItem();
                if (ContainerItemProvider.suppliesContainerProvider(stack)) {
                    int signum = (int) Math.signum(verticalAmount);
                    if (signum != 0) {
                        Player player = CommonScreens.INSTANCE.getMinecraft(screen).player;
                        int currentContainerSlot = ContainerSlotHelper.getCurrentContainerSlot(player);
                        currentContainerSlot = ContainerSlotHelper.findClosestSlotWithContent(ContainerItemProvider.get(stack.getItem()).getItemContainer(player, stack, false), currentContainerSlot, signum < 0);
                        ContainerSlotHelper.setCurrentContainerSlot(player, currentContainerSlot);
                    }
                    return Optional.of(Unit.INSTANCE);
                }
            }
        }
        return Optional.empty();
    }

    public static void onClientTick$End(Minecraft minecraft, ClientConfigCore clientConfig) {
        getHoveredSlotWithContainerItem(minecraft.screen, clientConfig).ifPresent(slot -> {
            if (lastHoveredContainerItemSlotIndex != slot.getContainerSlot()) {
                lastHoveredContainerItemSlotIndex = slot.getContainerSlot();
                ContainerSlotHelper.resetCurrentContainerSlot(minecraft.player);
            }
        });
    }

    private static Optional<Slot> getHoveredSlotWithContainerItem(@Nullable Screen screen, ClientConfigCore clientConfig) {
        if (showInventoryContents(clientConfig) && screen instanceof AbstractContainerScreen<?> containerScreen) {
            Slot hoveredSlot = CommonScreens.INSTANCE.getHoveredSlot(containerScreen);
            if (hoveredSlot != null && ContainerItemProvider.suppliesContainerProvider(hoveredSlot.getItem())) {
                return Optional.of(hoveredSlot);
            }
        }
        return Optional.empty();
    }

    public static boolean showInventoryContents(ClientConfigCore clientConfig) {
        return !clientConfig.contentsRequireShift() || Proxy.INSTANCE.hasShiftDown();
    }

    public static void ensureHasSentCurrentSlot() {
        int currentContainerSlot = ContainerSlotHelper.getCurrentContainerSlot(Minecraft.getInstance().player);
        if (currentContainerSlot != lastSentContainerSlot) {
            lastSentContainerSlot = currentContainerSlot;
            SimpleInventoryContainersApi.NETWORK.sendToServer(new C2SCurrentSlotMessage(currentContainerSlot));
        }
    }
}
