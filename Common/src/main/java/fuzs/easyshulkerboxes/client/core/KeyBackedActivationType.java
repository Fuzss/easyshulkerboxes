package fuzs.easyshulkerboxes.client.core;

import com.mojang.blaze3d.platform.InputConstants;
import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.puzzleslib.client.core.ClientAbstractions;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;

public class KeyBackedActivationType extends HeldActivationType implements KeyMappingProvider {
    public static final String TOOLTIP_PRESS_TRANSLATION_KEY = "item.container.tooltip.press";

    private final KeyMapping keyMapping;
    private boolean active;

    public KeyBackedActivationType(String id) {
        super(id);
        this.keyMapping = new KeyMapping("key." + id, InputConstants.UNKNOWN.getValue(), "key.categories." + EasyShulkerBoxes.MOD_ID);
    }

    @Override
    public String getIdentifier() {
        return "KEY";
    }

    @Override
    public Component getComponent(String translationId) {
        Component keyName = this.keyMapping.getTranslatedKeyMessage();
        return Component.translatable(translationId, Component.translatable(TOOLTIP_PRESS_TRANSLATION_KEY), Component.empty().append(keyName).withStyle(ChatFormatting.YELLOW)).withStyle(ChatFormatting.GRAY);
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode) {
        if (ClientAbstractions.INSTANCE.isKeyActiveAndMatches(this.keyMapping, keyCode, scanCode)) {
            this.active = !this.active;
            return true;
        }
        return false;
    }

    @Override
    public KeyMapping getKeyMapping() {
        return this.keyMapping;
    }
}
