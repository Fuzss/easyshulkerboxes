package fuzs.easyshulkerboxes.mixin.client.accessor;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Screen.class)
public interface ScreenAccessor {

    @Invoker("renderTooltip")
    void easyshulkerboxes$callRenderTooltip(PoseStack poseStack, ItemStack itemStack, int mouseX, int mouseY);
}
