package fuzs.easyshulkerboxes.config;

import fuzs.puzzleslib.config.ConfigCore;
import fuzs.puzzleslib.config.annotation.Config;

import java.util.Objects;

public class ClientConfig implements ConfigCore {
    @Config(description = "Color item inventories on tooltips according to the container item's color.")
    public boolean colorfulTooltips = true;
    @Config(description = "Select a key required to be held for seeing item inventory contents, otherwise show them always.")
    @Config.AllowedValues(values = {"KEY", "ALWAYS", "SHIFT", "CONTROL", "ALT"})
    private String revealContentsRaw = TooltipContentsActivation.TOGGLE_VISUAL_CONTENTS_KEY.toString();
    @Config(name = "slot_overlay", description = "Render a white overlay or the hotbar selected item frame over the slot the next item will be taken out when right-clicking the container item.")
    public SlotOverlay slotOverlay = SlotOverlay.HOVER;
    @Config(description = "Show an indicator on container items when the stack carried by the cursor can be added in your inventory.")
    public boolean containerItemIndicator = true;
    @Config(description = "Show a tooltip for the item currently selected in a container item's tooltip next to the main tooltip, select a key required to be held to see that tooltip.")
    @Config.AllowedValues(values = {"KEY", "ALWAYS", "SHIFT", "CONTROL", "ALT"})
    private String selectedItemTooltipRaw = TooltipContentsActivation.TOGGLE_SELECTED_TOOLTIPS_KEY.toString();

    public TooltipContentsActivation revealContents;
    public TooltipContentsActivation selectedItemTooltip;

    @Override
    public void afterConfigReload() {
        this.revealContents = Objects.requireNonNull(TooltipContentsActivation.REVEAL_CONTENTS_BY_NAME.get(this.revealContentsRaw), "reveal contents config entry was null");
        this.selectedItemTooltip = Objects.requireNonNull(TooltipContentsActivation.SELECTED_ITEM_TOOLTIP_BY_NAME.get(this.selectedItemTooltipRaw), "selected item tooltip config entry was null");
    }

    public enum SlotOverlay {
        NONE, HOTBAR, HOVER
    }
}
