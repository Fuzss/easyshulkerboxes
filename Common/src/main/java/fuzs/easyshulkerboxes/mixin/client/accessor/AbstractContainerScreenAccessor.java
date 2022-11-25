package fuzs.easyshulkerboxes.mixin.client.accessor;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import javax.annotation.Nullable;

@Mixin(AbstractContainerScreen.class)
public interface AbstractContainerScreenAccessor {

    @Accessor
    @Nullable
    Slot getClickedSlot();

    @Invoker
    Slot callFindSlot(double mouseX, double mouseY);

    @Invoker
    void callSlotClicked(Slot slot, int slotId, int mouseButton, ClickType type);
}
