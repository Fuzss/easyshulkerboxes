package fuzs.easyshulkerboxes.api.client.gui.screens.inventory.tooltip;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import fuzs.easyshulkerboxes.api.SimpleInventoryContainersApi;
import fuzs.easyshulkerboxes.api.client.handler.MouseScrollHandler;
import fuzs.easyshulkerboxes.api.config.ClientConfigCore;
import fuzs.easyshulkerboxes.api.world.inventory.ContainerSlotHelper;
import fuzs.easyshulkerboxes.api.world.inventory.tooltip.ContainerItemTooltip;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ClientContainerItemTooltip implements ClientTooltipComponent {
    public static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation("textures/gui/widgets.png");
    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(SimpleInventoryContainersApi.MOD_ID, "textures/gui/container/inventory_tooltip.png");
    private static final Component HOLD_SHIFT_COMPONENT = Component.translatable("item.container.tooltip.info", Component.translatable("item.container.tooltip.shift").withStyle(ChatFormatting.YELLOW)).withStyle(ChatFormatting.GRAY);
    private static final int BORDER_SIZE = 7;

    private final ClientConfigCore config;
    private final NonNullList<ItemStack> items;
    private final int gridSizeX;
    private final int gridSizeY;
    @Nullable
    private final DyeColor backgroundColor;

    public ClientContainerItemTooltip(ContainerItemTooltip tooltip, ClientConfigCore config) {
        this.items = tooltip.items();
        this.gridSizeX = tooltip.gridSizeX();
        this.gridSizeY = tooltip.gridSizeY();
        this.backgroundColor = tooltip.backgroundColor();
        this.config = config;
    }

    @Override
    public void renderText(Font p_169943_, int p_169944_, int p_169945_, Matrix4f p_169946_, MultiBufferSource.BufferSource p_169947_) {
        if (!MouseScrollHandler.showInventoryContents(this.config)) {
            p_169943_.drawInBatch(HOLD_SHIFT_COMPONENT, (float) p_169944_, (float) p_169945_, -1, true, p_169946_, p_169947_, false, 0, 15728880);
        }
    }

    @Override
    public int getHeight() {
        if (!MouseScrollHandler.showInventoryContents(this.config)) {
            return 10;
        }
        return this.gridSizeY * 18 + 2 * BORDER_SIZE;
    }

    @Override
    public int getWidth(Font font) {
        if (!MouseScrollHandler.showInventoryContents(this.config)) {
            return font.width(HOLD_SHIFT_COMPONENT);
        }
        return this.gridSizeX * 18 + 2 * BORDER_SIZE;
    }

    @Override
    public void renderImage(Font font, int mouseX, int mouseY, PoseStack poseStack, ItemRenderer itemRenderer, int blitOffset) {
        if (!MouseScrollHandler.showInventoryContents(this.config)) return;
        float[] color = this.getBackgroundColor();
        if (this.defaultSize()) {
            ContainerTexture.FULL.blit(poseStack, mouseX, mouseY, blitOffset, color);
        } else {
            this.drawBorder(mouseX, mouseY, this.gridSizeX, this.gridSizeY, poseStack, blitOffset);
        }
        int itemIndex = 0;
        int lastFilledSlot = this.getLastFilledSlot();
        for (int l = 0; l < this.gridSizeY; ++l) {
            for (int i1 = 0; i1 < this.gridSizeX; ++i1) {
                int posX = mouseX + i1 * 18 + BORDER_SIZE;
                int posY = mouseY + l * 18 + BORDER_SIZE;
                if (!this.defaultSize()) ContainerTexture.SLOT.blit(poseStack, posX, posY, blitOffset, color);
                this.drawSlot(posX, posY, itemIndex, font, poseStack, itemRenderer, blitOffset);
                if (itemIndex == lastFilledSlot) this.drawSlotOverlay(poseStack, posX, posY, blitOffset);
                itemIndex++;
            }
        }
        // reset color for other mods
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private boolean defaultSize() {
        // this is by far the most common size, we use a pre-built image for that
        return this.gridSizeX == 9 && this.gridSizeY == 3;
    }

    private float[] getBackgroundColor() {
        if (!this.config.colorfulTooltips() || this.backgroundColor == null) {
            return new float[]{1.0F, 1.0F, 1.0F};
        } else if (this.backgroundColor == DyeColor.WHITE) {
            return new float[]{0.9019608F, 0.9019608F, 0.9019608F};
        } else {
            return this.backgroundColor.getTextureDiffuseColors();
        }
    }

    private int getLastFilledSlot() {
        if (this.config.slotOverlay() == ClientConfigCore.SlotOverlay.NONE) return -1;
        int currentContainerSlot = ContainerSlotHelper.getCurrentContainerSlot(Minecraft.getInstance().player);
        if (currentContainerSlot != -1 && currentContainerSlot < this.items.size()) {
            if (!this.items.get(currentContainerSlot).isEmpty()) {
                return currentContainerSlot;
            }
        }
        for (int i = this.items.size() - 1; i >= 0; i--) {
            if (!this.items.get(i).isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    private void drawBorder(int mouseX, int mouseY, int gridSizeX, int gridSizeY, PoseStack poseStack, int blitOffset) {
        float[] color = this.getBackgroundColor();
        ContainerTexture.BORDER_TOP_LEFT.blit(poseStack, mouseX, mouseY, blitOffset, color);
        ContainerTexture.BORDER_TOP_RIGHT.blit(poseStack, mouseX + gridSizeX * 18 + BORDER_SIZE, mouseY, blitOffset, color);

        for (int i = 0; i < gridSizeX; ++i) {
            ContainerTexture.BORDER_TOP.blit(poseStack, mouseX + BORDER_SIZE + i * 18, mouseY, blitOffset, color);
            ContainerTexture.BORDER_BOTTOM.blit(poseStack, mouseX + BORDER_SIZE + i * 18, mouseY + gridSizeY * 18 + BORDER_SIZE, blitOffset, color);
        }

        for (int j = 0; j < gridSizeY; ++j) {
            ContainerTexture.BORDER_LEFT.blit(poseStack, mouseX, mouseY + j * 18 + BORDER_SIZE, blitOffset, color);
            ContainerTexture.BORDER_RIGHT.blit(poseStack, mouseX + gridSizeX * 18 + BORDER_SIZE, mouseY + j * 18 + BORDER_SIZE, blitOffset, color);
        }

        ContainerTexture.BORDER_BOTTOM_LEFT.blit(poseStack, mouseX, mouseY + gridSizeY * 18 + BORDER_SIZE, blitOffset, color);
        ContainerTexture.BORDER_BOTTOM_RIGHT.blit(poseStack, mouseX + gridSizeX * 18 + BORDER_SIZE, mouseY + gridSizeY * 18 + BORDER_SIZE, blitOffset, color);
    }

    private void drawSlot(int posX, int posY, int itemIndex, Font font, PoseStack poseStack, ItemRenderer itemRenderer, int blitOffset) {
        ContainerTexture.SLOT.blit(poseStack, posX, posY, blitOffset, this.getBackgroundColor());
        if (itemIndex < this.items.size()) {
            ItemStack itemstack = this.items.get(itemIndex);
            itemRenderer.renderAndDecorateItem(itemstack, posX + 1, posY + 1, itemIndex);
            itemRenderer.renderGuiItemDecorations(font, itemstack, posX + 1, posY + 1);
        }
    }

    private void drawSlotOverlay(PoseStack poseStack, int posX, int posY, int blitOffset) {
        ClientConfigCore.SlotOverlay slotOverlay = this.config.slotOverlay();
        if (slotOverlay == ClientConfigCore.SlotOverlay.HOTBAR) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);
            GuiComponent.blit(poseStack, posX - 3, posY - 3, blitOffset + 100, 0, 22, 24, 24, 256, 256);
        } else if (slotOverlay == ClientConfigCore.SlotOverlay.HOVER) {
            AbstractContainerScreen.renderSlotHighlight(poseStack, posX + 1, posY + 1, blitOffset);
        }
    }

    private enum ContainerTexture {
        SLOT(BORDER_SIZE, BORDER_SIZE, 18, 18),
        BORDER_TOP_LEFT(0, 0, BORDER_SIZE, BORDER_SIZE),
        BORDER_TOP(BORDER_SIZE, 0, 18, BORDER_SIZE),
        BORDER_TOP_RIGHT(18 + BORDER_SIZE, 0, BORDER_SIZE, BORDER_SIZE),
        BORDER_RIGHT(18 + BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, 18),
        BORDER_BOTTOM_RIGHT(18 + BORDER_SIZE, 18 + BORDER_SIZE, BORDER_SIZE, BORDER_SIZE),
        BORDER_BOTTOM(BORDER_SIZE, 18 + BORDER_SIZE, 18, BORDER_SIZE),
        BORDER_BOTTOM_LEFT(0, 18 + BORDER_SIZE, BORDER_SIZE, BORDER_SIZE),
        BORDER_LEFT(0, BORDER_SIZE, BORDER_SIZE, 18),
        FULL(0, 18 + 2 * BORDER_SIZE, 18 * 9 + 2 * BORDER_SIZE, 18 * 3 + 2 * BORDER_SIZE);

        public final int textureX;
        public final int textureY;
        public final int width;
        public final int height;

        ContainerTexture(int textureX, int textureY, int width, int height) {
            this.textureX = textureX;
            this.textureY = textureY;
            this.width = width;
            this.height = height;
        }

        private void blit(PoseStack poseStack, int posX, int posY, int blitOffset, float[] color) {
            RenderSystem.setShaderColor(color[0], color[1], color[2], 1.0F);
            RenderSystem.setShaderTexture(0, TEXTURE_LOCATION);
            GuiComponent.blit(poseStack, posX, posY, blitOffset, this.textureX, this.textureY, this.width, this.height, 256, 256);
        }
    }
}