package fuzs.easyshulkerboxes.client;

import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.client.gui.screens.inventory.tooltip.ClientMapContentsTooltip;
import fuzs.easyshulkerboxes.world.inventory.tooltip.MapContentsTooltip;
import fuzs.iteminteractions.api.v1.ItemContentsHelper;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.ClientTooltipComponentsContext;
import fuzs.puzzleslib.api.client.event.v1.gui.ItemTooltipCallback;
import fuzs.puzzleslib.api.config.v3.ConfigHolder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class EasyShulkerBoxesClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        ConfigHolder.registerConfigurationScreen(EasyShulkerBoxes.MOD_ID, "iteminteractions");
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        // hide vanilla shulker box contents on tooltips, they are no longer necessary with our custom rendering
        ItemTooltipCallback.EVENT.register((ItemStack itemStack, List<Component> tooltipLines, Item.TooltipContext tooltipContext, @Nullable Player player, TooltipFlag tooltipType) -> {
            if (itemStack.has(DataComponents.CONTAINER) &&
                    !ItemContentsHelper.getItemContentsBehavior(itemStack).isEmpty()) {
                tooltipLines.removeIf((Component component) -> {
                    if (component.getContents() instanceof TranslatableContents contents) {
                        return contents.getKey().equals("item.container.item_count") ||
                                contents.getKey().equals("item.container.more_items");
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
