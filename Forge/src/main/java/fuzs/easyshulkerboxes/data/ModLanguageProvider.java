package fuzs.easyshulkerboxes.data;

import fuzs.easyshulkerboxes.client.handler.KeyBindingHandler;
import fuzs.easyshulkerboxes.config.TooltipContentsActivation;
import net.minecraft.client.KeyMapping;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {

    public ModLanguageProvider(DataGenerator gen, String modId) {
        super(gen, modId, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.add(TooltipContentsActivation.REVEAL_CONTENTS_TRANSLATION_KEY, "%s %s to reveal contents");
        this.add(TooltipContentsActivation.SELECTED_ITEM_TOOLTIP_TRANSLATION_KEY, "%s %s to reveal selected item tooltip");
        this.add(TooltipContentsActivation.TOOLTIP_HOLD_TRANSLATION_KEY, "Hold");
        this.add(TooltipContentsActivation.TOOLTIP_PRESS_TRANSLATION_KEY, "Press");
        this.add(KeyBindingHandler.TOGGLE_VISUAL_CONTENTS_KEY_MAPPING, "Toggle Visual Tooltip Contents");
        this.add(KeyBindingHandler.TOGGLE_SELECTED_TOOLTIPS_KEY_MAPPING, "Toggle Selected Item Tooltips");
    }

    public void add(KeyMapping keyMapping, String value) {
        this.add(keyMapping.getName(), value);
    }
}
