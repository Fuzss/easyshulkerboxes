package fuzs.easyshulkerboxes.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.client.gui.screens.inventory.tooltip.ClientContainerItemTooltip;
import fuzs.easyshulkerboxes.config.ClientConfig;
import fuzs.easyshulkerboxes.mixin.client.accessor.BundleItemAccessor;
import fuzs.easyshulkerboxes.world.inventory.tooltip.ContainerItemTooltip;
import fuzs.easyshulkerboxes.world.item.ContainerItemHelper;
import fuzs.puzzleslib.client.core.ClientModConstructor;
import fuzs.puzzleslib.client.renderer.entity.DynamicItemDecorator;
import fuzs.puzzleslib.proxy.Proxy;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EnderChestBlock;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.BiPredicate;

public class EasyShulkerBoxesClient implements ClientModConstructor {

    @Override
    public void onRegisterClientTooltipComponents(ClientTooltipComponentsContext context) {
        context.registerClientTooltipComponent(ContainerItemTooltip.class, ClientContainerItemTooltip::new);
    }

    @Override
    public void onRegisterItemDecorations(ItemDecorationContext context) {
        for (Block block : Registry.BLOCK) {
            if (block instanceof ShulkerBoxBlock) {
                context.register(block, getDynamicItemDecorator((ItemStack containerStack, ItemStack carriedStack) -> {
                    return ContainerItemHelper.loadItemContainer(containerStack, BlockEntityType.SHULKER_BOX, 3, false).canAddItem(carriedStack);
                }));
            } else if (block instanceof EnderChestBlock) {
                context.register(block, getDynamicItemDecorator((ItemStack containerStack, ItemStack carriedStack) -> {
                    return Proxy.INSTANCE.getClientPlayer().getEnderChestInventory().canAddItem(carriedStack);
                }));
            }
        }
        for (Item item : Registry.ITEM) {
            if (item instanceof BundleItem) {
                context.register(item, getDynamicItemDecorator((ItemStack containerStack, ItemStack carriedStack) -> {
                    int weight = BundleItemAccessor.callGetWeight(carriedStack);
                    // fix java.lang.ArithmeticException: / by zero from Numismatic Overhaul as their coins stack to 99 instead of 64
                    if (weight > 0) {
                        int remainingWeight = (64 - BundleItemAccessor.callGetContentWeight(containerStack)) / weight;
                        return remainingWeight > 0;
                    }
                    return false;
                }));
            }
        }
    }

    private static DynamicItemDecorator getDynamicItemDecorator(BiPredicate<ItemStack, ItemStack> filter) {
        return (Font font, ItemStack stack, int itemPosX, int itemPosY, float blitOffset) -> registerContainerItemDecoration(font, stack, itemPosX, itemPosY, blitOffset, filter);
    }

    @SuppressWarnings("ConstantConditions")
    private static boolean registerContainerItemDecoration(Font font, ItemStack stack, int itemPosX, int itemPosY, float blitOffset, BiPredicate<ItemStack, ItemStack> filter) {
        if (!EasyShulkerBoxes.CONFIG.get(ClientConfig.class).containerItemIndicator) return false;
        if (stack.getCount() == 1) {
            if (Minecraft.getInstance().screen instanceof AbstractContainerScreen<?> screen) {
                ItemStack carriedStack = screen.getMenu().getCarried();
                if (!carriedStack.isEmpty() && stack != carriedStack && carriedStack.getItem().canFitInsideContainerItems() && filter.test(stack, carriedStack)) {
                    PoseStack posestack = new PoseStack();
                    String s = "+";
                    posestack.translate(0.0, 0.0, blitOffset + 200.0);
                    MultiBufferSource.BufferSource multibuffersource$buffersource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
                    font.drawInBatch(s, (float) (itemPosX + 19 - 2 - font.width(s)), (float) (itemPosY + 6 + 3), ChatFormatting.YELLOW.getColor(), true, posestack.last().pose(), multibuffersource$buffersource, false, 0, 15728880);
                    multibuffersource$buffersource.endBatch();
                    // font renderer modifies render states, so this tells the implementation to reset them
                    return true;
                }
            }
        }
        return false;
    }
}
