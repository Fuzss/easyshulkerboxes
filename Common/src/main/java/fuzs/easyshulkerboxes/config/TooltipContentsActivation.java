package fuzs.easyshulkerboxes.config;

import com.google.common.collect.ImmutableMap;
import fuzs.easyshulkerboxes.client.handler.KeyBindingTogglesHandler;
import fuzs.puzzleslib.proxy.Proxy;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class TooltipContentsActivation {
    public static final TooltipContentsActivation TOGGLE_VISUAL_CONTENTS_KEY = new TooltipContentsActivation("Key") {

        @Override
        public Component getComponent(String translationId) {
            Component keyName = KeyBindingTogglesHandler.TOGGLE_VISUAL_CONTENTS_KEY_MAPPING.getTranslatedKeyMessage();
            return Component.translatable(translationId, Component.translatable(TOOLTIP_PRESS_TRANSLATION_KEY), Component.empty().append(keyName).withStyle(ChatFormatting.YELLOW)).withStyle(ChatFormatting.GRAY);
        }

        @Override
        public boolean isActive() {
            return KeyBindingTogglesHandler.allowVisualContents || KeyBindingTogglesHandler.TOGGLE_VISUAL_CONTENTS_KEY_MAPPING.isUnbound();
        }
    };
    public static final TooltipContentsActivation TOGGLE_SELECTED_TOOLTIPS_KEY = new TooltipContentsActivation("Key") {

        @Override
        public Component getComponent(String translationId) {
            Component keyName = KeyBindingTogglesHandler.TOGGLE_SELECTED_TOOLTIPS_KEY_MAPPING.getTranslatedKeyMessage();
            return Component.translatable(translationId, Component.translatable(TOOLTIP_PRESS_TRANSLATION_KEY), Component.empty().append(keyName).withStyle(ChatFormatting.YELLOW)).withStyle(ChatFormatting.GRAY);
        }

        @Override
        public boolean isActive() {
            return KeyBindingTogglesHandler.allowSelectedTooltips || KeyBindingTogglesHandler.TOGGLE_SELECTED_TOOLTIPS_KEY_MAPPING.isUnbound();
        }
    };
    public static final TooltipContentsActivation SHIFT = new TooltipContentsActivation("Shift") {

        @Override
        public boolean isActive() {
            return Proxy.INSTANCE.hasShiftDown();
        }
    };
    public static final TooltipContentsActivation CONTROL = new TooltipContentsActivation("Control", Minecraft.ON_OSX ? "Cmd" : "Ctrl") {

        @Override
        public boolean isActive() {
            return Proxy.INSTANCE.hasControlDown();
        }
    };
    public static final TooltipContentsActivation ALT = new TooltipContentsActivation("Alt") {

        @Override
        public boolean isActive() {
            return Proxy.INSTANCE.hasAltDown();
        }
    };

    public static final String REVEAL_CONTENTS_TRANSLATION_KEY = "item.container.tooltip.revealContents";
    public static final String SELECTED_ITEM_TOOLTIP_TRANSLATION_KEY = "item.container.tooltip.selectedItemTooltip";
    public static final String TOOLTIP_HOLD_TRANSLATION_KEY = "item.container.tooltip.hold";
    public static final String TOOLTIP_PRESS_TRANSLATION_KEY = "item.container.tooltip.press";
    public static final Map<String, TooltipContentsActivation> MODIFIER_KEYS_BY_NAME = Stream.of(modifierKeys()).collect(ImmutableMap.toImmutableMap(Object::toString, Function.identity()));
    public static final Map<String, TooltipContentsActivation> REVEAL_CONTENTS_BY_NAME = Stream.of(revealContents()).collect(ImmutableMap.toImmutableMap(Object::toString, Function.identity()));
    public static final Map<String, TooltipContentsActivation> SELECTED_ITEM_TOOLTIP_BY_NAME = Stream.of(selectedItemTooltip()).collect(ImmutableMap.toImmutableMap(Object::toString, Function.identity()));

    private final String displayString;
    private final String name;

    public TooltipContentsActivation(String name) {
        this(name, name);
    }

    private TooltipContentsActivation(String displayString, String name) {
        this.displayString = displayString.toUpperCase(Locale.ROOT);
        this.name = name;
    }

    @Override
    public String toString() {
        return this.displayString;
    }

    public Component getComponent(String translationId) {
        return Component.translatable(translationId, Component.translatable(TOOLTIP_HOLD_TRANSLATION_KEY), Component.literal(this.name).withStyle(ChatFormatting.YELLOW)).withStyle(ChatFormatting.GRAY);
    }

    public abstract boolean isActive();

    public static TooltipContentsActivation[] modifierKeys() {
        return new TooltipContentsActivation[]{SHIFT, CONTROL, ALT};
    }

    public static TooltipContentsActivation[] revealContents() {
        return new TooltipContentsActivation[]{TOGGLE_VISUAL_CONTENTS_KEY, SHIFT, CONTROL, ALT};
    }

    public static TooltipContentsActivation[] selectedItemTooltip() {
        return new TooltipContentsActivation[]{TOGGLE_SELECTED_TOOLTIPS_KEY, SHIFT, CONTROL, ALT};
    }
}
