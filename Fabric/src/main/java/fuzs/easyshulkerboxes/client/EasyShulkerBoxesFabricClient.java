package fuzs.easyshulkerboxes.client;

import fuzs.easyshulkerboxes.client.gui.screens.inventory.tooltip.ClientContainerItemTooltip;
import fuzs.easyshulkerboxes.world.inventory.tooltip.ContainerItemTooltip;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

public class EasyShulkerBoxesFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        TooltipComponentCallback.EVENT.register((TooltipComponent data) -> {
            if (data.getClass() == ContainerItemTooltip.class) return new ClientContainerItemTooltip((ContainerItemTooltip) data);
            return null;
        });
    }
}
