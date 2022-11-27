package fuzs.easyshulkerboxes.client.init;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

public class ClientModRegistry {
    public static final KeyMapping TOGGLE_VISUAL_CONTENTS_KEY_MAPPING = new KeyMapping("key.toggleVisualTooltipContents", InputConstants.KEY_J, "key.categories.misc");
    public static final KeyMapping TOGGLE_SELECTED_TOOLTIPS_KEY_MAPPING = new KeyMapping("key.toggleSelectedItemTooltips", InputConstants.KEY_K, "key.categories.misc");
}
