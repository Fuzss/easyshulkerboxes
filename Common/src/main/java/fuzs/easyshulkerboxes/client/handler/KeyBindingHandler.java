package fuzs.easyshulkerboxes.client.handler;

import fuzs.easyshulkerboxes.client.init.ClientModRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

public class KeyBindingHandler {
    public static boolean allowVisualContents = true;
    public static boolean allowSelectedTooltips = true;

    public static void onClientTick$Start(Minecraft minecraft) {
        if (!(minecraft.screen instanceof AbstractContainerScreen<?>)) return;
        while (ClientModRegistry.TOGGLE_VISUAL_CONTENTS_KEY_MAPPING.consumeClick()) {
            allowVisualContents = !allowVisualContents;
        }
        while (ClientModRegistry.TOGGLE_SELECTED_TOOLTIPS_KEY_MAPPING.consumeClick()) {
            allowSelectedTooltips = !allowSelectedTooltips;
        }
    }
}
