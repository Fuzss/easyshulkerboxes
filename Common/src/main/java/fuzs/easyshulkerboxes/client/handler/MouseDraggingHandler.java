package fuzs.easyshulkerboxes.client.handler;

import com.google.common.collect.Sets;
import com.mojang.blaze3d.platform.InputConstants;
import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.api.world.item.container.ItemContainerProvider;
import fuzs.easyshulkerboxes.config.ClientConfig;
import fuzs.easyshulkerboxes.config.ServerConfig;
import fuzs.easyshulkerboxes.mixin.client.accessor.AbstractContainerScreenAccessor;
import fuzs.easyshulkerboxes.world.item.storage.ItemContainerProvidersListener;
import fuzs.puzzleslib.client.gui.screens.CommonScreens;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class MouseDraggingHandler {
    public static final MouseDraggingHandler INSTANCE = new MouseDraggingHandler();

    public final Set<Slot> containerDragSlots = Sets.newHashSet();
    @Nullable
    private ContainerDragType containerDragType;

    public Optional<Unit> onBeforeMousePressed(Screen screen, double mouseX, double mouseY, int button) {
        if (!shouldHandleMouseDragging(screen)) return Optional.empty();
        ItemStack carriedStack = ((AbstractContainerScreen<?>) screen).getMenu().getCarried();
        ItemContainerProvider provider = ItemContainerProvidersListener.INSTANCE.get(carriedStack);
        Minecraft minecraft = CommonScreens.INSTANCE.getMinecraft(screen);
        if (validMouseButton(button) && provider != null && provider.allowsPlayerInteractions(carriedStack, minecraft.player)) {
            Slot slot = ((AbstractContainerScreenAccessor) screen).easyshulkerboxes$findSlot(mouseX, mouseY);
            if (slot != null) {
                if (slot.hasItem() && !EasyShulkerBoxes.CONFIG.get(ClientConfig.class).precisionMode.isActive()) {
                    this.containerDragType = ContainerDragType.INSERT;
                } else {
                    this.containerDragType = ContainerDragType.REMOVE;
                }
                this.containerDragSlots.clear();
                return Optional.of(Unit.INSTANCE);
            }
        }
        this.containerDragType = null;
        return Optional.empty();
    }

    public Optional<Unit> onBeforeMouseDragged(Screen screen, double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (!shouldHandleMouseDragging(screen)) return Optional.empty();
        if (this.containerDragType != null) {
            if (!validMouseButton(button)) {
                this.containerDragType = null;
                this.containerDragSlots.clear();
                return Optional.empty();
            }
            Slot slot = ((AbstractContainerScreenAccessor) screen).easyshulkerboxes$findSlot(mouseX, mouseY);
            AbstractContainerMenu menu = ((AbstractContainerScreen<?>) screen).getMenu();
            if (slot != null && menu.canDragTo(slot) && !this.containerDragSlots.contains(slot)) {
                ItemStack carriedStack = menu.getCarried();
                ItemContainerProvider provider = ItemContainerProvidersListener.INSTANCE.get(carriedStack);
                Objects.requireNonNull(provider, "provider is null");
                Minecraft minecraft = CommonScreens.INSTANCE.getMinecraft(screen);
                boolean interact = false;
                if (this.containerDragType == ContainerDragType.INSERT && slot.hasItem() && provider.canAddItem(carriedStack, slot.getItem(), minecraft.player)) {
                    interact = true;
                } else if (this.containerDragType == ContainerDragType.REMOVE && (button == InputConstants.MOUSE_BUTTON_RIGHT && !slot.hasItem() || button == InputConstants.MOUSE_BUTTON_LEFT && slot.hasItem() && EasyShulkerBoxes.CONFIG.get(ClientConfig.class).precisionMode.isActive())) {
                    if (!provider.getItemContainer(carriedStack, minecraft.player, false).isEmpty()) {
                        interact = true;
                    }
                }
                if (interact) {
                    ((AbstractContainerScreenAccessor) screen).easyshulkerboxes$slotClicked(slot, slot.index, button, ClickType.PICKUP);
                    this.containerDragSlots.add(slot);
                    return Optional.of(Unit.INSTANCE);
                }
            }
        }
        return Optional.empty();
    }

    public Optional<Unit> onBeforeMouseRelease(Screen screen, double mouseX, double mouseY, int button) {
        if (!shouldHandleMouseDragging(screen)) return Optional.empty();
        if (this.containerDragType != null) {
            if (validMouseButton(button) && !this.containerDragSlots.isEmpty()) {
                if (!EasyShulkerBoxes.CONFIG.get(ClientConfig.class).disableInteractionSounds) {
                    // play this manually at the end, we suppress all interaction sounds played while dragging
                    SimpleSoundInstance sound = SimpleSoundInstance.forUI(this.containerDragType.sound, 0.8F, 0.8F + SoundInstance.createUnseededRandom().nextFloat() * 0.4F);
                    CommonScreens.INSTANCE.getMinecraft(screen).getSoundManager().play(sound);
                }
                this.containerDragType = null;
                this.containerDragSlots.clear();
                return Optional.of(Unit.INSTANCE);
            }
            this.containerDragType = null;
        }
        this.containerDragSlots.clear();
        return Optional.empty();
    }

    private static boolean shouldHandleMouseDragging(Screen screen) {
        if (!(screen instanceof AbstractContainerScreen<?>)) return false;
        return EasyShulkerBoxes.CONFIG.get(ServerConfig.class).allowMouseDragging;
    }

    private static boolean validMouseButton(int button) {
        if (button == InputConstants.MOUSE_BUTTON_LEFT) {
            return EasyShulkerBoxes.CONFIG.get(ClientConfig.class).precisionMode.isActive();
        }
        return button == InputConstants.MOUSE_BUTTON_RIGHT;
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

        public final SoundEvent sound;

        ContainerDragType(SoundEvent sound) {
            this.sound = sound;
        }
    }
}
