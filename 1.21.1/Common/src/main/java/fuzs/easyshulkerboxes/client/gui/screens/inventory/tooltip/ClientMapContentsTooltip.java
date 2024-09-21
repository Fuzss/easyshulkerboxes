package fuzs.easyshulkerboxes.client.gui.screens.inventory.tooltip;

import com.mojang.blaze3d.systems.RenderSystem;
import fuzs.easyshulkerboxes.world.inventory.tooltip.MapContentsTooltip;
import fuzs.iteminteractions.api.v1.client.tooltip.ExpandableClientContentsTooltip;
import fuzs.puzzleslib.api.core.v1.utility.ResourceLocationHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public class ClientMapContentsTooltip extends ExpandableClientContentsTooltip {
    private static final ResourceLocation MAP_BACKGROUND_CHECKERBOARD = ResourceLocationHelper.withDefaultNamespace("textures/map/map_background_checkerboard.png");

    private final MapId mapId;
    private final MapItemSavedData savedData;

    public ClientMapContentsTooltip(MapContentsTooltip tooltip) {
        this.mapId = tooltip.mapId();
        this.savedData = tooltip.savedData();
    }

    @Override
    public int getExpandedHeight() {
        return 64;
    }

    @Override
    public int getExpandedWidth(Font font) {
        return 64;
    }

    @Override
    public void renderExpandedImage(Font font, int mouseX, int mouseY, GuiGraphics guiGraphics) {
        // thanks cartography table screen
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.pose().pushPose();
        guiGraphics.blit(MAP_BACKGROUND_CHECKERBOARD, mouseX, mouseY, 0, 0, 0, 64, 64, 64, 64);
        guiGraphics.pose().translate(mouseX + 3, mouseY + 3, 500.0);
        guiGraphics.pose().scale(0.45F, 0.45F, 1.0F);
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.gameRenderer.getMapRenderer().render(guiGraphics.pose(), guiGraphics.bufferSource(), this.mapId, this.savedData, true, 15728880);
        guiGraphics.pose().popPose();
    }
}
