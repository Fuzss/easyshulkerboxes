package fuzs.easyshulkerboxes.api.client.gui.screens.inventory.tooltip;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import fuzs.easyshulkerboxes.api.client.handler.MouseScrollHandler;
import fuzs.easyshulkerboxes.api.config.ClientConfigCore;
import fuzs.easyshulkerboxes.api.world.inventory.tooltip.ContainerItemTooltip;
import fuzs.easyshulkerboxes.api.world.item.container.ContainerSlotHelper;
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
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ClientContainerItemTooltip implements ClientTooltipComponent {
    public static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation("textures/gui/widgets.png");
    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("textures/gui/container/bundle.png");
    private static final Component HOLD_SHIFT_COMPONENT = new TranslatableComponent("item.container.tooltip.info", new TranslatableComponent("item.container.tooltip.shift").withStyle(ChatFormatting.YELLOW)).withStyle(ChatFormatting.GRAY);

    private final ClientConfigCore config;
    private final NonNullList<ItemStack> items;
    private final int containerRows;
    @Nullable
    private final DyeColor backgroundColor;

    public ClientContainerItemTooltip(ContainerItemTooltip tooltip, ClientConfigCore config) {
        this.items = tooltip.items();
        this.containerRows = tooltip.containerRows();
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
        return this.gridSizeY() * 20 + 2 + 4;
    }

    @Override
    public int getWidth(Font font) {
        if (!MouseScrollHandler.showInventoryContents(this.config)) {
            return font.width(HOLD_SHIFT_COMPONENT);
        }
        return this.gridSizeX() * 18 + 2;
    }

    @Override
    public void renderImage(Font p_194042_, int p_194043_, int p_194044_, PoseStack p_194045_, ItemRenderer p_194046_, int p_194047_) {
        if (!MouseScrollHandler.showInventoryContents(this.config)) return;
        int i = this.gridSizeX();
        int j = this.gridSizeY();
        int k = 0;
        int lastFilledSlot = this.getLastFilledSlot();
        for (int l = 0; l < j; ++l) {
            for (int i1 = 0; i1 < i; ++i1) {
                int j1 = p_194043_ + i1 * 18 + 1;
                int k1 = p_194044_ + l * 20 + 1;
                this.renderSlot(j1, k1, k, p_194042_, p_194045_, p_194046_, p_194047_, k == lastFilledSlot);
                k++;
            }
        }

        this.drawBorder(p_194043_, p_194044_, i, j, p_194045_, p_194047_);
        // reset color for other mods
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private int getLastFilledSlot() {
        if (this.config.slotOverlay() != ClientConfigCore.SlotOverlay.NONE) {
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
        }
        return -1;
    }

    private void renderSlot(int posX, int posY, int itemIndex, Font font, PoseStack poseStack, ItemRenderer itemRenderer, int blitOffset, boolean lastFilledSlot) {
        if (itemIndex >= this.items.size()) {
            this.blit(poseStack, posX, posY, blitOffset, Texture.SLOT);
        } else {
            ItemStack itemstack = this.items.get(itemIndex);
            this.blit(poseStack, posX, posY, blitOffset, Texture.SLOT);
            itemRenderer.renderAndDecorateItem(itemstack, posX + 1, posY + 1, itemIndex);
            itemRenderer.renderGuiItemDecorations(font, itemstack, posX + 1, posY + 1);
            if (lastFilledSlot) {
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
        }
    }

    private void drawBorder(int p_194020_, int p_194021_, int p_194022_, int p_194023_, PoseStack p_194024_, int p_194025_) {
        this.blit(p_194024_, p_194020_, p_194021_, p_194025_, Texture.BORDER_CORNER_TOP);
        this.blit(p_194024_, p_194020_ + p_194022_ * 18 + 1, p_194021_, p_194025_, Texture.BORDER_CORNER_TOP);

        for (int i = 0; i < p_194022_; ++i) {
            this.blit(p_194024_, p_194020_ + 1 + i * 18, p_194021_, p_194025_, Texture.BORDER_HORIZONTAL_TOP);
            this.blit(p_194024_, p_194020_ + 1 + i * 18, p_194021_ + p_194023_ * 20, p_194025_, Texture.BORDER_HORIZONTAL_BOTTOM);
        }

        for (int j = 0; j < p_194023_; ++j) {
            this.blit(p_194024_, p_194020_, p_194021_ + j * 20 + 1, p_194025_, Texture.BORDER_VERTICAL);
            this.blit(p_194024_, p_194020_ + p_194022_ * 18 + 1, p_194021_ + j * 20 + 1, p_194025_, Texture.BORDER_VERTICAL);
        }

        this.blit(p_194024_, p_194020_, p_194021_ + p_194023_ * 20, p_194025_, Texture.BORDER_CORNER_BOTTOM);
        this.blit(p_194024_, p_194020_ + p_194022_ * 18 + 1, p_194021_ + p_194023_ * 20, p_194025_, Texture.BORDER_CORNER_BOTTOM);
    }

    private void blit(PoseStack p_194036_, int p_194037_, int p_194038_, int p_194039_, Texture p_194040_) {
        float[] color = this.getBackgroundColor();
        RenderSystem.setShaderColor(color[0], color[1], color[2], 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE_LOCATION);
        GuiComponent.blit(p_194036_, p_194037_, p_194038_, p_194039_, (float) p_194040_.x, (float) p_194040_.y, p_194040_.w, p_194040_.h, 128, 128);
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

    private int gridSizeX() {
        return 9;
    }

    private int gridSizeY() {
        return this.containerRows;
    }

    enum Texture {
        SLOT(0, 0, 18, 20),
        BLOCKED_SLOT(0, 40, 18, 20),
        BORDER_VERTICAL(0, 18, 1, 20),
        BORDER_HORIZONTAL_TOP(0, 20, 18, 1),
        BORDER_HORIZONTAL_BOTTOM(0, 60, 18, 1),
        BORDER_CORNER_TOP(0, 20, 1, 1),
        BORDER_CORNER_BOTTOM(0, 60, 1, 1);

        public final int x;
        public final int y;
        public final int w;
        public final int h;

        Texture(int p_169928_, int p_169929_, int p_169930_, int p_169931_) {
            this.x = p_169928_;
            this.y = p_169929_;
            this.w = p_169930_;
            this.h = p_169931_;
        }
    }
}