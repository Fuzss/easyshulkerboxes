package fuzs.easyshulkerboxes.data;

import fuzs.easyshulkerboxes.EasyShulkerBoxes;
import fuzs.easyshulkerboxes.impl.client.core.HeldActivationType;
import fuzs.easyshulkerboxes.impl.client.core.KeyBackedActivationType;
import fuzs.easyshulkerboxes.api.client.container.v1.tooltip.ExpandableClientTooltipComponent;
import fuzs.easyshulkerboxes.impl.client.handler.KeyBindingTogglesHandler;
import net.minecraft.client.KeyMapping;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {

    public ModLanguageProvider(DataGenerator gen, String modId) {
        super(gen, modId, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.add(ExpandableClientTooltipComponent.REVEAL_CONTENTS_TRANSLATION_KEY, "%s %s to reveal contents");
        this.add(HeldActivationType.TOOLTIP_HOLD_TRANSLATION_KEY, "Hold");
        this.add(KeyBackedActivationType.TOOLTIP_PRESS_TRANSLATION_KEY, "Press");
        this.add(KeyBindingTogglesHandler.VISUAL_ITEM_CONTENTS_KEY.getKeyMapping(), "Toggle Visual Item Contents");
        this.add(KeyBindingTogglesHandler.SELECTED_ITEM_TOOLTIPS_KEY.getKeyMapping(), "Toggle Selected Item Tooltips");
        this.add(KeyBindingTogglesHandler.CARRIED_ITEM_TOOLTIPS_KEY.getKeyMapping(), "Toggle Carried Item Tooltips");
        this.add("key.categories." + EasyShulkerBoxes.MOD_ID, EasyShulkerBoxes.MOD_NAME);
    }

    public void add(KeyMapping keyMapping, String value) {
        this.add(keyMapping.getName(), value);
    }
}
