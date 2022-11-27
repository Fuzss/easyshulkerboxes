package fuzs.easyshulkerboxes.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(DataGenerator gen, String modid) {
        super(gen, modid, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.add("item.container.tooltip.revealContents", "%s %s to reveal contents");
        this.add("item.container.tooltip.selectedItemTooltip", "%s %s to reveal selected item tooltip");
        this.add("item.container.tooltip.hold", "Hold");
        this.add("item.container.tooltip.press", "Press");
        this.add("key.toggleVisualTooltipContents", "Toggle Visual Tooltip Contents");
        this.add("key.toggleSelectedItemTooltips", "Toggle Selected Item Tooltips");
    }
}
