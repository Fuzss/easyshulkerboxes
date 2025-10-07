package fuzs.easyshulkerboxes.client.gui.screens.inventory.tooltip;

import fuzs.easyshulkerboxes.world.inventory.tooltip.MapContentsTooltip;
import fuzs.iteminteractions.api.v1.client.tooltip.ExpandableClientContentsTooltip;
import fuzs.puzzleslib.api.core.v1.utility.ResourceLocationHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MapRenderer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.Nullable;

public class ClientMapContentsTooltip extends ExpandableClientContentsTooltip {
    private static final ResourceLocation MAP_BACKGROUND_CHECKERBOARD = ResourceLocationHelper.withDefaultNamespace(
            "textures/map/map_background_checkerboard.png");

    private final MapRenderer mapRenderer = Minecraft.getInstance().getMapRenderer();
    private final MapRenderState mapRenderState = new MapRenderState();
    private final MapId mapId;
    private final MapItemSavedData mapData;

    public ClientMapContentsTooltip(MapContentsTooltip tooltip) {
        this.mapId = tooltip.mapId();
        this.mapData = tooltip.savedData();
    }

    @Override
    public int getExpandedHeight(Font font) {
        return 66;
    }

    @Override
    public int getExpandedWidth(Font font) {
        return 64;
    }

    @Override
    public void renderExpandedImage(Font font, int mouseX, int mouseY, GuiGraphics guiGraphics) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                MAP_BACKGROUND_CHECKERBOARD,
                mouseX,
                mouseY,
                0,
                0,
                64,
                64,
                64,
                64);
        this.renderMap(guiGraphics, this.mapId, this.mapData, mouseX + 3, mouseY + 3, 0.45F);
    }

    private void renderMap(GuiGraphics guiGraphics, @Nullable MapId mapId, @Nullable MapItemSavedData mapData, int x, int y, float scale) {
        if (mapId != null && mapData != null) {
            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().translate(x, y);
            guiGraphics.pose().scale(scale, scale);
            this.mapRenderer.extractRenderState(mapId, mapData, this.mapRenderState);
            guiGraphics.submitMapRenderState(this.mapRenderState);
            guiGraphics.pose().popMatrix();
        }
    }
}
