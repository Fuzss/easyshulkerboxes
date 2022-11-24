package fuzs.easyshulkerboxes.api.client.gui.screens.inventory.tooltip;

import fuzs.easyshulkerboxes.api.config.ClientConfigCore;
import fuzs.easyshulkerboxes.api.world.inventory.tooltip.ContainerItemTooltip;

public class ClientContainerItemTooltipImpl extends ClientContainerItemTooltip {
    private final int gridSizeX;
    private final int gridSizeY;

    public ClientContainerItemTooltipImpl(ContainerItemTooltip tooltip, ClientConfigCore config) {
        super(tooltip.items(), tooltip.backgroundColor(), config);
        this.gridSizeX = tooltip.gridSizeX();
        this.gridSizeY = tooltip.gridSizeY();
    }

    @Override
    protected int getGridSizeX() {
        return this.gridSizeX;
    }

    @Override
    protected int getGridSizeY() {
        return this.gridSizeY;
    }
}
