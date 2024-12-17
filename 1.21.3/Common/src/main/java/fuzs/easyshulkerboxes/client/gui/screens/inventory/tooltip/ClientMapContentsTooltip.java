package fuzs.easyshulkerboxes.client.gui.screens.inventory.tooltip;

import com.mojang.blaze3d.systems.RenderSystem;
import fuzs.easyshulkerboxes.world.inventory.tooltip.MapContentsTooltip;
import fuzs.iteminteractions.api.v1.client.tooltip.ExpandableClientContentsTooltip;
import fuzs.puzzleslib.api.core.v1.utility.ResourceLocationHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MapRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.resources.ResourceLocation;

public class ClientMapContentsTooltip extends ExpandableClientContentsTooltip {
    private static final ResourceLocation MAP_BACKGROUND_CHECKERBOARD = ResourceLocationHelper.withDefaultNamespace(
            "textures/map/map_background_checkerboard.png");

    private final MapRenderer mapRenderer;
    private final MapRenderState mapRenderState;

    public ClientMapContentsTooltip(MapContentsTooltip tooltip) {
        Minecraft minecraft = Minecraft.getInstance();
        this.mapRenderer = minecraft.getMapRenderer();
        this.mapRenderState = new MapRenderState();
        this.mapRenderer.extractRenderState(tooltip.mapId(), tooltip.savedData(), this.mapRenderState);
    }

    @Override
    public int getExpandedHeight(Font font) {
        return 64;
    }

    @Override
    public int getExpandedWidth(Font font) {
        return 64;
    }

    @Override
    public void renderExpandedImage(Font font, int mouseX, int mouseY, GuiGraphics guiGraphics) {
        // thanks cartography table screen
        guiGraphics.pose().pushPose();
        guiGraphics.blit(RenderType::guiTextured, MAP_BACKGROUND_CHECKERBOARD, mouseX, mouseY, 0, 0, 64, 64, 64, 64);
        guiGraphics.pose().translate(mouseX + 3, mouseY + 3, 500.0);
        guiGraphics.pose().scale(0.45F, 0.45F, 1.0F);
        guiGraphics.drawSpecial((MultiBufferSource bufferSource) -> {
            this.mapRenderer.render(this.mapRenderState, guiGraphics.pose(), bufferSource, true, 0XF000F0);
        });
        guiGraphics.pose().popPose();
    }
}
