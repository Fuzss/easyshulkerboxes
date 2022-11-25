package fuzs.easyshulkerboxes.api.client.handler;

import com.google.common.collect.Sets;
import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.easyshulkerboxes.api.world.inventory.ContainerItemProvider;
import fuzs.easyshulkerboxes.mixin.client.accessor.AbstractContainerScreenAccessor;
import fuzs.puzzleslib.client.gui.screens.CommonScreens;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;

public class MouseDragHandler {
    public static final MouseDragHandler INSTANCE = new MouseDragHandler();

    public final Set<Slot> containerDragSlots = Sets.newHashSet();
    @Nullable
    private ContainerDragType containerDragType;

    public Optional<Unit> onMousePress(Screen screen, double mouseX, double mouseY, int button) {
        if (!(screen instanceof AbstractContainerScreen<?> containerScreen)) return Optional.empty();
        ItemStack carriedStack = containerScreen.getMenu().getCarried();
        if (button == 1 && ContainerItemProvider.canSupplyProvider(carriedStack)) {
            Slot slot = ((AbstractContainerScreenAccessor) containerScreen).callFindSlot(mouseX, mouseY);
            if (slot != null && slot.hasItem()) {
                this.containerDragType = ContainerDragType.ADD;
            } else {
                this.containerDragType = ContainerDragType.REMOVE;
            }
            this.containerDragSlots.clear();
            return Optional.of(Unit.INSTANCE);
        } else {
            this.containerDragType = null;
        }
        return Optional.empty();
    }

    public Optional<Unit> onMouseDrag(Screen screen, double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (!(screen instanceof AbstractContainerScreen<?> containerScreen)) return Optional.empty();
        if (this.containerDragType != null) {
            if (button != 1) {
                this.containerDragType = null;
                this.containerDragSlots.clear();
                return Optional.empty();
            }
            Slot slot = ((AbstractContainerScreenAccessor) containerScreen).callFindSlot(mouseX, mouseY);
            if (slot != null && containerScreen.getMenu().canDragTo(slot) && !this.containerDragSlots.contains(slot)) {
                ItemStack carriedStack = containerScreen.getMenu().getCarried();
                ContainerItemProvider provider = ContainerItemProvider.get(carriedStack.getItem());
                boolean interact = false;
                if (this.containerDragType == ContainerDragType.ADD && slot.hasItem() && provider.canAcceptItem(carriedStack, slot.getItem())) {
                    interact = true;
                } else if (this.containerDragType == ContainerDragType.REMOVE && !slot.hasItem()) {
                    Player player = CommonScreens.INSTANCE.getMinecraft(screen).player;
                    if (!provider.isItemContainerEmpty(player, carriedStack)) {
                        interact = true;
                    }
                }
                if (interact) {
                    ((AbstractContainerScreenAccessor) containerScreen).callSlotClicked(slot, slot.index, 1, ClickType.PICKUP);
                    this.containerDragSlots.add(slot);
                    return Optional.of(Unit.INSTANCE);
                }
            }
        }
        return Optional.empty();
    }

    public Optional<Unit> onMouseRelease(Screen screen, double mouseX, double mouseY, int button) {
        if (!(screen instanceof AbstractContainerScreen<?> containerScreen)) return Optional.empty();
        if (this.containerDragType != null) {
            this.containerDragType = null;
            if (button == 1 && !this.containerDragSlots.isEmpty()) {
                this.containerDragSlots.clear();
                return Optional.of(Unit.INSTANCE);
            }
        }
        this.containerDragSlots.clear();
        return Optional.empty();
    }

    public void onDrawForeground(AbstractContainerScreen<?> containerScreen, PoseStack poseStack, int mouseX, int mouseY) {
        for (Slot slot : this.containerDragSlots) {
            if (slot != ((AbstractContainerScreenAccessor) containerScreen).getClickedSlot()) {
                GuiComponent.fill(poseStack, slot.x, slot.y, slot.x + 16, slot.y + 16, -2130706433);
            }
        }
    }

    private enum ContainerDragType {
        ADD, REMOVE
    }
}
