package fuzs.easyshulkerboxes.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(DataGenerator gen, String modid) {
        super(gen, modid, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.add("item.container.tooltip.revealContents", "Hold %s to reveal contents");
        this.add("item.container.tooltip.selectedItemTooltip", "Hold %s to reveal selected item tooltip");
    }
}
