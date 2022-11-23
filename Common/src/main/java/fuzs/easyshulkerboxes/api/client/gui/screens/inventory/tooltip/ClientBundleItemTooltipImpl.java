package fuzs.easyshulkerboxes.api.client.gui.screens.inventory.tooltip;

import fuzs.easyshulkerboxes.api.config.ClientConfigCore;
import fuzs.easyshulkerboxes.api.world.inventory.tooltip.BundleItemTooltip;
import net.minecraft.world.item.DyeColor;

public class ClientBundleItemTooltipImpl extends ClientContainerItemTooltip {
    private final boolean isBundleFull;

    public ClientBundleItemTooltipImpl(BundleItemTooltip tooltip, ClientConfigCore config) {
        super(tooltip.items(), DyeColor.BROWN.getTextureDiffuseColors(), config);
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
    protected boolean isBundleFull() {
        return this.isBundleFull;
    }
}
