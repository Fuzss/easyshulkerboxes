package fuzs.easyshulkerboxes.config;

import fuzs.puzzleslib.config.ConfigCore;
import fuzs.puzzleslib.config.annotation.Config;

import java.util.Objects;

public class ClientConfig implements ConfigCore {
    @Config(description = "Color item inventories on tooltips according to the container item's color.")
    public boolean colorfulTooltips = true;
    @Config(name = "reveal_contents", description = "Select a modifier key required to be held for seeing item inventory contents, otherwise selecting \"KEY\" serves as a toggle.")
    @Config.AllowedValues(values = {"KEY", "SHIFT", "CONTROL", "ALT"})
    String revealContentsRaw = TooltipContentsActivation.TOGGLE_VISUAL_CONTENTS_KEY.toString();
    @Config(description = "Render a white overlay or the hotbar selected item frame over the slot the next item will be taken out of when right-clicking the container item.")
    public SlotOverlay slotOverlay = SlotOverlay.HOVER;
    @Config(description = "Show an indicator on container items when the stack carried by the cursor can be added in your inventory.")
    public boolean containerItemIndicator = true;
    @Config(name = "selected_item_tooltip", description = {"Show a tooltip for the item currently selected in a container item's tooltip next to the main tooltip.", "Select a modifier key required to be held to see that tooltip, otherwise selecting \"KEY\" serves as a toggle."})
    @Config.AllowedValues(values = {"KEY", "SHIFT", "CONTROL", "ALT"})
    String selectedItemTooltipRaw = TooltipContentsActivation.TOGGLE_SELECTED_TOOLTIPS_KEY.toString();
    @Config(name = "precision_mode", description = {"Select a modifier key required to be held to extract/insert only a single item from a container item instead of everything from the selected slot.", "In precision mode left-clicking inserts an item, and right-clicking extracts an item. The scroll wheel can also be used."})
    @Config.AllowedValues(values = {"SHIFT", "CONTROL", "ALT"})
    String extractSingleItemRaw = TooltipContentsActivation.CONTROL.toString();
    @Config(description = "Disable sounds from inserting and extracting items from playing, as they trigger quite often with all the new interactions.")
    public boolean disableInteractionSounds = true;

    public TooltipContentsActivation revealContents;
    public TooltipContentsActivation selectedItemTooltip;
    public TooltipContentsActivation extractSingleItem;

    @Override
    public void afterConfigReload() {
        this.revealContents = TooltipContentsActivation.REVEAL_CONTENTS_BY_NAME.get(this.revealContentsRaw);
        Objects.requireNonNull(this.revealContents, "reveal contents is null");
        this.selectedItemTooltip = TooltipContentsActivation.SELECTED_ITEM_TOOLTIP_BY_NAME.get(this.selectedItemTooltipRaw);
        Objects.requireNonNull(this.selectedItemTooltip, "selected item tooltip is null");
        this.extractSingleItem = TooltipContentsActivation.MODIFIER_KEYS_BY_NAME.get(this.extractSingleItemRaw);
        Objects.requireNonNull(this.extractSingleItem, "extract single item is null");
    }

    public enum SlotOverlay {
        HOTBAR, HOVER
    }
}
