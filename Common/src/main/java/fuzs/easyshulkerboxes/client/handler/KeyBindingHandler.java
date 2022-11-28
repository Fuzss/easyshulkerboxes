package fuzs.easyshulkerboxes.client.handler;

import fuzs.easyshulkerboxes.client.core.ClientAbstractions;
import fuzs.easyshulkerboxes.client.init.ClientModRegistry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.Unit;

import java.util.Optional;

public class KeyBindingHandler {
    public static boolean allowVisualContents = true;
    public static boolean allowSelectedTooltips = true;

    public static Optional<Unit> onKeyPressed$Pre(Screen screen, int keyCode, int scanCode, int modifiers) {
        if (ClientAbstractions.INSTANCE.isKeyActiveAndMatches(ClientModRegistry.TOGGLE_VISUAL_CONTENTS_KEY_MAPPING, keyCode, scanCode)) {
            allowVisualContents = !allowVisualContents;
            return Optional.of(Unit.INSTANCE);
        }
        if (ClientAbstractions.INSTANCE.isKeyActiveAndMatches(ClientModRegistry.TOGGLE_SELECTED_TOOLTIPS_KEY_MAPPING, keyCode, scanCode)) {
            allowSelectedTooltips = !allowSelectedTooltips;
            return Optional.of(Unit.INSTANCE);
        }
        return Optional.empty();
    }
}
