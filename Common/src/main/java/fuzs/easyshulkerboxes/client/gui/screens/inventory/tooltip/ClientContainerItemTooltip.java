package fuzs.easyshulkerboxes.client.gui.screens.inventory.tooltip;

public class ClientContainerItemTooltip extends AbstractClientContainerItemTooltip {
    private final int gridSizeX;
    private final int gridSizeY;

    public ClientContainerItemTooltip(fuzs.easyshulkerboxes.world.inventory.tooltip.ContainerItemTooltip tooltip) {
        super(tooltip.items(), tooltip.backgroundColor());
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
