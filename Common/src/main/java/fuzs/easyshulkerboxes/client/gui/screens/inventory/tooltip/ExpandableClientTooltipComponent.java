package fuzs.easyshulkerboxes.client.gui.screens.inventory.tooltip;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.entity.ItemRenderer;

public interface ExpandableClientTooltipComponent {

    int getExpandedHeight();

    int getExpandedWidth(Font font);

    void renderExpandedImage(Font font, int mouseX, int mouseY, PoseStack poseStack, ItemRenderer itemRenderer, int blitOffset);
}
