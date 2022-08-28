package fuzs.easyshulkerboxes.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.client.handler.EnderChestMenuClientHandler;
import fuzs.easyshulkerboxes.mixin.client.accessor.BundleItemAccessor;
import fuzs.easyshulkerboxes.world.item.ContainerItemHelper;
import fuzs.puzzleslib.client.core.ClientCoreServices;
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
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EnderChestBlock;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.IItemDecorator;
import net.minecraftforge.client.event.RegisterItemDecorationsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

@Mod.EventBusSubscriber(modid = EasyShulkerBoxes.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EasyShulkerBoxesForgeClient {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ClientCoreServices.FACTORIES.clientModConstructor(EasyShulkerBoxes.MOD_ID).accept(new EasyShulkerBoxesClient());
        registerHandlers();
    }

    private static void registerHandlers() {
        EnderChestMenuClientHandler enderChestMenuHandler = new EnderChestMenuClientHandler();
        MinecraftForge.EVENT_BUS.addListener((final EntityJoinLevelEvent evt) -> {
            enderChestMenuHandler.onEntityJoinLevel(evt.getEntity(), evt.getLevel());
        });
    }

    @SubscribeEvent
    public static void onRegisterItemDecorations(final RegisterItemDecorationsEvent evt) {
        for (Block block : Registry.BLOCK) {
            if (block instanceof ShulkerBoxBlock) {
                registerContainerItemDecoration(evt::register, block, (ItemStack containerStack, ItemStack carriedStack) -> {
                    return ContainerItemHelper.loadItemContainer(containerStack, BlockEntityType.SHULKER_BOX, 3, false).canAddItem(carriedStack);
                });
            } else if (block instanceof EnderChestBlock) {
                registerContainerItemDecoration(evt::register, block, (ItemStack containerStack, ItemStack carriedStack) -> {
                    return Proxy.INSTANCE.getClientPlayer().getEnderChestInventory().canAddItem(carriedStack);
                });
            }
        }
        for (Item item : Registry.ITEM) {
            if (item instanceof BundleItem) {
                registerContainerItemDecoration(evt::register, item, (ItemStack containerStack, ItemStack carriedStack) -> {
                    // yeah, not copying those methods...
                    int i = (64 - BundleItemAccessor.callGetContentWeight(containerStack)) / BundleItemAccessor.callGetWeight(carriedStack);
                    return i > 0;
                });
            }
        }
    }

    private static void registerContainerItemDecoration(BiConsumer<ItemLike, IItemDecorator> evt, ItemLike itemLike, BiPredicate<ItemStack, ItemStack> filter) {
        evt.accept(itemLike, (Font font, ItemStack stack, int itemPosX, int itemPosY, float blitOffset) -> {
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
                        // font renderer modifies render states, so this tells Forge to reset them
                        return true;
                    }
                }
            }
            return false;
        });
    }
}
