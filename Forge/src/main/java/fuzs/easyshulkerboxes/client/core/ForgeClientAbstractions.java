package fuzs.easyshulkerboxes.client.core;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;

import java.util.List;

public class ForgeClientAbstractions implements ClientAbstractions {

    @Override
    public List<ClientTooltipComponent> getTooltipComponents(Screen screen, Font font, int mouseX, int mouseY, ItemStack stack) {
        return ForgeHooksClient.gatherTooltipComponents(stack, screen.getTooltipFromItem(stack), stack.getTooltipImage(), mouseX, screen.width, screen.height, font, font);
    }
}
