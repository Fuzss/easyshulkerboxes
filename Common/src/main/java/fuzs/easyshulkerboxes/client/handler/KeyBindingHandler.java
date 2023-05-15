package fuzs.easyshulkerboxes.client.handler;

import com.mojang.blaze3d.platform.InputConstants;
import fuzs.puzzleslib.client.core.ClientAbstractions;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.Unit;

import java.util.Optional;

public class KeyBindingHandler {
    public static final KeyMapping TOGGLE_VISUAL_CONTENTS_KEY_MAPPING = new KeyMapping("key.toggleVisualTooltipContents", InputConstants.KEY_J, "key.categories.misc");
    public static final KeyMapping TOGGLE_SELECTED_TOOLTIPS_KEY_MAPPING = new KeyMapping("key.toggleSelectedItemTooltips", InputConstants.KEY_K, "key.categories.misc");

    public static boolean allowVisualContents = true;
    public static boolean allowSelectedTooltips = true;

    public static Optional<Unit> onKeyPressed$Pre(Screen screen, int keyCode, int scanCode, int modifiers) {
        if (ClientAbstractions.INSTANCE.isKeyActiveAndMatches(TOGGLE_VISUAL_CONTENTS_KEY_MAPPING, keyCode, scanCode)) {
            allowVisualContents = !allowVisualContents;
            return Optional.of(Unit.INSTANCE);
        }
        if (ClientAbstractions.INSTANCE.isKeyActiveAndMatches(TOGGLE_SELECTED_TOOLTIPS_KEY_MAPPING, keyCode, scanCode)) {
            allowSelectedTooltips = !allowSelectedTooltips;
            return Optional.of(Unit.INSTANCE);
        }
        return Optional.empty();
    }
}
