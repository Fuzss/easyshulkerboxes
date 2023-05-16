package fuzs.easyshulkerboxes.client.handler;

import fuzs.easyshulkerboxes.client.core.HeldActivationType;
import fuzs.easyshulkerboxes.client.core.KeyBackedActivationType;
import fuzs.easyshulkerboxes.client.core.KeyMappingProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.Unit;

import java.util.Optional;

public class KeyBindingTogglesHandler {
    public static final HeldActivationType SHIFT = HeldActivationType.of("Shift", Screen::hasShiftDown);
    public static final HeldActivationType CONTROL = HeldActivationType.of("Control", Minecraft.ON_OSX ? "Cmd" : "Ctrl", Screen::hasControlDown);
    public static final HeldActivationType ALT = HeldActivationType.of("Alt", Screen::hasAltDown);
    public static final HeldActivationType ALWAYS = HeldActivationType.of("Always", () -> true);
    public static final KeyBackedActivationType VISUAL_ITEM_CONTENTS_KEY = new KeyBackedActivationType("toggleVisualItemContents");
    public static final KeyBackedActivationType SELECTED_ITEM_TOOLTIPS_KEY = new KeyBackedActivationType("toggleSelectedItemTooltips");
    public static final KeyBackedActivationType CARRIED_ITEM_TOOLTIPS_KEY = new KeyBackedActivationType("toggleCarriedItemTooltips");

    public static Optional<Unit> onBeforeKeyPressed(Screen screen, int keyCode, int scanCode, int modifiers) {
        for (KeyMappingProvider provider : HeldActivationType.getKeyMappingProviders().toList()) {
            if (provider.keyPressed(keyCode, scanCode)) return Optional.of(Unit.INSTANCE);
        }
        return Optional.empty();
    }
}
