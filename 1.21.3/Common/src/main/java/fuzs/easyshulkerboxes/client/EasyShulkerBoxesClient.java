package fuzs.easyshulkerboxes.client;

import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.client.gui.screens.inventory.tooltip.ClientMapContentsTooltip;
import fuzs.easyshulkerboxes.world.inventory.tooltip.MapContentsTooltip;
import fuzs.iteminteractions.api.v1.ItemContentsHelper;
import fuzs.puzzleslib.api.client.core.v1.ClientAbstractions;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.ClientTooltipComponentsContext;
import fuzs.puzzleslib.api.client.event.v1.gui.ItemTooltipCallback;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EasyShulkerBoxesClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        ClientAbstractions.INSTANCE.registerConfigScreenFactory(EasyShulkerBoxes.MOD_ID,
                new String[]{"iteminteractions"});
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        // hide vanilla shulker box contents on tooltips, they are no longer necessary with our custom rendering
        ItemTooltipCallback.EVENT.register((ItemStack itemStack, List<Component> tooltipLines, Item.TooltipContext tooltipContext, @Nullable Player player, TooltipFlag tooltipType) -> {
            if (itemStack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof ShulkerBoxBlock &&
                    !ItemContentsHelper.getItemContentsBehavior(itemStack).isEmpty()) {
                tooltipLines.removeIf((Component component) -> {
                    if (component.getContents() instanceof TranslatableContents contents) {
                        return contents.getKey().equals("container.shulkerBox.itemCount") ||
                                contents.getKey().equals("container.shulkerBox.more");
                    } else {
                        return false;
                    }
                });
            }
        });
    }

    @Override
    public void onRegisterClientTooltipComponents(ClientTooltipComponentsContext context) {
        context.registerClientTooltipComponent(MapContentsTooltip.class, ClientMapContentsTooltip::new);
    }
}
