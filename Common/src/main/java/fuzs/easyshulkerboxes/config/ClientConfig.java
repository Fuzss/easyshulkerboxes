package fuzs.easyshulkerboxes.config;

import fuzs.easyshulkerboxes.api.config.ClientConfigCore;
import fuzs.puzzleslib.config.ConfigCore;
import fuzs.puzzleslib.config.annotation.Config;

public class ClientConfig implements ConfigCore, ClientConfigCore {
    @Config(description = "Color item inventories on tooltips according to the container item's color.")
    public boolean colorfulTooltips = true;
    @Config(description = "Select a key required to be held for seeing item inventory contents, otherwise show them always.")
    public TooltipContentsActivation tooltipContentsActivation = TooltipContentsActivation.ALWAYS;
    @Config(name = "slot_overlay", description = "Render a white overlay or the hotbar selected item frame over the slot the next item will be taken out when right-clicking the container item.")
    public SlotOverlay slotOverlay = SlotOverlay.HOVER;
    @Config(description = "Show an indicator on container items when the stack carried by the cursor can be added in your inventory.")
    public boolean containerItemIndicator = true;
    @Config(description = "Show a tooltip for the item currently selected in a container item's tooltip next to the main tooltip, select a key required to be held to see that tooltip.")
    public TooltipContentsActivation selectedItemTooltipActivation = TooltipContentsActivation.ALWAYS;

    @Override
    public boolean colorfulTooltips() {
        return this.colorfulTooltips;
    }

    @Override
    public TooltipContentsActivation tooltipContentsActivation() {
        return this.tooltipContentsActivation;
    }

    @Override
    public SlotOverlay slotOverlay() {
        return this.slotOverlay;
    }

    @Override
    public TooltipContentsActivation selectedItemTooltipActivation() {
        return this.selectedItemTooltipActivation;
    }
}
