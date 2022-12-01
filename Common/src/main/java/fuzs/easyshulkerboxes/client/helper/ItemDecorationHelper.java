package fuzs.easyshulkerboxes.client.helper;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.api.world.item.container.ItemContainerProvider;
import fuzs.easyshulkerboxes.config.ClientConfig;
import fuzs.easyshulkerboxes.world.item.storage.ItemContainerProviders;
import fuzs.puzzleslib.client.renderer.entity.DynamicItemDecorator;
import fuzs.puzzleslib.proxy.Proxy;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.function.BooleanSupplier;

public class ItemDecorationHelper {
    private static final Map<ItemContainerProvider, DynamicItemDecorator> DECORATORS_CACHE = Maps.newIdentityHashMap();

    private static DynamicItemDecorator getDynamicItemDecorator(ItemDecoratorPredicate filter, BooleanSupplier allow) {
        return (Font font, ItemStack stack, int itemPosX, int itemPosY, float blitOffset) -> {
            if (!allow.getAsBoolean()) return false;
            return registerContainerItemDecoration(font, stack, itemPosX, itemPosY, blitOffset, filter);
        };
    }

    @SuppressWarnings("ConstantConditions")
    private static boolean registerContainerItemDecoration(Font font, ItemStack stack, int itemPosX, int itemPosY, float blitOffset, ItemDecoratorPredicate filter) {
        if (!(Minecraft.getInstance().screen instanceof AbstractContainerScreen<?> screen)) return false;
        ItemStack carriedStack = screen.getMenu().getCarried();
        if (stack != carriedStack && filter.test(screen, stack, carriedStack)) {
            PoseStack posestack = new PoseStack();
            posestack.translate(0.0, 0.0, blitOffset + 200.0);
            MultiBufferSource.BufferSource multibuffersource$buffersource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
            String s = "+";
            font.drawInBatch(s, (float) (itemPosX + 19 - 2 - font.width(s)), (float) (itemPosY + 6 + 3), ChatFormatting.YELLOW.getColor(), true, posestack.last().pose(), multibuffersource$buffersource, false, 0, 15728880);
            multibuffersource$buffersource.endBatch();
            // font renderer modifies render states, so this tells the implementation to reset them
            return true;
        }
        return false;
    }

    public static void render(Font font, ItemStack stack, int itemPosX, int itemPosY, float blitOffset) {
        resetRenderState();
        Item item = stack.getItem();
        ItemContainerProvider provider = ItemContainerProviders.INSTANCE.get(item);
        if (provider != null) {
            DynamicItemDecorator itemDecorator = DECORATORS_CACHE.computeIfAbsent(provider, provider1 -> ItemDecorationHelper.getDynamicItemDecorator((AbstractContainerScreen<?> screen, ItemStack containerStack, ItemStack carriedStack) -> {
                return provider.canAddItem(containerStack, carriedStack, Proxy.INSTANCE.getClientPlayer());
            }, () -> EasyShulkerBoxes.CONFIG.get(ClientConfig.class).containerItemIndicator));
            if (itemDecorator.renderItemDecorations(font, stack, itemPosX, itemPosY, blitOffset)) {
                resetRenderState();
            }
        }
    }

    private static void resetRenderState() {
        RenderSystem.enableTexture();
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
    }

    public static void clearCache() {
        DECORATORS_CACHE.clear();
    }

    @FunctionalInterface
    public interface ItemDecoratorPredicate {

        boolean test(AbstractContainerScreen<?> screen, ItemStack stack, ItemStack carriedStack);
    }
}
