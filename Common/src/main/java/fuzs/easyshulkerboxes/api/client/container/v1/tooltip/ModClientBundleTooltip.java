package fuzs.easyshulkerboxes.api.client.container.v1.tooltip;

import fuzs.easyshulkerboxes.api.client.container.v1.tooltip.AbstractClientContainerItemTooltip;
import fuzs.easyshulkerboxes.api.container.v1.tooltip.ModBundleTooltip;

public class ModClientBundleTooltip extends AbstractClientContainerItemTooltip {
    private final boolean isBundleFull;

    public ModClientBundleTooltip(ModBundleTooltip tooltip) {
        super(tooltip.items(), tooltip.backgroundColor());
        this.isBundleFull = tooltip.isBundleFull();
    }

    @Override
    protected int getGridSizeX() {
        return Math.max(2, (int) Math.ceil(Math.sqrt((double) this.items.size() + 1.0D)));
    }

    @Override
    protected int getGridSizeY() {
        return (int) Math.ceil(((double) this.items.size() + 1.0D) / (double) this.getGridSizeX());
    }

    @Override
    protected boolean isSlotBlocked(int itemIndex) {
        // container is larger by one to allow for adding items, we need to subtract that additional slot again when checking if it is full
        return itemIndex >= this.items.size() - 1 && this.isBundleFull;
    }
}
