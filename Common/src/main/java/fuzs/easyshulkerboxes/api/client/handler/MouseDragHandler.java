package fuzs.easyshulkerboxes.api.client.handler;

import com.google.common.collect.Sets;
import fuzs.easyshulkerboxes.api.config.ServerConfigCore;
import fuzs.easyshulkerboxes.api.world.inventory.ContainerItemProvider;
import fuzs.easyshulkerboxes.mixin.client.accessor.AbstractContainerScreenAccessor;
import fuzs.puzzleslib.client.gui.screens.CommonScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
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

    public Optional<Unit> onMousePress(Screen screen, double mouseX, double mouseY, int button, ServerConfigCore serverConfig) {
        if (!serverConfig.allowMouseDragging()) return Optional.empty();
        if (!(screen instanceof AbstractContainerScreen<?> containerScreen)) return Optional.empty();
        ItemStack carriedStack = containerScreen.getMenu().getCarried();
        if (button == 1 && ContainerItemProvider.canSupplyProvider(carriedStack)) {
            Slot slot = ((AbstractContainerScreenAccessor) containerScreen).callFindSlot(mouseX, mouseY);
            if (slot != null && slot.hasItem()) {
                this.containerDragType = ContainerDragType.INSERT;
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
                if (this.containerDragType == ContainerDragType.INSERT && slot.hasItem() && provider.canAcceptItem(carriedStack, slot.getItem())) {
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
            if (button == 1 && !this.containerDragSlots.isEmpty()) {
                // play this manually at the end, we suppress all interaction sounds played while dragging
                SimpleSoundInstance sound = SimpleSoundInstance.forUI(this.containerDragType.sound, 0.8F, 0.8F + SoundInstance.createUnseededRandom().nextFloat() * 0.4F);
                CommonScreens.INSTANCE.getMinecraft(screen).getSoundManager().play(sound);
                this.containerDragType = null;
                this.containerDragSlots.clear();
                return Optional.of(Unit.INSTANCE);
            }
            this.containerDragType = null;
        }
        this.containerDragSlots.clear();
        return Optional.empty();
    }

    public Optional<Unit> onPlaySoundAtPosition(Entity entity, SoundEvent sound, SoundSource source, float volume, float pitch) {
        // prevent the bundle sounds from being spammed when dragging, not a nice solution, but it works
        if (this.containerDragType != null && source == SoundSource.PLAYERS) {
            if (sound == this.containerDragType.sound) {
                return Optional.of(Unit.INSTANCE);
            }
        }
        return Optional.empty();
    }

    private enum ContainerDragType {
        INSERT(SoundEvents.BUNDLE_INSERT), REMOVE(SoundEvents.BUNDLE_REMOVE_ONE);

        private final SoundEvent sound;

        ContainerDragType(SoundEvent sound) {
            this.sound = sound;
        }
    }
}
