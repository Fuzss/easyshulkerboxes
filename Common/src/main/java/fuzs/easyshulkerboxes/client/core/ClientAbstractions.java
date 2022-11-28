package fuzs.easyshulkerboxes.client.core;

import fuzs.puzzleslib.util.PuzzlesUtil;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface ClientAbstractions {
    ClientAbstractions INSTANCE = PuzzlesUtil.loadServiceProvider(ClientAbstractions.class);

    List<ClientTooltipComponent> getTooltipComponents(Screen screen, Font font, int mouseX, int mouseY, ItemStack stack);

    boolean isKeyActiveAndMatches(KeyMapping keyMapping, int keyCode, int scanCode);
}
