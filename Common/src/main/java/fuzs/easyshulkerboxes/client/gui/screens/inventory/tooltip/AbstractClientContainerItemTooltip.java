package fuzs.easyshulkerboxes.client.gui.screens.inventory.tooltip;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.client.core.ClientAbstractions;
import fuzs.easyshulkerboxes.config.ClientConfig;
import fuzs.easyshulkerboxes.world.inventory.ContainerSlotHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.List;
import java.util.Optional;

public abstract class AbstractClientContainerItemTooltip extends ExpandableClientTooltipComponent {
    public static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation("textures/gui/widgets.png");
    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(EasyShulkerBoxes.MOD_ID, "textures/gui/container/inventory_tooltip.png");
    private static final int BORDER_SIZE = 7;
    private static final MutableInt ACTIVE_CONTAINER_ITEM_TOOLTIPS = new MutableInt();

    protected final NonNullList<ItemStack> items;
    private final float[] backgroundColor;

    public AbstractClientContainerItemTooltip(NonNullList<ItemStack> items, float[] backgroundColor) {
        this.items = items;
        this.backgroundColor = backgroundColor;
    }

    protected abstract int getGridSizeX();

    protected abstract int getGridSizeY();

    protected boolean isSlotBlocked(int itemIndex) {
        return false;
    }

    @Override
    public int getExpandedHeight() {
        return this.getGridSizeY() * 18 + 2 * BORDER_SIZE;
    }

    @Override
    public int getExpandedWidth(Font font) {
        return this.getGridSizeX() * 18 + 2 * BORDER_SIZE;
    }

    @Override
    public void renderExpandedImage(Font font, int mouseX, int mouseY, PoseStack poseStack, ItemRenderer itemRenderer, int blitOffset) {
        ACTIVE_CONTAINER_ITEM_TOOLTIPS.increment();
        float[] color = this.getBackgroundColor();
        if (this.defaultSize()) {
            ContainerTexture.FULL.blit(poseStack, mouseX, mouseY, blitOffset, color);
        } else {
            this.drawBorder(mouseX, mouseY, this.getGridSizeX(), this.getGridSizeY(), poseStack, blitOffset);
        }
        int itemIndex = 0;
        int lastFilledSlot = this.getLastFilledSlot();
        for (int l = 0; l < this.getGridSizeY(); ++l) {
            for (int i1 = 0; i1 < this.getGridSizeX(); ++i1) {
                int posX = mouseX + i1 * 18 + BORDER_SIZE;
                int posY = mouseY + l * 18 + BORDER_SIZE;
                if (!this.defaultSize()) {
                    if (this.isSlotBlocked(itemIndex)) {
                        ContainerTexture.BLOCKED_SLOT.blit(poseStack, posX, posY, blitOffset, color);
                    } else {
                        ContainerTexture.SLOT.blit(poseStack, posX, posY, blitOffset, color);
                    }
                }
                this.drawSlot(posX, posY, itemIndex, font, itemRenderer);
                if (itemIndex == lastFilledSlot) this.drawSlotOverlay(poseStack, posX, posY, blitOffset);
                itemIndex++;
            }
        }
        // reset color for other mods
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawSelectedSlotTooltip(mouseX, mouseY, poseStack, lastFilledSlot, font);
        ACTIVE_CONTAINER_ITEM_TOOLTIPS.decrement();
    }

    private void drawSelectedSlotTooltip(int mouseX, int mouseY, PoseStack poseStack, int lastFilledSlot, Font font) {
        if (!EasyShulkerBoxes.CONFIG.get(ClientConfig.class).selectedItemTooltip.isActive()) return;
        if (ACTIVE_CONTAINER_ITEM_TOOLTIPS.intValue() > 1) return;
        if (lastFilledSlot >= 0 && lastFilledSlot < this.items.size()) {
            ItemStack stack = this.items.get(lastFilledSlot);
            Screen currentScreen = Minecraft.getInstance().screen;
            List<Component> tooltipFromItem = currentScreen.getTooltipFromItem(stack);
            Optional<TooltipComponent> tooltipImage = stack.getTooltipImage();
            List<ClientTooltipComponent> tooltipComponents = ClientAbstractions.INSTANCE.getTooltipComponents(currentScreen, font, mouseX, mouseY, stack);
            int maxWidth = tooltipComponents.stream().mapToInt(tooltipComponent -> tooltipComponent.getWidth(font)).max().orElse(0);
            poseStack.pushPose();
            currentScreen.renderTooltip(poseStack, tooltipFromItem, tooltipImage, mouseX - maxWidth - 2 * 18, mouseY);
            poseStack.popPose();
        }
    }

    private boolean defaultSize() {
        // this is by far the most common size, we use a pre-built image for that
        return this.getGridSizeX() == 9 && this.getGridSizeY() == 3;
    }

    private float[] getBackgroundColor() {
        if (!EasyShulkerBoxes.CONFIG.get(ClientConfig.class).colorfulTooltips) {
            return new float[]{1.0F, 1.0F, 1.0F};
        }
        return this.backgroundColor;
    }

    private int getLastFilledSlot() {
        if (EasyShulkerBoxes.CONFIG.get(ClientConfig.class).slotOverlay == ClientConfig.SlotOverlay.NONE) return -1;
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

    private void drawSlot(int posX, int posY, int itemIndex, Font font, ItemRenderer itemRenderer) {
        if (itemIndex < this.items.size()) {
            ItemStack itemstack = this.items.get(itemIndex);
            itemRenderer.renderAndDecorateItem(itemstack, posX + 1, posY + 1, itemIndex);
            itemRenderer.renderGuiItemDecorations(font, itemstack, posX + 1, posY + 1);
        }
    }

    private void drawSlotOverlay(PoseStack poseStack, int posX, int posY, int blitOffset) {
        if (ACTIVE_CONTAINER_ITEM_TOOLTIPS.intValue() > 1) return;
        ClientConfig.SlotOverlay slotOverlay = EasyShulkerBoxes.CONFIG.get(ClientConfig.class).slotOverlay;
        if (slotOverlay == ClientConfig.SlotOverlay.HOTBAR) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);
            GuiComponent.blit(poseStack, posX - 3, posY - 3, blitOffset + 100, 0, 22, 24, 24, 256, 256);
        } else if (slotOverlay == ClientConfig.SlotOverlay.HOVER) {
            AbstractContainerScreen.renderSlotHighlight(poseStack, posX + 1, posY + 1, blitOffset);
        }
    }

    private enum ContainerTexture {
        SLOT(BORDER_SIZE, BORDER_SIZE, 18, 18),
        BLOCKED_SLOT(BORDER_SIZE * 3 + 18, BORDER_SIZE, 18, 18),
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
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(color[0], color[1], color[2], 1.0F);
            RenderSystem.setShaderTexture(0, TEXTURE_LOCATION);
            GuiComponent.blit(poseStack, posX, posY, blitOffset, this.textureX, this.textureY, this.width, this.height, 256, 256);
        }
    }
}